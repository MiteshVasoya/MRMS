package com.mit.loginandregister;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.mit.loginandregister.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyLocation extends FragmentActivity {

	private GoogleMap mMap;
	LatLng myPosition;
	String msg="@";
	ImageView back_to_homepage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_location);	

        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
	/*	final LatLng CIU = new LatLng(23.0300, 72.5800);
		Marker ciu = mMap.addMarker(new MarkerOptions()
		                          .position(CIU).title("My Office"));
		ciu.setVisible(true);*/
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        double altitude=location.getAltitude();
        float speed=location.getSpeed();
        float accuracy=location.getAccuracy();
        
        
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

    //     myPosition = new LatLng(latitude, longitude);
        
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(latitude,
                                                         longitude));
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(17);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            mMap.getUiSettings().setCompassEnabled(true);
        
         Marker ciu = mMap.addMarker(new MarkerOptions()
         		.position(latLng).title("")
         		.snippet(msg)
         		.icon(BitmapDescriptorFactory.fromResource(R.drawable.simple_marker)));
      
         mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
			
				return false;
			}
		});
         
         /*   mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				// TODO Auto-generated method stub
				//msg.toUpperCase();
				return false;
			}
		});
         mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

             @SuppressWarnings("null")
			@Override
             public View getInfoWindow(Marker arg0) 
             {
                 Activity context = null;
				View vMapInfo = ((Activity) context).getLayoutInflater().inflate(R.layout.map_info_layout, null);
                 return vMapInfo;
             }

			@Override
			public View getInfoContents(Marker arg0) {
				// TODO Auto-generated method stub
				return null;
			}

         });*/
         ciu.setVisible(true);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
