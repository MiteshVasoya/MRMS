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

import com.mit.loginandregister.R;
import com.mit.loginandregister.place_textmemo.check_update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class customer_textmemo extends Activity {
	ImageView back_to_homepage;
	protected ProgressDialog progressDialog;	
	InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	int count;
	String Hqid,Cid,lat,lon,status,newCID;
	EditText Memo;
	Button save;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textmemo_text);
    
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        Intent in = getIntent();
        String memo = in.getStringExtra("memo1");
        
        EditText Memo = (EditText) findViewById(R.id.Memo);
        
        Memo.setText(memo);
        
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new check_update().execute();
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
				newCID=i1.getStringExtra("newCID");
				Log.d("New Customer ID", newCID);
				 String url = "http://manpowermgmt.igexsolutions.com/myservices/customer.php?mode="+"updatememo"
                                                                     +"&custid="+(Integer.parseInt(newCID)+1)
                                                                     +"&memo="+Memo.getText().toString().trim();
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
			
			 String url = "http://manpowermgmt.igexsolutions.com/myservices/visitentry.php?mode="+"updatememo"
                                                                      +"&custid="+Cid
                                                                      +"&memo="+Memo.getText().toString().trim();
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
    		progressDialog = ProgressDialog.show(customer_textmemo.this, "", "Loading..!!", true);
    		
    	}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			Log.d("count",count+"");
			if(count==1)
			{
				Toast.makeText(customer_textmemo.this, "Update successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==0)
			{
				Toast.makeText(customer_textmemo.this, "Error in updating..!!", Toast.LENGTH_SHORT).show();
			}
			else if(count==2)
			{
				Toast.makeText(customer_textmemo.this, "Insert successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==3)
			{
				Toast.makeText(customer_textmemo.this, "Error in Inserting..!!", Toast.LENGTH_SHORT).show();
			}
			
		}
		
    }


}
