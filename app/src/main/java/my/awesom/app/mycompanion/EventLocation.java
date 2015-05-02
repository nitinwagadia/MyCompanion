package my.awesom.app.mycompanion;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventLocation extends ActionBarActivity implements OnMapReadyCallback {


    MapFragment mapFragment;
    GoogleMap googleMap;
    boolean doesMarkerExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_location);
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
                Toast.makeText(EventLocation.this, "Latitude is " + latLng.latitude + " Longitude is " + latLng.longitude, Toast.LENGTH_SHORT).show();
                if (!doesMarkerExist) {
                    MarkerOptions markerOptions = new MarkerOptions().draggable(true).position(latLng).title("You are here");
                    marker[0] = googleMap.addMarker(markerOptions);
                    doesMarkerExist = true;
                } else {
                    marker[0].setPosition(latLng);
                }

            }
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Log.i("MYLIST", "Latitude is : " + marker.getPosition().latitude + "Longitude is " + marker.getPosition().longitude);

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Log.i("MYLIST", "Latitude is : " + (int) marker.getPosition().latitude + "Longitude is " + (int) marker.getPosition().longitude);

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Log.i("MYLIST", "Latitude is : " + marker.getPosition().latitude + "Longitude is " + marker.getPosition().longitude);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
