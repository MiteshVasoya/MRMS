package com.mit.loginandregister;

import com.mit.loginandregister.R;

import android.app.Activity;
import android.os.Bundle;

public class SingleMenuItemActivity  extends Activity {
	
	// JSON node keys
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PHONE_MOBILE = "mobile";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_single_list_item);
        

    }
}
