package my.awesom.app.mycompanion;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
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

import java.util.ArrayList;

import my.awesom.app.mycompanion.Animation.AnimationsClass;
import my.awesom.app.mycompanion.models.MyContacts;
import my.awesom.app.mycompanion.models.MyEventDetails;
import my.awesom.app.mycompanion.services.SettingGeoFenceService;


public class EventLocation extends ActionBarActivity implements OnMapReadyCallback, View.OnClickListener {


    static Marker[] marker = new Marker[1];
    MapFragment mapFragment;
    Toolbar toolbar;
    boolean doesMarkerExist = false;
    private RadioGroup radioGroupSMS, radioGroupTransition;
    private LinearLayout contactLayout;
    private Button contactIntentButton;
    private Button confirmButton;
    private EditText messageBox;
    private String names_list;
    private Circle myCircle;
    private ArrayList<MyContacts> selectedContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);
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

        if (savedInstanceState == null) {
            mapFragment.getMapAsync(this);
        }

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        Location location = googleMap.getMyLocation();
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

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
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
                            Log.i("MYLIST", "SMS eventId is" + eventId);
                            MyEventDetails details = new MyEventDetails(message, selectedContacts, EventLocation.marker[0].getPosition().latitude + "", EventLocation.marker[0].getPosition().longitude + "", eventId, Constants.IS_NOT_PAST, type, transition_type);
                            Geofence geofence = setUpGeoFenceBuilder(eventId, transition_type);
                            new AddLocationToDataBase().execute(details);
                            SettingGeoFenceService.setUp(geofence, eventId, type);
                            startService(new Intent(EventLocation.this, SettingGeoFenceService.class));

                        } else {

                            Toast.makeText(EventLocation.this, "Please select a location", Toast.LENGTH_LONG).show();

                        }


                    } else {
                        AnimationsClass.animateContactBox(contactIntentButton);
                    }

                } else {
                    if (EventLocation.marker[0] != null) {
                        int eventId = Constants.eventId++;
                        MyEventDetails details = new MyEventDetails(message, null, EventLocation.marker[0].getPosition().latitude + "", EventLocation.marker[0].getPosition().longitude + "", eventId, Constants.IS_NOT_PAST, type, transition_type);
                        new AddLocationToDataBase().execute(details);
                        Geofence geofence = setUpGeoFenceBuilder(eventId, transition_type);
                        new AddLocationToDataBase().execute(details);
                        SettingGeoFenceService.setUp(geofence, eventId, type);
                        startService(new Intent(EventLocation.this, SettingGeoFenceService.class));


                    } else {
                        Toast.makeText(EventLocation.this, "Please select a location", Toast.LENGTH_LONG).show();
                    }
                }


            }


        } else if (id == R.id.cancel) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private Geofence setUpGeoFenceBuilder(int eventId, int transition_type) {

        Geofence.Builder geoFenceBuilder = new Geofence.Builder();
        geoFenceBuilder.setRequestId(eventId + "");
        geoFenceBuilder.setCircularRegion(marker[0].getPosition().latitude, marker[0].getPosition().longitude, 100);
        geoFenceBuilder.setExpirationDuration(Geofence.NEVER_EXPIRE);
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
}
