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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mit.loginandregister.R;

public class CustomerSingleListItem extends Activity {

	Context context;
	Button update,cancel,addimg,addtxtmemo,alertstng;
	JSONArray states = null;
	JSONArray cities = null;
	ImageView back_to_homepage;
	String memo,a_email,a_mobile;
	InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	EditText name,code,hqid,type,ad1,ad2,ad3,Pincode,Tel,Mobile,Bus_Stop,Rail_Station,LMark;
	String Cid,city,state;
	int count;
	protected ProgressDialog progressDialog;
	Spinner City,State;
	int statePos,stateChange=1,cityChange=1;
	ArrayList<String> stateList;
	ArrayList<String> cityList;
	ArrayList<String> stateIDList;
	ArrayList<String> cityIDList;
	String Hqid,newCID;
	int addcount=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_single_list_item);
        stateList = new ArrayList<String>();
		stateIDList = new ArrayList<String>();
		
		
		
         name = (EditText) findViewById(R.id.name_label);
         code = (EditText) findViewById(R.id.code);
         type = (EditText) findViewById(R.id.type);
         ad1 = (EditText) findViewById(R.id.add1);
         ad2 = (EditText) findViewById(R.id.add2);
         ad3 = (EditText) findViewById(R.id.add3);
         City = (Spinner) findViewById(R.id.city);
         State = (Spinner) findViewById(R.id.state);
         Pincode = (EditText) findViewById(R.id.pincode);
         Tel = (EditText) findViewById(R.id.tel);
         Mobile = (EditText) findViewById(R.id.mobile);
         Bus_Stop = (EditText) findViewById(R.id.bus_stop);
         Rail_Station = (EditText) findViewById(R.id.railway_station);
         LMark = (EditText) findViewById(R.id.landmark);
        
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        Intent i1 = getIntent();
        Intent in = getIntent();
        Hqid = in.getStringExtra("Hqid");	
		Log.d("Hqid :", Hqid+"!");
        
        if(i1.getStringExtra("Add").equals("Add"))
        {
        	update=(Button)findViewById(R.id.update);
        	update.setText("Add");
        	stateChange=1;
        	addcount=1;
            new get_state().execute();
            cityChange=1;
        }
        else
        {
        // Get JSON values from previous intent
        	stateChange=0;
        	cityChange=0;
        String Cname = in.getStringExtra("Cname");
        Cid = in.getStringExtra("Cid");
        String Ccode = in.getStringExtra("Ccode");
        
        String Ctype = in.getStringExtra("Ctype");
        String add1 = in.getStringExtra("add1");
        String add2 = in.getStringExtra("add2");
        String add3 = in.getStringExtra("add3");
        city = in.getStringExtra("city");
        state = in.getStringExtra("state");
        String pincode = in.getStringExtra("pincode");
        String tel = in.getStringExtra("tel");
        String mobile = in.getStringExtra("mobile");
        String addDT = in.getStringExtra("addDT");
        String delDT = in.getStringExtra("delDT");
        String lat = in.getStringExtra("lat");
        String lon = in.getStringExtra("lon");
        String image = in.getStringExtra("image");
        memo = in.getStringExtra("memo");
        String Bus_stop = in.getStringExtra("Bus_stop");
        String Railway_station = in.getStringExtra("Railway_station");
        String Lmark = in.getStringExtra("Lmark");
        a_email = in.getStringExtra("a_email");
        a_mobile = in.getStringExtra("a_mobile");
        
        // Displaying all values on the screen
        
        new get_state().execute();
        name.setText(Cname);
        code.setText(Ccode);
        type.setText(Ctype);
        ad1.setText(add1);
        ad2.setText(add2);
        ad3.setText(add3);
        Pincode.setText(pincode);
        Tel.setText(tel);
        Mobile.setText(mobile);
        Bus_Stop.setText(Bus_stop);
        Rail_Station.setText(Railway_station);
        LMark.setText(Lmark);
        
        }
        addimg=(Button)findViewById(R.id.addImage);
        addimg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent geti=getIntent();
				newCID=geti.getStringExtra("newCID");
				Log.d("newCID", newCID+"..");
				Intent i = new Intent(getApplicationContext(), customer_image.class);
				if(addcount==1)
				{
				Log.d("Click..", "Add new");
				i.putExtra("Add", "Add");
				}
				else
				{
					i.putExtra("Add", "No");
					i.putExtra("Cid", Cid);
				}
				i.putExtra("newCID", newCID);
				i.putExtra("Hqid", Hqid);
    			startActivity(i);
			}
		});
        
        addtxtmemo=(Button)findViewById(R.id.addMemo);
        addtxtmemo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent imemo = new Intent(getApplicationContext(), customer_textmemo.class);
    			
				imemo.putExtra("memo1", memo);
					if(addcount==1)
					{
					imemo.putExtra("Add", "Add");
					imemo.putExtra("Hqid", Hqid);
					}
				startActivity(imemo);
			}
		});
        
        alertstng=(Button)findViewById(R.id.alert);
        alertstng.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("a_email", a_email+".");
				Log.d("a_mobile", a_mobile+".");
				Intent ialert = new Intent(getApplicationContext(), customer_alert.class);
				if(addcount!=1)
				{
				ialert.putExtra("a_email", a_email);
				ialert.putExtra("a_mobile", a_mobile);
				}
    			startActivity(ialert);
    			
			}
		});
        
        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Log.d("Hqid :", Hqid+"!");
					//progressDialog = ProgressDialog.show(context, "", "", true);
					new check_update().execute();	
				
			}
		});
        
        cancel=(Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name.setText("");
		        code.setText("");
		        type.setText("");
		        ad1.setText("");
		        ad2.setText("");
		        ad3.setText("");
		        Pincode.setText("");
		        Tel.setText("");
		        Mobile.setText("");
		        Bus_Stop.setText("");
		        Rail_Station.setText("");
		        LMark.setText("");
		        City.setSelection(0);
		        
			}
		});
        
    }
    
    
    public class check_update extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			
			Intent i1=getIntent();
			if(i1.getStringExtra("Add").equals("Add"))
	        {
				
				Log.d("Hqid :", Hqid+"!");
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		        Criteria criteria = new Criteria();
		        String provider = locationManager.getBestProvider(criteria, true);
		        Location location = locationManager.getLastKnownLocation(provider);
		        
		        double latitude = location.getLatitude();
		        double longitude = location.getLongitude();
		        
				 String url = "http://manpowermgmt.igexsolutions.com/myservices/customer.php?cust_code="+code.getText().toString()+"&cname="+name.getText().toString()+"&cust_type="+type.getText().toString()+"&hq_id="+Hqid+"&add1="+ad1.getText().toString()+"&add2="+ad2.getText().toString()+"&add3="+ad3.getText().toString()+"&city_id="+cityIDList.get(City.getSelectedItemPosition()).toString()+"&state_id="+stateIDList.get(State.getSelectedItemPosition()).toString()+"&pincode="+Pincode.getText().toString()+"&tel_r="+Tel.getText().toString()+"&mob_no="+Mobile.getText().toString()+"&Lat="+latitude+"&Lon="+longitude+"&mode=insert";
				 Log.d("Insert URL",url);

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
				        
				        JSONObject root;
						try {
							root = new JSONObject(json);
							Log.d("tag", root.getString("tag"));
							
							if(root.getString("tag")!="0")
							{
								count=2;
								
								Log.d("Toast", "Insert sucess");
							}
							else
							{
								count=3;
								
								Log.d("Toast", "Error in Inserting..!!");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        }
			else
			{
			
			Log.d("code : ",code.getText().toString());
			Log.d("STATE", stateIDList.get(State.getSelectedItemPosition()).toString()+"");
	    	Log.d("CITY",cityIDList.get(City.getSelectedItemPosition()).toString()+"");
			 String url = "http://manpowermgmt.igexsolutions.com/myservices/customer.php?cust_code="+code.getText().toString()+"&cname="+name.getText().toString()+"&CustId="+Cid+"&cust_type="+type.getText().toString()+"&hq_id="+Hqid+"&add1="+ad1.getText().toString()+"&add2="+ad2.getText().toString()+"&add3="+ad3.getText().toString()+"&city_id="+cityIDList.get(City.getSelectedItemPosition()).toString()+"&state_id="+stateIDList.get(State.getSelectedItemPosition()).toString()+"&pincode="+Pincode.getText().toString()+"&tel_r="+Tel.getText().toString()+"&mob_no="+Mobile.getText().toString()+"&Lat=7&Lon=8&mode=update";
			 Log.d("Update URL",url);

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
			        
			        JSONObject root;
					try {
						root = new JSONObject(json);
						Log.d("tag", root.getString("tag"));
						
						if(root.getString("tag").equals("success"))
						{
							count=1;
							
							Log.d("Toast", "Update sucess");
						}
						else
						{
							count=0;
							
							Log.d("Toast", "Error in updating..!!");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			
			return null;
		}
		
		@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		progressDialog = ProgressDialog.show(CustomerSingleListItem.this, "", "Loading..!!", true);
    		
    	}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			Log.d("count",count+"");
			if(count==1)
			{
				Toast.makeText(CustomerSingleListItem.this, "Update successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==0)
			{
				Toast.makeText(CustomerSingleListItem.this, "Error in updating..!!", Toast.LENGTH_SHORT).show();
			}
			else if(count==2)
			{
				Toast.makeText(CustomerSingleListItem.this, "Insert successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==3)
			{
				Toast.makeText(CustomerSingleListItem.this, "Error in Inserting..!!", Toast.LENGTH_SHORT).show();
			}
			
		}
		
    }
    public class get_state extends AsyncTask<Void, Void, Void>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		progressDialog = ProgressDialog.show(CustomerSingleListItem.this, "", "Loading data..!!", true);
    		
    		super.onPreExecute();
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			
			String url = "http://manpowermgmt.igexsolutions.com/myservices/getstatelist.php";
			Log.d("State URL",url);

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
				states = root.getJSONArray("statedata");
				Log.d("custdata", states.toString());
				
				// looping through All Contacts
				for(int i = 0; i < states.length(); i++){
					JSONObject c = states.getJSONObject(i);
					
					Log.d("c", c.toString());
					// Storing each json item in variable
					String stateID = c.getString("iStateID");
					String countryID = c.getString("iCountryID");
					String stateName = c.getString("vStateName");
					String status = c.getString("eStatus");
					
					
					/*// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					//map.put("CustId", Cid);
					map.put("Cname", Cname);
					*/

					// adding HashList to ArrayList
					stateList.add(stateName);
					stateIDList.add(stateID);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Log.d("State List", stateList.toString());
			Log.d("State ID List", stateIDList.toString());
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
	        ArrayAdapter<String> adapter=new ArrayAdapter<String>(CustomerSingleListItem.this,android.R.layout.simple_spinner_item,stateList);
	        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    	State.setAdapter(adapter);
	    	
	    	State.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					Log.d("stateChange", stateChange+"");
					if(stateChange==0)
					{
						if(state.equals(""))
						{
							statePos=1;
							State.setSelection(statePos-1);
							Log.d("State Position",statePos+"");
						}
						else
						{
					Log.d("state",state);
					pos=stateIDList.indexOf(state);
					Log.d("STATE Position",pos+"");
					statePos=pos+1;
					State.setSelection(pos);
						}
					}
					else
					{
						
						statePos=Integer.parseInt(stateIDList.get(pos));	
						Log.d("statePos",statePos+"");
					}
					
					Log.d("cityChange",cityChange+"");
					
						new get_city().execute();
					
					stateChange=1;
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	
			super.onPostExecute(result);
		}
    }
    
    public class get_city extends AsyncTask<Void, Void, Void>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		cityList = new ArrayList<String>();
			
    	}
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			cityIDList=new ArrayList<String>();
			String url = "http://manpowermgmt.igexsolutions.com/myservices/getcitylist.php";
			Log.d("City URL",url);
			
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
				cities = root.getJSONArray("citydata");
				Log.d("citydata", states.toString());
				
				// looping through All Contacts
				for(int i = 0; i < cities.length(); i++){
					JSONObject c = cities.getJSONObject(i);
					
					Log.d("c", c.toString());
					// Storing each json item in variable
					String stateID = c.getString("iStateID");
					String cityID = c.getString("city_id");
					String cityName = c.getString("city_name");
					String status = c.getString("status");
					
					
					/*// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					// adding each child node to HashMap key => value
					//map.put("CustId", Cid);
					map.put("Cname", Cname);
					*/

					// adding HashList to ArrayList
					if(Integer.parseInt(stateID)==statePos)
					{cityList.add(cityName);
					 cityIDList.add(cityID);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			return null;
			
			
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			Log.d("City List", cityList.toString());
			Log.d("City ID List", cityIDList.toString());
			Log.d("cityChange", cityChange+"");
			
	        ArrayAdapter<String> adapter=new ArrayAdapter<String>(CustomerSingleListItem.this,android.R.layout.simple_spinner_item,cityList);
	        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
	    	City.setAdapter(adapter);
			
	    	City.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					
					if(cityChange==0)
					{
						if(city.equals(""))
						{
							City.setSelection(0);
							Log.d("City position..","0");
						}
						else
						{
							position=cityIDList.indexOf(city);
							Log.d("CITY position",position+"");
							City.setSelection(position);
						}
					}
					else
					{
						Log.d("City position..",position+"!");
						position=cityIDList.indexOf(city);
					}
					
					
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			
	    	
	    	progressDialog.dismiss();
	    	
	    	
	    	//Log.d("state..", stateIDList.get(State.getSelectedItemPosition()).toString()+"");
	    	//Log.d("city..",cityIDList.get(City.getSelectedItemPosition()).toString()+"");
			super.onPostExecute(result);
		}
    }
    



}
