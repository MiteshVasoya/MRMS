package com.mit.loginandregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyService extends Service {

    PendingIntent pintent;
    AlarmManager alarm;
    InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	JSONArray teamdata = null;
	String Hqid,assetid,start="00:00",end="00:00";
	int count,hh,mm,cnt=0;
	String START[],END[];
	
	   @Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
	      // Let it continue running until it is stopped.
	      //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
		   
		   new GetDay().execute();
		   SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
	       start=pref.getString("start", "00:00");
	       end=pref.getString("end","00:00");
	       if(!(start.equals("00:00") && end.equals("00:00")))
	       {
	    	   
	       
	       
	       Date todayDate = new Date();
	       
	       hh=todayDate.getHours();
	       mm=todayDate.getMinutes();
	       START=start.split(":");
	       END=end.split(":");
	       
	       Log.d("","");
	       
	       if( Integer.parseInt(START[0]) > Integer.parseInt(END[0]) )
		   {
			cnt=1;
		   }
	       if( Integer.parseInt(START[0]) == Integer.parseInt(END[0]) )
		   {
	    	   Log.d("Integer.parseInt(START[0]) == Integer.parseInt(END[0])",Integer.parseInt(START[0])+"=="+Integer.parseInt(END[0]));
			if((mm>=Integer.parseInt(START[1])) && (mm<=Integer.parseInt(END[1])))
			{
				new update_customerDetails().execute();   
			}
		   }
	       
	       if(((hh>Integer.parseInt(START[0])) && (hh<Integer.parseInt(END[0]))) || (cnt==1))
		   {
			   if(hh==Integer.parseInt(START[0]))
			   {
				   Log.d("hh==Integer.parseInt(START[0])",hh+"=="+Integer.parseInt(START[0]));
				   if(mm>=Integer.parseInt(START[1]))
				   {
					   new update_customerDetails().execute();   
				   }
			   }
			   else if(hh==Integer.parseInt(END[0]))
			   {
				   Log.d("hh==Integer.parseInt(END[0])",hh+"=="+Integer.parseInt(END[0]));
				   if((mm<=Integer.parseInt(END[1])))
				   {
					   new update_customerDetails().execute();
				   }
			   }   
			   else
			   {
				   Log.d("final else","final else");
				   new update_customerDetails().execute();
			   }
		   }
		   
		   
	       }
		   
		   Log.d("Serive :","Started");
		   
		   if(pintent==null)
		      {
		      Intent intent1 = new Intent(this, MyService.class);
		      pintent = PendingIntent.getService(this, 0, intent1, 0);

		      alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		      // Start every 30 seconds
		      alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+2000, 60000, pintent);
		      }
		      
		      
		      return super.onStartCommand(intent, flags, startId);
	   }
	   
	   @Override
	   public void onDestroy() {
	      super.onDestroy();
	      
	      alarm.cancel(pintent);
	      pintent=null;
	      
	      Toast.makeText(this, "Service is Stopped..", Toast.LENGTH_LONG).show();
	   }

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
    public class update_customerDetails extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	        
	        double latitude = location.getLatitude();
	        double longitude = location.getLongitude();
	        double altitude=location.getAltitude();
	        float speed=location.getSpeed();
	        float accuracy=location.getAccuracy();
	        
	        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
	        Hqid=pref.getString("myHqid", null);
	        assetid=pref.getString("assetid", null);
	        
				 String url = "http://manpowermgmt.igexsolutions.com/myservices/mylocation.php?"+"AssetId="+assetid
                         +"&Latitude="+latitude
                         +"&Longitude="+longitude
                         +"&Altitude="+altitude
                         +"&Accuracy="+accuracy
                         +"&Altitude_Accuracy="+"23"  // static value
                         +"&Heading="+"23"            // static value
                         +"&Speed="+speed
                         +"&hqid="+Hqid
                         +"&bettorylevel="+String.valueOf((int)getBatteryLevel())
                         +"&mode="+"insert";
				 
				 Log.d("Insert URL",url.replace(" ", "%20"));

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
				        
				        JSONObject root;
						try {
							root = new JSONObject(json);
							Log.d("tag", root.getString("tag"));
							
							if(root.getString("tag")!="0")
							{
								count=0;
								
								Log.d("Toast", "Insert sucess");
							}
							else
							{
								count=1;
								
								Log.d("Toast", "Error in Inserting..!!");
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        			
			return null;
		}
		
		@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
			
    		super.onPreExecute();
    	}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Log.d("count",count+"");
			/*if(count==1)
			{
				Toast.makeText(MyService.this, "Insert successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==0)
			{
				Toast.makeText(MyService.this, "Error in Inserting..!!", Toast.LENGTH_SHORT).show();
			}*/		
		}
    }
    
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f; 
    }
    
    public class GetDay extends AsyncTask<Void, Void, Void>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
			Date d = new Date();
			String dayOfTheWeek = sdf.format(d);
			Log.d("dayOfTheWeek"," "+dayOfTheWeek);
			
	        String url = "http://manpowermgmt.igexsolutions.com/myservices/tracking_time.php?day="+dayOfTheWeek;
			
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
				teamdata = root.getJSONArray("teamdata");
				Log.d("teamdata", teamdata.toString());
				
				// looping through All Contacts
				for(int i = 0; i < teamdata.length(); i++){
					JSONObject c = teamdata.getJSONObject(i);
					
					Log.d("c", c.toString());
					// Storing each json item in variable
					start = c.getString("Start_time");
					end = c.getString("End_time");
					
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
	        
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			Editor editor = pref.edit();
			
			editor.putString("start", start);
			editor.putString("end", end);
			
			editor.commit();
		
	        
		}
    	
    }
}