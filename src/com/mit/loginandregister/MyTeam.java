package com.mit.loginandregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mit.loginandregister.R;
import com.mit.loginandregister.MyVisit.loadList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class MyTeam extends FragmentActivity {
	ImageView back_to_homepage;
	private GoogleMap mMap;
	LatLng myPosition;
	JSONArray teams = null;
    InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	protected ProgressDialog progressDialog;
	LatLngBounds.Builder latlngbounds;
	ArrayList<String> TeamLatList = new ArrayList<String>();
	ArrayList<String> TeamLonList = new ArrayList<String>();
	ArrayList<String> EnameList = new ArrayList<String>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_team);
    
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        progressDialog = ProgressDialog.show(MyTeam.this, "", "Loading..!!", true);
        new loadList().execute();
        
        
    }
    
    public class loadList extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
	        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			
	        String myHq_id=pref.getString("myHqid", null);
	        Log.d("myHq_id :", myHq_id);
	        
	        String url = "http://manpowermgmt.igexsolutions.com/ws/myteam.php?hqid="+myHq_id;
			

			 try {
					DefaultHttpClient hc=new DefaultHttpClient();
					HttpPost pi=new HttpPost(url.replace(" ", "%20"));
					
						HttpResponse hr=hc.execute(pi);
						
						HttpEntity he=hr.getEntity();
						is=he.getContent();
						
					} catch (ClientProtocolException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        
			        try {
			            BufferedReader reader = new BufferedReader(new InputStreamReader(
			                    is, "iso-8859-1"), 8);
			            StringBuilder sb = new StringBuilder();
			            String line = null;
			            while ((line = reader.readLine()) != null) {
			                sb.append(line + "\n");
			            }
			            
			            
			            is.close();
			            Log.d("sb", sb.toString());
			            json=sb.toString();
			            
			            
			        } catch (Exception e) {
			            Log.e("Buffer Error", "Error converting result " + e.toString());
			        }

			try {
				// Getting Array of Contacts
				JSONObject root=new JSONObject(json);
				teams = root.getJSONArray("teamdata");
				Log.d("custdata", teams.toString());
				
				// looping through All Contacts
				for(int i = 0; i < teams.length(); i++){
					JSONObject c = teams.getJSONObject(i);
					
					Log.d("c", c.toString());
					// Storing each json item in variable
					String lat = c.getString("Latitude");
					String lon = c.getString("Longitude");
					String Ename = c.getString("Ename");
					
					TeamLatList.add(lat);
					TeamLonList.add(lon);
					EnameList.add(Ename);
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Log.d("Lat List", TeamLatList.toString());
			Log.d("Lon List", TeamLonList.toString());
			Log.d("Name List", EnameList.toString());
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			
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
	        //double latitude = location.getLatitude();

	        // Getting longitude of the current location
	        //double longitude = location.getLongitude();

	        // Creating a LatLng object for the current location

	        	latlngbounds = new LatLngBounds.Builder();

	            
	         for(int i=0;i<EnameList.size();i++)
	         {
	        	 double latitude = Double.parseDouble(TeamLatList.get(i));
	        	 double longitude = Double.parseDouble(TeamLonList.get(i));
	        	 
	 	        LatLng latLng = new LatLng(latitude, longitude);

	 		    //     myPosition = new LatLng(latitude, longitude);
	 		        
	 		    /*    CameraUpdate center=
	 		                CameraUpdateFactory.newLatLng(new LatLng(latitude,
	 		                                                         longitude));
	 		       mMap.moveCamera(center);    
	 		       CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);
		            mMap.animateCamera(zoom);*/
		            mMap.getUiSettings().setCompassEnabled(true);
		        
		           latlngbounds.include(latLng);
	        	 
	         Marker ciu = mMap.addMarker(new MarkerOptions()
	         		.position(latLng).title(EnameList.get(i))
	         		.icon(BitmapDescriptorFactory.fromResource(R.drawable.simple_marker)));
	         		
	         ciu.setVisible(true);
	         }
	         CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(latlngbounds.build(), 60);
	         mMap.moveCamera(cameraUpdate);
	         
	         mMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
				
					return false;
				}
			});
	         

	         
	        }

			
			
			progressDialog.dismiss();
		}
    	
    }

}
