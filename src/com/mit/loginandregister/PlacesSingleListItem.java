package com.mit.loginandregister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.mit.loginandregister.R;


public class PlacesSingleListItem extends Activity {
	Button update,cancel,addimg,addtxtmemo;
	ImageView back_to_homepage;
	String memo,a_email,a_mobile;
	InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	EditText name,code,hqid,type,Address,CPname,Tel,Email,id,cp_name;
	int count;
	protected ProgressDialog progressDialog;
	String Pid,Hqid,newPID;
	int addcount=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_single_list_item);
        
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Hqid=pref.getString("myHqid", null);
        Log.d("Hqid :", Hqid);
        
        
         name = (EditText) findViewById(R.id.place_name);
         code = (EditText) findViewById(R.id.code);
         type = (EditText) findViewById(R.id.type);
         Address = (EditText) findViewById(R.id.add);
         Tel = (EditText) findViewById(R.id.tel);
         cp_name = (EditText) findViewById(R.id.cp_name);
         Email = (EditText) findViewById(R.id.email);
        
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        Intent i1 = getIntent();
        Intent in = getIntent();
        
        if(i1.getStringExtra("Add").equals("Add"))
        {
        	update=(Button)findViewById(R.id.update);
        	update.setText("Add");
        	addcount=1;
        }
        else
        {
        	Log.d("Checking..", "Work");
        // Get JSON values from previous intent
        String Pname = in.getStringExtra("Pname");
        Pid = in.getStringExtra("Pid");
        Log.d("Place ID", Pid);
        String Pcode = in.getStringExtra("Pcode");
        Hqid = in.getStringExtra("Hqid");
        String Ptype = in.getStringExtra("Ptype");
        String ContactPerson = in.getStringExtra("contactPerson");
        String tel = in.getStringExtra("tel");
        String addDT = in.getStringExtra("addDT");
        String delDT = in.getStringExtra("delDT");
        String lat = in.getStringExtra("lat");
        String lon = in.getStringExtra("lon");
        String image = in.getStringExtra("image");
        memo = in.getStringExtra("memo");
        String email = in.getStringExtra("email");
        String address = in.getStringExtra("address");
        // Displaying all values on the screen
        
        
         
        name.setText(Pname);
        //id.setText(Pid);
        code.setText(Pcode);
        //hqid.setText(Hqid);
        type.setText(Ptype);
        cp_name.setText(ContactPerson);
        Tel.setText(tel);
        Email.setText(email);
        Address.setText(address);
        
        }
        
        addimg=(Button)findViewById(R.id.addImage);
        addimg.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(), place_image.class);
				
				Intent geti=getIntent();
				newPID=geti.getStringExtra("newPID");
				Log.d("newCID", newPID+"..");
				
				if(addcount==1)
				{
				Log.d("Click..", "Add new");
				i.putExtra("Add", "Add");
				}
				else
				{
					i.putExtra("Add", "No");
					i.putExtra("Pid", Pid);
				}
				i.putExtra("newCID", newPID);
				i.putExtra("Hqid", Hqid);
    			startActivity(i);
			}
		});
        
        addtxtmemo=(Button)findViewById(R.id.addMemo);
        addtxtmemo.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent imemo = new Intent(getApplicationContext(), place_textmemo.class);    			
				imemo.putExtra("memo1", memo);
				imemo.putExtra("Hqid", Hqid);
				imemo.putExtra("newPID", newPID);
				
				if(addcount==1)
				{
					imemo.putExtra("Add", "Add");
				}
				else
				{
					imemo.putExtra("Add", "No");
					imemo.putExtra("Pid", Pid);
				}
				
				startActivity(imemo);
			}
		});

        
        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
		        cp_name.setText("");
		        Tel.setText("");
		        Email.setText("");
		        Address.setText("");
		        
			}
		});
        
    }
    
    
    public class check_update extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String provider = locationManager.getBestProvider(criteria, true);
	        Location location = locationManager.getLastKnownLocation(provider);
	        
	        double latitude = location.getLatitude();
	        double longitude = location.getLongitude();
	        
			Intent i1=getIntent();
			if(i1.getStringExtra("Add").equals("Add"))
	        {
				
				 String url = "http://manpowermgmt.igexsolutions.com/myservices/myplaces.php?PlaceId="+Pid
						 											 +"&place_code="+code.getText().toString()
                                                                     +"&place_name="+name.getText().toString()
                                                                     +"&place_type="+type.getText().toString()
                                                                     +"&hq_id="+Hqid
                                                                     +"&address="+Address.getText().toString()
                                                                     +"&cp_name="+cp_name.getText().toString()
                                                                     +"&tel_no="+Tel.getText().toString()
                                                                     +"&email="+Email.getText().toString()
                                                                     +"&mode=insert"
                                                                     +"&lat="+latitude+"&lon="+longitude;
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
			 String url = "http://manpowermgmt.igexsolutions.com/myservices/myplaces.php?PlaceId="+Pid
																	 +"&place_code="+code.getText().toString()
												                     +"&place_name="+name.getText().toString()
												                     +"&place_type="+type.getText().toString()
												                     +"&hq_id="+Hqid
												                     +"&address="+Address.getText().toString()
                                                                     +"&cp_name="+cp_name.getText().toString()
                                                                     +"&tel_no="+Tel.getText().toString()
                                                                     +"&email="+Email.getText().toString()
                                                                     +"&mode=update"
                                                                     +"&lat="+latitude+"&lon="+longitude;
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
			progressDialog = ProgressDialog.show(PlacesSingleListItem.this, "", "Processing..!!", true);
    		super.onPreExecute();
    		
    	}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			progressDialog.dismiss();
			
			Log.d("count",count+"");
			if(count==1)
			{
				Toast.makeText(PlacesSingleListItem.this, "Update successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==0)
			{
				Toast.makeText(PlacesSingleListItem.this, "Error in updating..!!", Toast.LENGTH_SHORT).show();
			}
			else if(count==2)
			{
				Toast.makeText(PlacesSingleListItem.this, "Insert successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==3)
			{
				Toast.makeText(PlacesSingleListItem.this, "Error in Inserting..!!", Toast.LENGTH_SHORT).show();
			}
			
		}
    }
    
}
