package my.awesom.app.mycompanion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import my.awesom.app.mycompanion.Animation.AnimationsClass;
import my.awesom.app.mycompanion.models.MyContacts;
import my.awesom.app.mycompanion.models.MyEventDetails;
import my.awesom.app.mycompanion.services.SettingGeoFenceService;


public class EventLocation extends ActionBarActivity implements OnMapReadyCallback, View.OnClickListener {


    static Marker[] marker = new Marker[1];
    MapFragment mapFragment;
    Toolbar toolbar;
    boolean doesMarkerExist = false;
    private GoogleMap googleMap;
    private RadioGroup radioGroupSMS, radioGroupTransition;
    private LinearLayout contactLayout;
    private Button contactIntentButton;
    private Button confirmButton;
    private EditText messageBox;
    private String names_list;
    private Circle myCircle;
    private ArrayList<MyContacts> selectedContacts;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);
        EventBus.getDefault().register(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_event_location);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        radioGroupSMS = (RadioGroup) findViewById(R.id.smschoice);
        radioGroupTransition = (RadioGroup) findViewById(R.id.transitionChoice);
        contactLayout = (LinearLayout) findViewById(R.id.contactLayout);
        contactIntentButton = (Button) findViewById(R.id.contactIntentButton);
        messageBox = (EditText) findViewById(R.id.message);
        contactIntentButton.setOnClickListener(this);

        radioGroupSMS.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.yes_sms)
                    contactLayout.setVisibility(View.VISIBLE);

                if (checkedId == R.id.no_sms)
                    contactLayout.setVisibility(View.GONE);
            }
        });

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
        if (savedInstanceState == null) {
            mapFragment.getMapAsync(this);
        } else {
            Bundle b = savedInstanceState;
            messageBox.setText(b.getString("messageBox"));
            mapFragment.getMapAsync(this);
            EventLocation.marker[0].setPosition(new LatLng(b.getDouble("latitude"), b.getDouble("longitude")));
            //setCircle(EventLocation.marker[0].getPosition(), googleMap);
            doesMarkerExist = true;
            Log.i("MYLIST", "Lat : " + b.getDouble("latitude") + "");
            Log.i("MYLIST", "Lon : " + b.getDouble("longitude"));


        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        Log.i("MYLIST", "MAP READY");
        googleMap = gMap;
        googleMap.setMyLocationEnabled(true);
        // Location location = googleMap.getMyLocation();

        final Marker[] marker = new Marker[1];

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (!doesMarkerExist) {
                    googleMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions().draggable(true).position(latLng).title("You are here");
                    EventLocation.marker[0] = googleMap.addMarker(markerOptions);
                    doesMarkerExist = true;
                } else {
                    EventLocation.marker[0].setPosition(latLng);
                }

                setCircle(EventLocation.marker[0].getPosition(), googleMap);


            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                EventLocation.marker[0] = marker;
                setCircle(EventLocation.marker[0].getPosition(), googleMap);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                EventLocation.marker[0] = marker;
                setCircle(EventLocation.marker[0].getPosition(), googleMap);

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                EventLocation.marker[0] = marker;
                setCircle(EventLocation.marker[0].getPosition(), googleMap);

            }
        });
    }

    private void setCircle(LatLng position, GoogleMap googleMap) {
        if (myCircle != null) {
            myCircle.remove();
        }

        // change radius to size of radius of geofence;

        CircleOptions circleOptions = new CircleOptions()
                .center(EventLocation.marker[0].getPosition())
                .radius(100)
                .strokeColor(Color.BLACK)
                .fillColor(0x88ff0000)
                .strokeWidth(5);
        myCircle = googleMap.addCircle(circleOptions);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        names_list = "";
        if (requestCode == Constants.PICKED_CONTACTS && data != null) {
            selectedContacts = data.getParcelableArrayListExtra(Constants.CONTACT_DATA);

            if (selectedContacts != null) {
                for (int i = 0; i < selectedContacts.size(); i++)
                    names_list += selectedContacts.get(i).getName() + "\n";

                ((TextView) findViewById(R.id.contacts_selected)).setText(names_list);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        boolean flag = false;
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            startActivity(new Intent(EventLocation.this, MainActivity.class));
            finish();

        } else if (id == R.id.accept) {

            int type, transition_type;
            String message;

            if (radioGroupSMS.getCheckedRadioButtonId() == R.id.yes_sms) {
                type = Constants.TYPE_LOCATION_SMS;
            } else {
                type = Constants.TYPE_LOCATION_NO_SMS;
            }

            if (radioGroupTransition.getCheckedRadioButtonId() == R.id.transition_enter) {
                transition_type = Constants.TRANSITION_ENTER;
            } else {
                transition_type = Constants.TRANSITION_LEAVE;
            }


            message = messageBox.getText().toString();


            if (message.isEmpty()) {
                AnimationsClass.animateInvalidInput(findViewById(R.id.messageLayout));
                messageBox.setError("Cannot be Empty");
            } else {
                if (type == Constants.TYPE_LOCATION_SMS) {
                    if (selectedContacts != null) {
                        if (EventLocation.marker[0] != null) {
                            int eventId = Constants.eventId++;
                            Geofence geofence = setUpGeoFenceBuilder(eventId, transition_type);

                            Log.i("MYLIST", "I am going to find  geocode");
                            String geoCode = getAddress(EventLocation.marker[0].getPosition().latitude, EventLocation.marker[0].getPosition().longitude);
                            Log.i("MYLIST", "Yippeeee geocode " + geoCode);

                            MyEventDetails details = null;
                            details = new MyEventDetails(message, selectedContacts, geoCode, eventId, Constants.IS_NOT_PAST, type, transition_type);
                            Log.i("MYLIST", "I found  geocode " + geoCode);


                            new AddLocationToDataBase().execute(details);
                            SettingGeoFenceService.setUp(geofence, eventId, type);
                            startService(new Intent(EventLocation.this, SettingGeoFenceService.class));
                            flag = true;

                        } else {

                            Toast.makeText(EventLocation.this, "Please select a location", Toast.LENGTH_LONG).show();
                            flag = false;
                        }


                    } else {
                        AnimationsClass.animateContactBox(contactIntentButton);
                        flag = false;
                    }

                } else if (type == Constants.TYPE_LOCATION_NO_SMS) {
                    if (EventLocation.marker[0] != null) {
                        int eventId = Constants.eventId++;
                        Geofence geofence = setUpGeoFenceBuilder(eventId, transition_type);
                        SettingGeoFenceService.setUp(geofence, eventId, type);

                        Log.i("MYLIST", "I am going to find  geocode");
                        String geoCode = getAddress(EventLocation.marker[0].getPosition().latitude, EventLocation.marker[0].getPosition().longitude);

                        Toast.makeText(EventLocation.this, geoCode, Toast.LENGTH_LONG).show();
                        Log.i("MYLIST", "geocode is : " + geoCode);

                        MyEventDetails details = null;
                        Log.i("MYLIST", "creating detail object");
                        Log.i("MYLIST", "geocode is : " + geoCode);
                        details = new MyEventDetails(message, null, geoCode, eventId, Constants.IS_NOT_PAST, type, transition_type);
                        Log.i("MYLIST", "Done creating detail object");
                        Log.i("MYLIST", "geocode is : " + geoCode);
                        new AddLocationToDataBase().execute(details);
                        Log.i("MYLIST", "Adding to Database");
                        startService(new Intent(EventLocation.this, SettingGeoFenceService.class));
                        Log.i("MYLIST", "Starting service");

                        flag = true;

                    } else {
                        Toast.makeText(EventLocation.this, "Please select a location", Toast.LENGTH_LONG).show();
                        flag = false;
                    }
                }


            }
            if (flag) {
               /* startActivity(new Intent(EventLocation.this, MainActivity.class));
                finish();*/
            }
        } else if (id == R.id.cancel) {
            startActivity(new Intent(EventLocation.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("contacts", selectedContacts);
        outState.putInt("radiosms", radioGroupSMS.getCheckedRadioButtonId());
        outState.putInt("radiotransition", radioGroupTransition.getCheckedRadioButtonId());
        outState.putString("message", messageBox.getText().toString());
        outState.putDouble("latitude", EventLocation.marker[0].getPosition().latitude);
        outState.putDouble("latitude", EventLocation.marker[0].getPosition().longitude);

    }

    public void onEventMainThread(ShowDialogForGeoCode s) {
        progressDialog = null;
        progressDialog = ProgressDialog.show(EventLocation.this, "Please Wait ...", "Setting Reminders!");
    }

    public void onEventMainThread(CloseDialogForGeoCode c) {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = null;
    }

    private String getAddress(double latitude, double longitude) {
        String address = "";
        try {
            address = new GetGeoCode().execute(latitude, longitude).get();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.i("MYLIST", "address is : " + address);
        //return geoCode;
        return address;
    }

    private Geofence setUpGeoFenceBuilder(int eventId, int transition_type) {

        Geofence.Builder geoFenceBuilder = new Geofence.Builder();
        geoFenceBuilder.setRequestId(eventId + "");
        geoFenceBuilder.setCircularRegion(marker[0].getPosition().latitude, marker[0].getPosition().longitude, 100);
        geoFenceBuilder.setExpirationDuration(Constants.time);
        if (transition_type == Constants.TRANSITION_ENTER) {
            geoFenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER);
        } else {
            geoFenceBuilder.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_EXIT);
        }

        return geoFenceBuilder.build();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.contactIntentButton) {
            Intent contactPick = new Intent(EventLocation.this, Contacts.class);
            startActivityForResult(contactPick, Constants.PICKED_CONTACTS);
        }
    }


    class AddLocationToDataBase extends AsyncTask<MyEventDetails, Void, Void> {

        @Override
        protected Void doInBackground(MyEventDetails... params) {
            Database.addEvent(params[0]);
            return null;
        }
    }


    private class GetGeoCode extends AsyncTask<Double, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EventBus.getDefault().post(new ShowDialogForGeoCode());
        }

        @Override
        protected String doInBackground(Double... params) {

            String geoCode = "";

            Geocoder geocoder = new Geocoder(EventLocation.this, Locale.getDefault());
            List<Address> address = null;
            try {
                address = geocoder.getFromLocation(params[0], params[1], 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (address.size() > 0) {
                for (int i = 0; i < address.get(0).getMaxAddressLineIndex(); i++)
                    geoCode += address.get(0).getAddressLine(i) + "\n";

            }
            return geoCode;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            EventBus.getDefault().post(new CloseDialogForGeoCode());
            startActivity(new Intent(EventLocation.this, MainActivity.class));
            finish();
        }
    }

    class ShowDialogForGeoCode {
    }


    class CloseDialogForGeoCode {
    }


}
