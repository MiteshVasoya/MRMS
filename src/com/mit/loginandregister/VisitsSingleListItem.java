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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class VisitsSingleListItem extends Activity {
ImageView back_to_homepage;
protected ProgressDialog progressDialog;	
InputStream is;
static JSONObject jObj = null;
static String json = "";
int count;
String Hqid,Cid,lat,lon,status,newCID;
EditText Memo;
Button save,addimg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textmemo_text);
    
        
        Memo=(EditText)findViewById(R.id.Memo);
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        Intent in = getIntent();
        String memo = in.getStringExtra("memo");
        status=in.getStringExtra("Add");
        Hqid=in.getStringExtra("Hqid");
        Cid=in.getStringExtra("Cid");
        lat=in.getStringExtra("lat");
        lon=in.getStringExtra("lon");
        Memo = (EditText) findViewById(R.id.Memo);
        save=(Button)findViewById(R.id.save);
        
        	Memo.setText(memo);
        
        	addimg=(Button)findViewById(R.id.addimg);
            addimg.setVisibility(View.VISIBLE);
            addimg.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				Intent i = new Intent(getApplicationContext(), TextMemo_Image.class);
    				i.putExtra("Visit", "Visit");
    				i.putExtra("Cid", Cid);
    				
    				
    				startActivity(i);
    			}
    		});
            
        save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
					//progressDialog = ProgressDialog.show(context, "", "", true);
					new check_update().execute();	
				
			}
		});
    }
    
    
    public class check_update extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
				
				Log.d("Hqid :", Hqid+"!");
				
				String url = "http://manpowermgmt.igexsolutions.com/myservices/visitentry.php?mode=insert"
	                     +"&HqId="+Hqid
	                     +"&CustId="+Cid
	                     +"&TextMemo="+Memo.getText().toString()
	                     +"&Lat="+lat
	                     +"&Lon="+lon;
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
	      			
			return null;
		}
		
		@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		progressDialog = ProgressDialog.show(VisitsSingleListItem.this, "", "Loading..!!", true);
    		
    	}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			Log.d("count",count+"");
			if(count==2)
			{
				Toast.makeText(VisitsSingleListItem.this, "Insert successful..", Toast.LENGTH_SHORT).show();
			}
			else if(count==3)
			{
				Toast.makeText(VisitsSingleListItem.this, "Error in Inserting..!!", Toast.LENGTH_SHORT).show();
			}
			
		}
		
    }

}
