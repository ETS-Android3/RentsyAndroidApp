package com.menlopark.rentsyuser.ui.activitys;

import static android.Manifest.permission.READ_CONTACTS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.snackbar.Snackbar;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.api.ApiClient;
import com.menlopark.rentsyuser.api.ApiService;
import com.menlopark.rentsyuser.api.ParseUtils;
import com.menlopark.rentsyuser.databinding.ActivityBusinessSignupBinding;
import com.menlopark.rentsyuser.model.Business_signup_model.BSignupParams;
import com.menlopark.rentsyuser.model.Business_signup_model.BusinessResp;
import com.menlopark.rentsyuser.model.location.Detail;
import com.menlopark.rentsyuser.model.location.LocationModel;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.ui.BaseActivity;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class BusinessSignupActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    ActivityBusinessSignupBinding binding;
    private UserLoginTask mAuthTask;
    ApiService apiService;
    Activity act;
    ListView listView;
    String selectedLocationId = "";
    String[] items;

    public List<Detail> citydetails;
    private ArrayList<Detail> cityList;
    ArrayList<String> city = new ArrayList<>();
    com.menlopark.rentsyuser.util.Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        populateAutoComplete();

        init();
        binding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        binding.location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCitiesList();
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
                binding.emailSignInButton.setBackgroundResource(R.drawable.bg_btn_rounded_pink);
                binding.member.setBackgroundColor(getResources().getColor(R.color.trans));
                if (validateFields()) {
                    call_Signup();
                }
            }
        });
        binding.member.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(BusinessSignupActivity.this, LoginActivity.class);
                startActivity(mainIntent);
            }
        });

        binding.terms.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(act, Term_ConditionActivity.class));
            }
        });
        binding.terms.setText(Html.fromHtml("By signing up you agree to our <b> Terms and Conditions </b>"));

    }

    public void showCityDialog(Context context, String title, String[] btnText,
                               DialogInterface.OnClickListener listener) {

        final CharSequence[] items = new CharSequence[cityList.size()];
        int i = 0;
        for (Detail city : cityList) {
            items[i] = city.getName();
            i++;
        }

        if (listener == null)
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface,
                                    int paramInt) {
                    paramDialogInterface.dismiss();
                }
            };
       AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);


        builder.setSingleChoiceItems(items, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Toast.makeText(act, "clicked " + items[item], Toast.LENGTH_SHORT).show();
                        binding.location.setText(cityList.get(item).getName());
                        Toast.makeText(act, cityList.get(item).getId() + "", Toast.LENGTH_SHORT).show();

                        selectedLocationId = cityList.get(item).getId().toString();
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    public void call_Signup() {

        BSignupParams signupParams = new BSignupParams();
        signupParams.setBusiness_name(binding.name.getText().toString());
        signupParams.setContact_no(binding.contact.getText().toString());
        signupParams.setEmail(binding.email.getText().toString());
        signupParams.setLocation_id(selectedLocationId);
        Log.d("bussreg","para-"+binding.name.getText().toString()+" "+binding.contact.getText().toString()+" "+
                binding.email.getText().toString()+" "+selectedLocationId);
        dialog.show();
        Call<BusinessResp> call = apiService.callBusinessSignUp(signupParams);
        call.enqueue(new Callback<BusinessResp>() {
            @Override
            public void onResponse(Call<BusinessResp> call, Response<BusinessResp> response) {
                Log.d("bussreg","res-"+response.code());
                dialog.dismiss();
                if(response.code()==200){
                    Toast.makeText(act, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Intent integer = new Intent(act, LoginActivity.class);
                    startActivity(integer);
                    finish();
                }
                else{
                    Toast.makeText(act, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BusinessResp> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(binding.name.getText().toString())) {
            binding.name.requestFocus();
            Toast.makeText(act, "Enter Business Name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.contact.getText().toString())) {
            binding.contact.requestFocus();
            Toast.makeText(act, "Enter Contact Number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.email.getText().toString())) {
            binding.email.requestFocus();
            Toast.makeText(act, "Enter Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(binding.email.getText().toString())) {
            Toast.makeText(act, "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.location.getText().toString()) || selectedLocationId == "") {
            binding.location.requestFocus();
            Toast.makeText(act, "Select Location", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void init() {
        act = BusinessSignupActivity.this;
        apiService = ApiClient.getClient(PreferenceUtil.getPref(act).getString(Constants.pref_App_Token,"")).create(ApiService.class);
        dialog = new com.menlopark.rentsyuser.util.Dialog(act);
        call_Location(citydetails);
        cityList = new ArrayList<>();

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
                new ArrayAdapter<>(BusinessSignupActivity.this,
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
                ParseUtils.CommonDialog(BusinessSignupActivity.this, getResources().getString(R.string.application_recieved), getResources().getString(R.string.continues), "");
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

    private void cityChoise() {
        final Dialog dialog = new Dialog(act);
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select Location");
        final ListView listView = (ListView) dialog.findViewById(R.id.list_location);
        String[] values = new String[]{"Delhi", "Hyderabad", "Kolkata", "Chennai", "Ahmedabad", "Pune", "Mumbai", "Kanpur", "Jaipur", "Surat", "Lucknow", "Nagpur"};
        dialog.show();

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(act,
                android.R.layout.simple_list_item_2, android.R.id.text2, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) listView.getItemAtPosition(position);
                binding.location.setText(itemValue);
                dialog.cancel();
            }
        });
    }

    public void call_Location(final List<Detail> citydetails) {
        dialog.show();
        Call<LocationModel> call = apiService.callLocation();
        call.enqueue(new Callback<LocationModel>() {
            @Override
            public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                if (response.code() == 200) {
                    dialog.dismiss();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        cityList.add(response.body().getData().get(i));
                    }
                    for (int ii = 0; ii < cityList.size(); ii++) {
                        city.add(cityList.get(ii).getName());
                    }
                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<LocationModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(act, t.toString() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void searchCitiesList() {
        final Dialog dialog = new Dialog(act);
        dialog.setContentView(R.layout.list_location);
        dialog.setTitle("Select City");
        listView = (ListView) dialog.findViewById(R.id.list_location);

        final CharSequence[] items = new CharSequence[cityList.size()];
        int i = 0;
        for (Detail city : cityList) {
            items[i] = city.getName();
            i++;
        }

        dialog.show();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, city) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                binding.location.setText(cityList.get(itemPosition).getName());
                selectedLocationId = cityList.get(itemPosition).getId().toString();
                dialog.cancel();
            }

        });
        adapter.notifyDataSetChanged();
    }
}

