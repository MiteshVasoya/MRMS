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

public class MyCustomer extends Activity {

	ImageView back_to_homepage;
	JSONArray customers = null;
    InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	ListView ls;
	LinearLayout addNewCust;
	protected ProgressDialog progressDialog;
	String Hqid,newCID;
	
	// Hashmap for ListView
	ArrayList<String> customerList = new ArrayList<String>();

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_customer);
    
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        Hqid=pref.getString("myHqid", null);
        
        ls=(ListView)findViewById(android.R.id.list);
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        addNewCust=(LinearLayout)findViewById(R.id.CentarPart);
        addNewCust.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), CustomerSingleListItem.class);
				
				i.putExtra("Add", "Add");
				i.putExtra("newCID", newCID);
				i.putExtra("Hqid", Hqid);
				Log.d("Hqid :", Hqid+"..!");
				startActivity(i);
				
			}
		});
        progressDialog = ProgressDialog.show(MyCustomer.this, "", "Loading..!!", true);
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
			
	        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			
	        String myHq_id=pref.getString("myHqid", null);
	        Log.d("myHq_id :", myHq_id);
	        
	        String url = "http://manpowermgmt.igexsolutions.com/myservices/getcustomerlist.php?hq_id="+myHq_id;
			

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
				customers = root.getJSONArray("custdata");
				Log.d("custdata", customers.toString());
				
				// looping through All Contacts
				for(int i = 0; i < customers.length(); i++){
					JSONObject c = customers.getJSONObject(i);
					
					Log.d("c", c.toString());
					// Storing each json item in variable
					String Cid = c.getString("CustId");
					String Cname = c.getString("Cname");
					
					
					/*// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					//map.put("CustId", Cid);
					map.put("Cname", Cname);
					*/

					// adding HashList to ArrayList
					newCID=Cid;
					customerList.add(Cname);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Log.d("Customer List", customerList.toString());
			Log.d("newCID", newCID+"..");
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d("Final Customer List", customerList.toString());
	        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MyCustomer.this,android.R.layout.simple_list_item_1,customerList);
	        ls.setAdapter(adapter);
	        
	        ls.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					
	    			try {
	    				// Getting Array of Contacts
	    				JSONObject root=new JSONObject(json);
	    				customers = root.getJSONArray("custdata");
	    				Log.d("custdata", customers.toString());
	    				
	    				// looping through All Contacts
	    				
	    					JSONObject c = customers.getJSONObject(position);
	    					
	    					Log.d("c", c.toString());
	    					// Storing each json item in variable
	    					String Cid = c.getString("CustId");
	    					String Cname = c.getString("Cname");
	    					String Ccode = c.getString("CustCode");
	    					Hqid = c.getString("HqId");
	    					String Ctype = c.getString("Ctype");
	    					String add1 = c.getString("Add1");
	    					String add2 = c.getString("Add2");
	    					String add3 = c.getString("Add3");
	    					String city = c.getString("City");
	    					String state = c.getString("State");
	    					String pincode = c.getString("Pincode");
	    					String tel = c.getString("Tel_R");
	    					String mobile = c.getString("Mobile_No");
	    					String addDT = c.getString("AddDateTime");
	    					String delDT = c.getString("DelDateTime");
	    					String lat = c.getString("Lat");
	    					String lon = c.getString("Lon");
	    					String image = c.getString("Image");
	    					String memo = c.getString("Memo");
	    					String a_email = c.getString("alert_email");
	    					String a_mobile = c.getString("alert_mobile");
	    					String Bus_Stop = c.getString("Nearest_Bus_Stop");
	    					String Railway_station = c.getString("Nearest_Railway_Station");
	    					String Lmark = c.getString("Land_Mark");
	    					
	    					
	    					Intent in = new Intent(getApplicationContext(), CustomerSingleListItem.class);
	    					in.putExtra("Cname", Cname);
	    					in.putExtra("Cid", Cid);
	    					in.putExtra("Ccode", Ccode);
	    					in.putExtra("Hqid", Hqid);
	    					in.putExtra("Ctype", Ctype);
	    					in.putExtra("add1", add1);
	    					in.putExtra("add2", add2);
	    					in.putExtra("add3", add3);
	    					in.putExtra("city", city);
	    					in.putExtra("state", state);
	    					in.putExtra("pincode", pincode);
	    					in.putExtra("tel", tel);
	    					in.putExtra("mobile", mobile);
	    					in.putExtra("addDT", addDT);
	    					in.putExtra("delDT", delDT);
	    					in.putExtra("lat", lat);
	    					in.putExtra("lon", lon);
	    					in.putExtra("image", image);
	    					in.putExtra("memo", memo);
	    					in.putExtra("a_email", a_email);
	    					in.putExtra("a_mobile", a_mobile);
	    					in.putExtra("Bus_stop", Bus_Stop);
	    					in.putExtra("Railway_station", Railway_station);
	    					in.putExtra("Lmark", Lmark);
	    					in.putExtra("Add", "Update");
	    					
	    					startActivity(in);
	    					
	    					Log.d("Cid", Cid);
	    					Log.d("Cname", Cname);
	    				
	    			} catch (JSONException e) {
	    				e.printStackTrace();
	    			}
	    			
	    			
				}
			});
	        
	        progressDialog.dismiss();
		}
    	
    }
    
    @Override
    protected void onResume() {

       super.onResume();
       
       new loadList().execute();
    }
}
