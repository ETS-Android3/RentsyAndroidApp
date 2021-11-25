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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ParseUtils;
import com.menlopark.rentsyuser.databinding.ActivitySignupBinding;
import com.menlopark.rentsyuser.model.ResponseModel;
import com.menlopark.rentsyuser.model.signup.SignupParams;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.BaseActivity;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class SignupActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;
    static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"};
    ActivitySignupBinding binding;
    private UserLoginTask mAuthTask;
    ApiService apiService;
    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateAutoComplete();
        init();
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

        binding.member.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.member.setBackgroundResource(R.drawable.bg_btn_rounded_pink);
                binding.emailSignInButton.setBackgroundColor(getResources().getColor(R.color.trans));
            }
        });

        binding.emailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    call_Signup();
                }
            }
        });
        binding.businessSingup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(SignupActivity.this, BusinessSignupActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.member.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.terms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act,Term_ConditionActivity.class));
            }
        });
        binding.businessSingup.setText(Html.fromHtml(getString(R.string.put_business)));
        binding.terms.setText(Html.fromHtml("By signing up you agree to our <b> Terms and Conditions </b>"));
    }

    void init() {
        act = SignupActivity.this;
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);
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
                    .setAction(android.R.string.ok, new OnClickListener() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
    private boolean validateFields() {
        if (TextUtils.isEmpty(binding.name.getText().toString())) {
            binding.name.requestFocus();
            Toast.makeText(act, getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.contact.getText().toString())) {
            binding.contact.requestFocus();
            Toast.makeText(act, getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();

            return false;
        } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
                binding.email.requestFocus();
                Toast.makeText(act, getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
                return false;
        }
        else if (!isEmailValid(binding.email.getText().toString())) {
            Toast.makeText(act, getString(R.string.error_invalid_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (TextUtils.isEmpty(binding.password.getText().toString())) {
            binding.password.requestFocus();
            Toast.makeText(act, getString(R.string.error_field_required), Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isPasswordValid(binding.password.getText().toString())) {
            Toast.makeText(act, getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
           return false;
        }else {
            return true;
        }
    }

    private void attemptLogin() {

        if (mAuthTask != null) {
            return;
        }

        binding.email.setError(null);
        binding.password.setError(null);
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        boolean cancel = false;
        View focusView = null;
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
        }

    private boolean isEmailValid(String email) {
        Boolean valid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return valid;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(SignupActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
        binding.email.setAdapter(adapter);
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
                if (pieces[0].equals(mEmail)) { return pieces[1].equals(mPassword);
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                ParseUtils.CommonDialog(SignupActivity.this, getResources().getString(R.string.application_recieved), getResources().getString(R.string.continues), "");
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

    public void call_Signup() {
        SignupParams signupParams = new SignupParams();
        signupParams.setName(binding.name.getText().toString());
        signupParams.setContact_no(binding.contact.getText().toString());
        signupParams.setEmail(binding.email.getText().toString());
        signupParams.setPassword(binding.password.getText().toString());

        Call<ResponseModel> call = apiService.callSignup(signupParams);
        showProgress(true);
        call.enqueue(new retrofit2.Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, retrofit2.Response<ResponseModel> response) {
                Log.d("register_res","res100--"+response.code());
                showProgress(false);
                if (response.code() == 200) {
                    ResponseModel model=response.body();
                    if(model.getStatus().equals("SUCCESS")){
                        Log.d("register_res","res1--"+response.body().getMessage());
                        Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(act, LoginActivity.class);
                        i.putExtra("email",binding.email.getText().toString());
                        startActivity(i);
                        finish();
                    }
                    else{
                        Log.d("register_res","res1222--"+response.body().getMessage());
                        Toast.makeText(act, model.getError().toString(), Toast.LENGTH_SHORT).show();

                    }

                } else {
                    Log.d("register_res","res2--"+response.body().getMessage());
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.d("register_res","res2--"+t.getMessage());
                showProgress(false);
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

