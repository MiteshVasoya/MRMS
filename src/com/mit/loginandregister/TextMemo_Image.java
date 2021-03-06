 package com.mit.loginandregister;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mit.loginandregister.R;
import com.mit.loginandregister.customer_image.upload_file;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TextMemo_Image extends Activity {

	ImageView back_to_homepage;
	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
    String Cid,Hqid;
    Uri sourceFile;
    String upLoadServerUri,data;
	int column_index;
	protected ProgressDialog dialog;
	Button save;
	String operation="No";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.textmemo_image);
    
        Intent in=getIntent();
        Cid=in.getStringExtra("Cid");
        if(Cid!=null)
        {
        	operation=in.getStringExtra("Visit");
        }
        
        
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        Hqid=pref.getString("myHqid", null);
        
        back_to_homepage=(ImageView)findViewById(R.id.back_to_homepage);
        back_to_homepage.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        
        this.imageView = (ImageView)this.findViewById(R.id.Upload_image);
        
        imageView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
			}
		});
        
        save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog=ProgressDialog.show(TextMemo_Image.this, "", "Loading..!!", true);
				new upload_file().execute();
			}
		});
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
            sourceFile=data.getData();
        }  
    } 
    
    
    public class upload_file extends AsyncTask<Void, Void, Void>{

    	
	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		
		uploadFile(getPath(sourceFile, TextMemo_Image.this));
		
		return null;
	}
    }
    
    public String getPath(Uri uri, Activity activity) {
  	  String[] projection = { MediaColumns.DATA };
  	  Cursor cursor = activity
  	    .managedQuery(uri, projection, null, null, null);

  	  column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);

  	  cursor.moveToFirst();
  	  String galname = cursor.getColumnName(column_index);
  	  Log.i("gally fine name", "gally fine name " + galname);
  	  // Toast.makeText(AddImage.this, "gal "+galname,
  	  // Toast.LENGTH_LONG).show();

  	  return cursor.getString(column_index);

  	 }

    public int uploadFile(String sourceFileUri) 
    {
    	Log.d("operation",operation+".!");
    	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        
			if(operation.equals("Visit"))
	        {
				
				Log.d("Hqid :", Hqid+"!");
				
				upLoadServerUri = "http://manpowermgmt.igexsolutions.com/ws/visitentry.php?"
					      + "mode=insert"
					      + "&HqId="+Hqid
					      + "&CustId="+ Cid + "&TextMemo="
					      + "" + "&Lat="+latitude+"&Lon="+longitude;

					    Log.i("serverurl insertvisitentry", "serverurl visitentry"
					      + upLoadServerUri);
		       }
			else
			{
				  upLoadServerUri = "http://manpowermgmt.igexsolutions.com/ws/textpicturememo.php?"+ "mode="
						     + "insert" + "&memocode=" + "m002" + "&hqid="
						     + Hqid + "&memotype="
						     + "img" + "&lat="+latitude+ "&lon="+longitude;

						   Log.i("textpicturememo", "textpicturememo" + upLoadServerUri);

			}
    
			 Log.i("url", "url" + upLoadServerUri);
		        String fileName = sourceFileUri;
		        int serverResponseCode = 0;
		        HttpURLConnection conn = null;
		        DataOutputStream dos = null;
		        String lineEnd = "\r\n";
		        String twoHyphens = "--";
		        String boundary = "*****";
		        int bytesRead, bytesAvailable, bufferSize;
		        byte[] buffer;
		        int maxBufferSize = 1 * 1024 * 1024;
		        File sourceFile = new File(sourceFileUri);
		        if (!sourceFile.isFile()) {
		         Log.e("uploadFile", "Source File Does not exist");
		         dialog.dismiss();
		         return 0;
		        } else {
		         try { // open a URL connection to the Servlet
		          FileInputStream fileInputStream = new FileInputStream(
		            sourceFile);
		          URL url = new URL(upLoadServerUri);
		          conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
		                       // connection
		                       // to
		                       // the URL
		          conn.setDoInput(true); // Allow Inputs
		          conn.setDoOutput(true); // Allow Outputs
		          conn.setUseCaches(false); // Don't use a Cached Copy
		          conn.setRequestMethod("POST");
		          conn.setRequestProperty("Connection", "Keep-Alive");
		          conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		          conn.setRequestProperty("Content-Type",
		            "multipart/form-data;boundary=" + boundary);
		          conn.setRequestProperty("uploaded_file", fileName);

		          conn.setRequestProperty("charset", "utf-8");
		          Log.i("name", "name " + fileName);
		          dos = new DataOutputStream(conn.getOutputStream());

		          dos.writeBytes(twoHyphens + boundary + lineEnd);
		          dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
		            + fileName + "\"" + lineEnd);

		          Log.i("name",
		            "name "
		              + "Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
		              + fileName + "\"" + lineEnd);

		          dos.writeBytes(lineEnd);

		          bytesAvailable = fileInputStream.available(); // create a buffer
		                      // of
		                      // maximum size

		          bufferSize = Math.min(bytesAvailable, maxBufferSize);
		          buffer = new byte[bufferSize];

		          // read file and write it into form...
		          bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		          while (bytesRead > 0) {
		           dos.write(buffer, 0, bufferSize);
		           bytesAvailable = fileInputStream.available();
		           bufferSize = Math.min(bytesAvailable, maxBufferSize);
		           bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		          }

		          // send multipart form data necesssary after file data...
		          dos.writeBytes(lineEnd);
		          dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		          /*double db = (double) dos.size();

		          double d = db / 1024.0;
		          String gf = constants.getKEY_UPLOADED_DATA();
		          double df = Double.parseDouble(gf) + d;
		          Log.i("sp in dkb", "sp in dkb " + df);

		          constants.setKEY_UPLOADED_DATA(String.valueOf(df));
		          Log.i("data in dkb", "data in dkb " + df);
*/
		          // Responses from the server (code and message)
		          serverResponseCode = conn.getResponseCode();
		          String serverResponseMessage = conn.getResponseMessage();
		          BufferedReader bufferedReader = new BufferedReader(
		            new InputStreamReader(conn.getInputStream()));
		          String line = null;
		          StringBuilder sb = new StringBuilder();
		          while ((line = bufferedReader.readLine()) != null) {
		           System.out.println(line);
		           sb.append(line);
		          }
		          data = sb.toString();

		          // Toast.makeText(MainActivity.this, data,
		          // Toast.LENGTH_LONG).show();
		          Log.i("retruen value", "returnvalue" + data);

		          Log.i("uploadFile", "HTTP Response is : "
		            + serverResponseMessage + ": " + serverResponseCode);
		          if (serverResponseCode == 200) {
		           runOnUiThread(new Runnable() {
		            public void run() {
		             // tv.setText("File Upload Completed.");
		             // dialog.dismiss();
		             Toast.makeText(TextMemo_Image.this,
		               "File Upload Complete.", Toast.LENGTH_SHORT)
		               .show();
		             dialog.dismiss();
		             finish();
		            }
		           });
		          }

		          // close the streams //
		          fileInputStream.close();
		          dos.flush();
		          dos.close();

		         } catch (MalformedURLException ex) {
		          dialog.dismiss();
		          /*
		           * Toast.makeText(AddImage.this, "MalformedURLException",
		           * Toast.LENGTH_SHORT).show();
		           */
		          Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		         } catch (Exception e) {
		          dialog.dismiss();
		          e.printStackTrace();
		          // Toast.makeText(MainActivity.this,
		          // "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();
		          Log.e("Upload file to server Exception",
		            "Exception : " + e.getMessage(), e);
		         }
		         dialog.dismiss();
		         return serverResponseCode;
		        } // End else block
    }

}