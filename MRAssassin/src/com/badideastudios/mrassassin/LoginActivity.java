package com.badideastudios.mrassassin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity 
{
	private EditText userForm;
	//private EditText passForm;
	private EditText servForm;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.login);    
	    userForm = (EditText) findViewById(R.id.username_entry);
	    //passForm = (EditText) findViewById(R.id.password_entry);
	    servForm = (EditText) findViewById(R.id.server_entry);
	    
	    /* Load previous user settings, if available. */
		settings = getSharedPreferences("AssassinPrefs", 0);
		String savedName = settings.getString("Username", ""), savedServer = settings.getString("Server", "");
		if(savedName.length() > 0)
			userForm.setText(savedName);
		if(savedServer.length() > 0)
			servForm.setText(savedServer);
	}
	
	public void login(View v)
	{
		// If all fields are not empty...
		if(userForm.length() <= 0 /*|| passForm.length() <=0 */|| servForm.length() <= 0)
		{
			Toast.makeText(this, "All fields must be filled in.", Toast.LENGTH_LONG).show();
			return;
		}

		/* Grab the form data. */
		String username, server;
		//String password;
		username = userForm.getText().toString();
		//password = passForm.getText().toString();
		server = servForm.getText().toString();
		
		/* Check for any errors. */
		if( username.contains(" ") )
		{
			Toast.makeText(this, "Username cannot contain a space.", Toast.LENGTH_LONG).show();
			return;
		}
		/*if( password.length() < 6 )
		{
			Toast.makeText(this, "Password is too short. Password must be at least 6 characters.", Toast.LENGTH_LONG).show();
			return;
		}*/
		
		/* Save user name and server data to user preferences for use if they need to login again. */
		settings = getSharedPreferences("AssassinPrefs", 0);
		SharedPreferences.Editor settingEditor = settings.edit();
		settingEditor.putString("Username", username);
		settingEditor.putString("Server", server);
		settingEditor.commit();
		
		/* Perform a login at our server. */
		
		/* Launch the game. */
    	Intent gameActivity = new Intent(getBaseContext(), TabWidget.class);
    	startActivity(gameActivity);
	}
    
    /** Handle the menu button press and present options */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.login_menu, menu);
    	return true;
    }
    
    /** Handle callback when a menu option is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch( item.getItemId() )
    	{
    	case R.id.exit:
    		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
    		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
    		startService(unregIntent);
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
