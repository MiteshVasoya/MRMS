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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	Context context;
	int count=0;
	Button submit;
	EditText LoginID,Password;
	private static String url;
	String id_str,passwd_str;
    String id,passwd,joindate,ename,level,grade,add1,add2,add3,city,state,pincode,tel_r,mobile,email,assetid,status,m_imeino,Hqid,myHqid;
    InputStream is;
	static JSONObject jObj = null;
	static String json = "";
	protected ProgressDialog progressDialog;
	
	private static final String TAG_empdata = "empdata";
	private static final String TAG_EmpId = "EmpId";
	private static final String TAG_password = "password";

	
	//JSONArray empdata = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
       /* 
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
//				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
//				startActivity(i);
			}
		});
    */
        
        LoginID=(EditText)findViewById(R.id.editText1);
        Password=(EditText)findViewById(R.id.editText2);

        
    submit=(Button)findViewById(R.id.login);
    
    submit.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			progressDialog = ProgressDialog.show(LoginActivity.this, "", "", true);
			new authenticate().execute();
			
		}
	});
    
    
    }
    
    public class authenticate extends AsyncTask<Void, Void, Void>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		progressDialog.setMessage("Please wait...");
            progressDialog.show();
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
	        id_str=LoginID.getText().toString();
	        passwd_str=Password.getText().toString();

	        
	        url = "http://manpowermgmt.igexsolutions.com/myservices/login.php?empid="+id_str+"&password="+passwd_str;
	        
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
				JSONObject root=new JSONObject(json);
				
				Log.d("empdata", root.getString("empdata"));
				
				if(root.getString("empdata").equals("error"))
				{
					count=1;
					
				}
				else
				{
					count=0;
				JSONObject json2=root.getJSONObject("empdata");
				
				id = json2.getString(TAG_EmpId);
				passwd = json2.getString(TAG_password);
				joindate=json2.getString("JoinDate");
				ename=json2.getString("Ename");
				level=json2.getString("Level");
				grade=json2.getString("Grade");
				add1=json2.getString("Add1");
				add2=json2.getString("Add2");
				add3=json2.getString("Add3");
				city=json2.getString("City");
				state=json2.getString("State");
				pincode=json2.getString("Pincode");
				tel_r=json2.getString("Tel_R");
				mobile=json2.getString("Mobile_No");
				email=json2.getString("Email");
				assetid=json2.getString("AssetId");
				status=json2.getString("Status");
				m_imeino=json2.getString("M_IMEINo");
				Hqid=json2.getString("HqId");
				myHqid=json2.getString("MyHqId");
				
				
				Log.d("id", id);Log.d("passwd", passwd);
				Log.d("Lid", id_str);Log.d("Lpasswd", passwd_str);
				
			
				createLoginSession(id_str, passwd_str,joindate,ename,level,grade,add1,add2,add3,city,state,pincode,tel_r,mobile,email,assetid,status,m_imeino,Hqid,myHqid);
				
				Intent i1 = new Intent(getApplicationContext(), HomepageActivity.class);
				startActivity(i1);
				}
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("JSON Parser", "Error parsing data " + e.toString());
				e.printStackTrace();
				
			}
			return null;
	        
	    		
		
    }
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("count",count+"");
			if(count==1)
			{
				Toast.makeText(LoginActivity.this, "Invalid Login-ID or Password", Toast.LENGTH_SHORT).show();
			}
			
			progressDialog.dismiss();
		}
		
		public void createLoginSession(String id_str,String passwd_str,String joindate,String ename,String level,String grade,String add1,String add2,String add3,String city,String state,String pincode,String tel_r,String mobile,String email,String assetid,String status,String m_imeino,String Hqid,String myHqid){
			
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			Editor editor = pref.edit();
			
			editor.putString("id", id_str);
			editor.putString("passwd", passwd_str);
			editor.putString("joindate", joindate);
			editor.putString("ename", ename);
			editor.putString("level", level);
			editor.putString("grade", grade);
			editor.putString("add1", add1);
			editor.putString("add2", add2);
			editor.putString("add3", add3);
			editor.putString("city", city);
			editor.putString("state", state);
			editor.putString("pincode", pincode);
			editor.putString("tel_r", tel_r);
			editor.putString("mobile", mobile);
			editor.putString("email", email);
			editor.putString("assetid", assetid);
			editor.putString("status", status);
			editor.putString("m_imeino", m_imeino);
			editor.putString("Hqid", Hqid);
			editor.putString("myHqid", myHqid);
			editor.commit();
			
			/*String id_new=pref.getString(id, null);
			String passwd_new=pref.getString(passwd, null);
			String jd=pref.getString(joindate, null);
			
			Log.d("ID", id_new);
			Log.d("Password", passwd_new);
			Log.d("JoinDate", jd);*/
		}
			
		
		
		}
   
}