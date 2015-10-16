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

import com.mit.loginandregister.R;
import com.mit.loginandregister.MyVisit.loadList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MyPlaces extends Activity {
	
	ImageView back_to_homepage;
	JSONArray places = null;
    InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	ListView ls;
	LinearLayout addNewPlace;
	protected ProgressDialog progressDialog;
	String newPID;
	
	// Hashmap for ListView
	ArrayList<String> placeList;
	ArrayList<String> placeIDList;
	
	@Override
    protected void onResume() {

       super.onResume();
       
       new loadList().execute();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_places);
    
        ls=(ListView)findViewById(android.R.id.list);
        
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

        addNewPlace=(LinearLayout)findViewById(R.id.CentarPart);
        addNewPlace.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), PlacesSingleListItem.class);
				
				i.putExtra("Add", "Add");
				i.putExtra("newPID", newPID);
				startActivity(i);
			}
		});
        progressDialog = ProgressDialog.show(MyPlaces.this, "", "Loading..!!", true);
        new loadList().execute();
        
        
		
    }
    
    public class loadList extends AsyncTask<Void, Void, Void>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			placeList = new ArrayList<String>();
			placeIDList = new ArrayList<String>();
	        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			
	        String myHq_id=pref.getString("myHqid", null);
	        Log.d("myHq_id :", myHq_id);
	        
	        String url = "http://manpowermgmt.igexsolutions.com/myservices/getmyplaceslist.php?hq_id="+myHq_id;
			

			 try {
					DefaultHttpClient hc=new DefaultHttpClient();
					HttpPost pi=new HttpPost(url);
					
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
				places = root.getJSONArray("placedata");
				Log.d("placedata", places.toString());
				
				// looping through All Contacts
				for(int i = 0; i < places.length(); i++){
					JSONObject p = places.getJSONObject(i);
					
					Log.d("c", p.toString());
					// Storing each json item in variable
					
					String Pname = p.getString("PName");
					String Pid = p.getString("PlaceId");
					
					/*// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					//map.put("CustId", Cid);
					map.put("Cname", Cname);
					*/

					// adding HashList to ArrayList
					placeList.add(Pname);
					placeIDList.add(Pid);
					newPID=Pid;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Log.d("Places List", placeList.toString());
				
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d("Final Places List", placeList.toString());
	        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MyPlaces.this,android.R.layout.simple_list_item_1,placeList);
	        ls.setAdapter(adapter);
	        
	        ls.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
	    			try {
	    				// Getting Array of Contacts
	    				JSONObject root=new JSONObject(json);
	    				places = root.getJSONArray("placedata");
	    				Log.d("places", places.toString());
	    				
	    				// looping through All Contacts
	    				
	    					JSONObject c = places.getJSONObject(position);
	    					
	    					Log.d("c", c.toString());
	    					// Storing each json item in variable
	    					String Pname = c.getString("PName");
	    			        String Pid = c.getString("PlaceId");
	    			        String Pcode = c.getString("PlaceCode");
	    			        String Hqid = c.getString("HqId");
	    			        String Ptype = c.getString("Ptype");
	    			        String ContactPerson = c.getString("ContactPerson");
	    			        String tel = c.getString("TelNo");
	    			        String addDT = c.getString("AddDateTime");
	    			        String delDT = c.getString("DelDatetime");
	    			        String lat = c.getString("Lat");
	    			        String lon = c.getString("Lon");
	    			        String image = c.getString("PictureMemo");
	    			        String memo = c.getString("TextMemo");
	    			        String email = c.getString("Email");
	    			        String address = c.getString("PhysicalAddress");
	    					
	    					
	    					Intent in = new Intent(getApplicationContext(), PlacesSingleListItem.class);
	    					in.putExtra("Pname", Pname);
	    					in.putExtra("Pid", Pid);
	    					in.putExtra("Pcode", Pcode);
	    					in.putExtra("Hqid", Hqid);
	    					in.putExtra("Ptype", Ptype);
	    					in.putExtra("contactPerson", ContactPerson);
	    					in.putExtra("tel", tel);
	    					in.putExtra("address", address);
	    					in.putExtra("addDT", addDT);
	    					in.putExtra("delDT", delDT);
	    					in.putExtra("lat", lat);
	    					in.putExtra("lon", lon);
	    					in.putExtra("image", image);
	    					in.putExtra("memo", memo);
	    					in.putExtra("email", email);
	    					in.putExtra("Add", "Update");
	    					
	    					startActivity(in);
	    					
	    					Log.d("Pname", Pname);
	    				
	    			} catch (JSONException e) {
	    				e.printStackTrace();
	    			}
	    			
	    			
				}
			});
	        
	        progressDialog.dismiss();
	        
		}
    	
    }

}
