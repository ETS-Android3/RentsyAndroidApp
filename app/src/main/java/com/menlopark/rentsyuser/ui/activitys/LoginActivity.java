package com.menlopark.rentsyuser.ui.activitys;

import static android.Manifest.permission.READ_CONTACTS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.menlopark.rentsyuser.Config;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.databinding.ActivityLoginBinding;
import com.menlopark.rentsyuser.model.Facebook_Model.FacebookRes;
import com.menlopark.rentsyuser.model.login.LoginModel;
import com.menlopark.rentsyuser.model.login.LoginParams;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.BaseActivity;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    ActivityLoginBinding binding;
    private UserLoginTask mAuthTask;
    ApiService apiService;
    Activity act;

    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String firstName, lastName, email, birthday, gender;
    private URL profilePicture;
    private String userId;
    private String TAG = "LoginActivity";
    FacebookCallback<LoginResult> callback;


    String sFB_id, sEmail, sName;
    Gson gson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getKeyHash();


        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateAutoComplete();
        callbackManager = CallbackManager.Factory.create();
        init();
        loginButton.setReadPermissions("email", "user_posts");
        loginButton.registerCallback(callbackManager, callback);
        loginCallBack();
        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        binding.forgotPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.forgotPwd.setBackgroundResource(R.drawable.bg_btn_rounded_pink);
                binding.emailSignInButton.setBackgroundColor(getResources().getColor(R.color.trans));
                Intent mainIntent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(mainIntent);
            }
        });

        binding.emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.emailSignInButton.setBackgroundResource(R.drawable.bg_btn_rounded_pink);
                binding.forgotPwd.setBackgroundColor(getResources().getColor(R.color.trans));
                attemptLogin();
            }
        });
        binding.singUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(LoginActivity.this, SignupActivity.class);
                mainIntent.putExtra("isRegister", true);
                startActivity(mainIntent);
            }
        });

        binding.facebookLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });
    }

    void init() {
        act = LoginActivity.this;
        if (getIntent().hasExtra("email")) {
            binding.emailAddress.setText(getIntent().getStringExtra("email"));
        }
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loginButton = (LoginButton) findViewById(R.id.login_button);


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        accessTokenTracker.startTracking();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.e("FB_ID", newProfile.getId());
                sFB_id = newProfile.getId();
                sName = newProfile.getFirstName();
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(binding.emailLoginForm, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        binding.emailAddress.setError(null);
        binding.password.setError(null);
        String email = binding.emailAddress.getText().toString();
        String password = binding.password.getText().toString();
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "EmailId  " + getString(R.string.error_empty_value), Toast.LENGTH_SHORT).show();
            focusView = binding.emailAddress;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Toast.makeText(LoginActivity.this, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            focusView = binding.emailAddress;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Password  " + getString(R.string.error_empty_value), Toast.LENGTH_SHORT).show();
            focusView = binding.password;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            call_Login(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        Boolean valid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return valid;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            binding.loginProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            if (binding.loginProgress.getVisibility() == View.VISIBLE) {
                binding.relMain.setBackgroundColor(getResources().getColor(R.color.bottom_gray));
            }
        } else {
            binding.loginProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            if (binding.loginProgress.getVisibility() == View.VISIBLE) {
                binding.relMain.setBackgroundColor(getResources().getColor(R.color.bottom_gray));
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    return pieces[1].equals(mPassword);
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("isRegister", false);
                startActivity(mainIntent);
                finish();
            } else {
                binding.password.setError(getString(R.string.error_incorrect_password));
                binding.password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void call_Login_FB(String sFb_Id, String email, String name) {
        LoginParams loginParams = new LoginParams();

        loginParams.setFb_id(sFb_Id);
        loginParams.setName(name);
        loginParams.setEmail(email);

        Call<FacebookRes> call = apiService.callFacebookLogin(loginParams);
        showProgress(true);
        call.enqueue(new retrofit2.Callback<FacebookRes>() {
            @Override
            public void onResponse(Call<FacebookRes> call, retrofit2.Response<FacebookRes> response) {
                showProgress(false);
                if (response.body().getStatus() == 200) {
                    Intent mainIntent = new Intent(act, LocationActivity.class);
                    Config.setPreference(Constants.pref_App_Token, response.body().getData().getCustomer().getAppToken().toString());
                    PreferenceUtil.getPref(act).edit()
                            .putString(Constants.pref_App_Token, response.body().getData().getCustomer().getAppToken().toString())
                            .commit();
                    Constants.APP_TOKEN = response.body().getData().getCustomer().getAppToken().toString();

                    mainIntent.putExtra("isRegister", false);
                    startActivity(mainIntent);
                    finish();
                } else {
                    showProgress(false);
                    LoginManager.getInstance().logOut();
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FacebookRes> call, Throwable t) {
                showProgress(false);
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "passvalue.rathod.com.dixit__practical",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    void loginCallBack() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.e(TAG, object.toString());
                        Log.e(TAG, response.toString());
                        try {
                            userId = object.getString("id");
                            sFB_id = object.getString("id");
                            profilePicture = new URL("https://graph.facebook.com/" + userId + "/picture?width=500&height=500");
                            if (object.has("first_name"))
                                firstName = object.getString("first_name");
                            if (object.has("last_name"))
                                lastName = object.getString("last_name");
                            sName = object.getString("first_name") + " " + object.getString("last_name");

                            if (object.has("email"))
                                email = object.getString("email");
                            sEmail = object.getString("email");
                            if (object.has("birthday"))
                                birthday = object.getString("birthday");
                            if (object.has("gender"))
                                gender = object.getString("gender");
                            call_Login_FB(sFB_id, sEmail, sName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, birthday, gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }

    protected void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void call_Login(String username, String password) {
        LoginParams loginParams = new LoginParams();
//        loginParams.setUsername(username);
//        loginParams.setPassword(password);
//        loginParams.setDevice_token(FirebaseInstanceId.getInstance().getToken());
//        loginParams.setDevice_type("1");

        loginParams.setEmail(username);
        loginParams.setPassword(password);
        loginParams.setDevice_token(Config.getPreference(Constants.pref_FCM_token));
        loginParams.setDevice_type("1");

        Call<LoginModel> call = apiService.callLogin(loginParams);
        showProgress(true);
        call.enqueue(new retrofit2.Callback<LoginModel>() {
            @Override
            public void onResponse(Call<LoginModel> call, retrofit2.Response<LoginModel> response) {
                showProgress(false);
                Log.d("loginres","res--"+response.code()+"");
                if (response.code() == 200) {

                    if(!response.body().getStatus().equals("FAIL")){
                        LoginModel loginModel = new LoginModel();
                        Intent mainIntent = new Intent(act, LocationActivity.class);
//                    Config.setPreference(Constants.pref_App_Token, response.body().getData().getCustomer().getAppToken().toString());
//                    Config.setPreference(Constants.pref_CustomerId, response.body().getData().getCustomer().getId().toString());
//                    Config.setPreference(Constants.pref_CustomerEmail, response.body().getData().getCustomer().getEmail());

                        Config.setPreference(Constants.pref_App_Token, response.body().getAccess_token());
                        Config.setPreference(Constants.pref_CustomerId, String.valueOf(response.body().getData().getId()));
                        Config.setPreference(Constants.pref_CustomerEmail, response.body().getData().getEmail());



//
//                    PreferenceUtil.getPref(act).edit()
//                            .putString(Constants.pref_App_Token, response.body().getData().getCustomer().getAppToken().toString())
//                            .putString(Constants.pref_CustomerId, response.body().getData().getCustomer().getId().toString())
//                            .putString(Constants.pref_CustomerEmail, response.body().getData().getCustomer().getEmail())
//                            .apply();


                        PreferenceUtil.getPref(act).edit()
                                .putString(Constants.pref_App_Token, response.body().getAccess_token())
                                .putString(Constants.pref_CustomerId, String.valueOf(response.body().getData().getId()))
                                .putString(Constants.pref_CustomerEmail,response.body().getData().getEmail())
                                .apply();




                        Constants.APP_TOKEN = response.body().getAccess_token();

                        Log.d("loginres","res---"+response.body().getAccess_token()+" "+String.valueOf(response.body().getData().getId())+" "+
                                response.body().getData().getEmail()+" "+Constants.APP_TOKEN);
                        mainIntent.putExtra("isRegister", false);
                        startActivity(mainIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(act, getString(R.string.error_incorrect_password), Toast.LENGTH_SHORT).show();
                    binding.password.requestFocus();
                }
            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                showProgress(false);
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

