package com.menlopark.rentsyuser.util;

/**
 * Created by dixit on 9/5/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.LinkedHashSet;
import java.util.Set;


public class GoogleClientApiHelper implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final String TAG = GoogleClientApiHelper.class.getSimpleName();
    public static boolean LOG_ON = false;

    public static int LOCATION_GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    public static int LOCATION_GPS_INTERVAL_MILLIS = 60000;//60 seg
    public static int LOCATION_GPS_FASTEST_INTERVAL_MILLIS = 30000;//30seg
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private GoogleApiClient mGoogleApiClient;
    private Set<LocationListener> locationListeners;
    Context context;

    public GoogleClientApiHelper(Context context) {
        this.context = context;

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this);
        builder.addApi(LocationServices.API);
        mGoogleApiClient = builder.build();

        mGoogleApiClient.connect();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                showSettingDialog();
            } else {
                checkLocationPermission();
            }
        } else {


        }

    }

    public void onResume(LocationListener locationListener) {
        log("connect()");
        mGoogleApiClient.connect();


        addLocationListeners(locationListener);

    }

    private void log(String s) {
        if (LOG_ON) {
            Log.v(TAG, s);
        }
    }

    public void onPause() {
        if (mGoogleApiClient.isConnected()) {

            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

            log("disconnect()");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        log("onConnected()");

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_GPS_INTERVAL_MILLIS); // 10 segundos
        mLocationRequest.setFastestInterval(LOCATION_GPS_FASTEST_INTERVAL_MILLIS); // 5 segundos
        mLocationRequest.setPriority(LOCATION_GPS_PRIORITY);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int status) {
        log("onConnectionSuspended(): " + status);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        log("onConnectionFailed(): " + connectionResult);
    }

    private void addLocationListeners(LocationListener locationListeners) {
        if (this.locationListeners == null) {
            this.locationListeners = new LinkedHashSet<>();
        }
        this.locationListeners.add(locationListeners);
    }

    public String getLastLocationString() {
        Location l = getLastLocation();
        if (l != null) {
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();
            return String.format("lat/lng: %s/%s", latitude, longitude);
        } else {
            return "lat/lng: 0/0";
        }

    }

    public Location getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        return l;
    }

    @Override
    public void onLocationChanged(Location location) {
        log("onLocationChanged(): " + location);

        if (locationListeners != null) {
            for (LocationListener listener : locationListeners) {
                listener.onLocationChanged(location);
            }
        }
    }


    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                        try {

                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


}
