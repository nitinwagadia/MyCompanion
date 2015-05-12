package my.awesom.app.mycompanion.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import my.awesom.app.mycompanion.Constants;


public class SettingGeoFenceService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private static Geofence geofence;
    private static int type;
    private static int requestNumber;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private GeofencingRequest geofencingRequest;
    private PendingIntent pendingIntent;

    public SettingGeoFenceService() {
        super("SettingGeoFenceService");
    }

    public static void setUp(Geofence geofence, int eventId, int type) {
        SettingGeoFenceService.type = type;
        SettingGeoFenceService.requestNumber = eventId;
        SettingGeoFenceService.geofence = geofence;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            setGoogleApiClient();

        }


    }

    private void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(this).build();

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient.connect();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Intent intent;

        geofencingRequest = new GeofencingRequest.Builder().addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER).build();

        if (type == Constants.TYPE_LOCATION_SMS) {
            intent = new Intent(this, ServiceForSMS.class);
            intent.putExtra("requestcode", requestNumber + "");
        } else {
            intent = new Intent(this, ServiceDialog.class);
            intent.putExtra("1", "I am Location service");
            intent.putExtra("requestcode", requestNumber + "");

        }

        pendingIntent = PendingIntent.getService(this, requestNumber, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingResult<Status> pendingResult = LocationServices.GeofencingApi
                .addGeofences(googleApiClient, geofencingRequest,
                        pendingIntent);

        pendingResult.setResultCallback(this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("MYLIST", "Suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i("MYLIST", "Connection failed");

    }

    @Override
    public void onResult(Status status) {

        Log.i("MYLIST", "I am in Result");
        if (status.isSuccess()) {
            //LocationServices.GeofencingApi.removeGeofences(this.googleApiClient, this.pendingIntent);
            Log.i("MYLIST", "Success");
            Log.i("MYLIST", requestNumber + "");


        } else if (status.isCanceled()) {
            Log.i("MYLIST", "Cancelled");

        } else if (status.isInterrupted()) {
            Log.i("MYLIST", "Interuppted");

        } else if (status.hasResolution()) {
            Log.i("MYLIST", "Has resolution");

        }
    }


}
