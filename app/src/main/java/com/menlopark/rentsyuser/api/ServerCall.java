package com.menlopark.rentsyuser.api;

import static com.menlopark.rentsyuser.Config.API_URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.server_access.Constants;
import com.menlopark.rentsyuser.util.preferences.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;

/**
 */

public class ServerCall {
    public static boolean setCaching = false;

    public static  String tokenforVolley;
    public static void getResponse(final Context context, String method, final String params, boolean progress, VolleyCallback callback) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait...");
        tokenforVolley= PreferenceUtil.getPref(context).getString(Constants.pref_App_Token,"");

        if (progress) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL + method, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (progressDialog != null && progressDialog.isShowing())
                    progressDialog.dismiss();
                callback.onSuccess(response);
                Log.e("Response: ", response + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                callback.onError(error.toString());

            }
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                Log.e(context.getString(R.string.app_name) + " Params: ", params + "");

                 return params.getBytes();
               // return new JSONObject(params).toString().getBytes();

            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
//                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("app_token",Constants.APP_TOKEN.equals("")?tokenforVolley:Constants.APP_TOKEN);

                return params;
            }

        };

        int socketTimeout = 20000;//20 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        if (setCaching) {
            if (isNetworkAvailable(context)) {
                requestQueue.getCache().clear();
            } else {

            }
        } else {
            requestQueue.getCache().clear();
        }

        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }


    private static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface VolleyCallback {
        void onSuccess(String result);

        void onError(String error);

    }
}
