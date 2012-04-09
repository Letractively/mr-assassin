package com.badideastudios.mrassassin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LauncherActivity extends Activity {

	private SharedPreferences settings;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    String savedC2DM_ID = settings.getString("c2dmPref", "");
	    // TODO Auto-generated method stub
	}

}
