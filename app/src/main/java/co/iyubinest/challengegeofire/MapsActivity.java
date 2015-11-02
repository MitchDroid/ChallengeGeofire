package co.iyubinest.challengegeofire;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings ("ResourceType") public class MapsActivity extends FragmentActivity
    implements OnMapReadyCallback
{

    private GoogleMap mMap;

    private LocationManager locationmanager;

    private Firebase firebase;

    private String uid;

    private LocationListener locationCallback = new LocationListener()
    {

        @Override
        public void onLocationChanged (Location location)
        {
            updateLocation(location.getLatitude(), location.getLongitude());
            updateFirebaseLocation(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged (String provider, int status, Bundle extras)
        {

        }

        @Override
        public void onProviderEnabled (String provider)
        {

        }

        @Override
        public void onProviderDisabled (String provider)
        {

        }
    };

    private ChildEventListener childLocationCalback = new ChildEventListener()
    {

        @Override
        public void onChildAdded (DataSnapshot dataSnapshot, String s)
        {

        }

        @Override
        public void onChildChanged (DataSnapshot dataSnapshot, String s)
        {
            HashMap values = (HashMap) dataSnapshot.getValue();
            String g = (String) values.get("g");
            ArrayList l = (ArrayList) values.get("l");
            addMarker(g, Double.parseDouble(l.get(0).toString()),
                      Double.parseDouble(l.get(1).toString()));
        }

        @Override
        public void onChildRemoved (DataSnapshot dataSnapshot)
        {

        }

        @Override
        public void onChildMoved (DataSnapshot dataSnapshot, String s)
        {

        }

        @Override
        public void onCancelled (FirebaseError firebaseError)
        {

        }
    };

    private void addMarker (String g, double latitude, double longitude)
    {
        LatLng latlng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latlng);
        mMap.addMarker(markerOptions);
    }

    private void updateFirebaseLocation (double latitude, double longitude)
    {
        GeoFire geoFire = new GeoFire(firebase.child("locations"));
        GeoLocation geoLocation = new GeoLocation(latitude, longitude);
        geoFire.setLocation(uid, geoLocation);
    }

    private void updateLocation (double latitude, double longitude)
    {
        LatLng latlng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latlng);
        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
    }

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://demomapas.firebaseio.com/");

        uid = getIntent().getExtras().getString("uid");

        showUsers();
    }

    private void showUsers ()
    {
        firebase.child("locations").addChildEventListener(childLocationCalback);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        if (item.getItemId() == R.id.logout_option)
        {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout ()
    {
        firebase.unauth();
        finish();
        LoginActivity.start(this);
    }

    @Override
    public void onMapReady (GoogleMap googleMap)
    {
        mMap = googleMap;
        locationmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationmanager
            .requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 50, locationCallback);
    }

    public static void start (Context context, String uid)
    {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }
}
