package com.mit.loginandregister;


import com.mit.loginandregister.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

	public class HomepageActivity extends Activity {
		
		LinearLayout my_location,my_customer,my_places,my_visit,add_textmemo,my_team;
		ImageView logout;
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.homepage);
	        
	        //Service started
	        startService(new Intent(getBaseContext(), MyService.class));
	        
	        my_location=(LinearLayout)findViewById(R.id.my_location);
	        my_customer=(LinearLayout)findViewById(R.id.my_customer);
	        
	        my_location.setOnClickListener(new OnClickListener() {
	    		
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), MyLocation.class);
	    			startActivity(i);
	    		}
	    	});
	        
	        my_customer.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), MyCustomer.class);
	    			startActivity(i);
				}
			});
	        
	        my_places=(LinearLayout)findViewById(R.id.my_places);
	        
	        my_places.setOnClickListener(new OnClickListener() {
	    		
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), MyPlaces.class);
	    			startActivity(i);
	    		}
	    	});
	        
	        my_visit=(LinearLayout)findViewById(R.id.my_visit);
	        
	        my_visit.setOnClickListener(new OnClickListener() {
	    		
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), MyVisit.class);
	    			startActivity(i);
	    		}
	    	});
	        
	        add_textmemo=(LinearLayout)findViewById(R.id.my_textmemo);
	        
	        add_textmemo.setOnClickListener(new OnClickListener() {
	    		
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), AddTextMemo.class);
	    			startActivity(i);
	    		}
	    	});
	        
	        my_team=(LinearLayout)findViewById(R.id.my_team);
	        
	        my_team.setOnClickListener(new OnClickListener() {
	    		
	    		public void onClick(View v) {
	    			// TODO Auto-generated method stub
	    			Intent i = new Intent(getApplicationContext(), MyTeam.class);
	    			startActivity(i);
	    		}
	    	});
	        
	        logout = (ImageView)findViewById(R.id.logout);
	        logout.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	}
	    
	    
	    // Initiating Menu XML file (menu.xml)
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu)
	    {
	        MenuInflater menuInflater = getMenuInflater();
	        menuInflater.inflate(R.layout.menu, menu);
	        return true;
	    }
	     
	    /**
	     * Event Handling for Individual menu item selected
	     * Identify single menu item by it's id
	     * */
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	         
	        switch (item.getItemId())
	        {
	        case R.id.menu_stopService:
	            // Single menu item is selected do something
	            // Ex: launching new activity/screen or show alert message
	            //Toast.makeText(LoginActivity.this, "Service is stopped.", Toast.LENGTH_SHORT).show();
	            stopService(new Intent(getBaseContext(), MyService.class));
	            return true;
	 
	        
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    } 
}
