package com.badideastudios.mrassassin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LauncherActivity extends Activity 
{
	private SharedPreferences sharedPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
		sharedPrefs = getSharedPreferences("AssassinPrefs", 0);

        /** Set up C2DM */
        
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // Boilerplate
        registrationIntent.putExtra("sender", "anotherbadideastudios@gmail.com");//"mthomasleary@gmail.com");
        startService(registrationIntent);

        /*Get C2DM identifier */
	    String savedC2DM_ID = sharedPrefs.getString("registrationKey", "");
	    
	    /* If we have user data already available, just run, else show login */
	    if(sharedPrefs.getString("Username", "") == "" || sharedPrefs.getString("Server", "") == "" )
	    {
    		Intent loginActivity = new Intent(getBaseContext(), LoginActivity.class);
    		startActivity(loginActivity);
	    }
	    else
	    {
	    	Intent gameActivity = new Intent(getBaseContext(), TabWidget.class);
	    	startActivity(gameActivity);
	    }
	    finish();
	}

}
