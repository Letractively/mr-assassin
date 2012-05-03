package com.badideastudios.mrassassin;

import java.net.MalformedURLException;

import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class TargetActivity extends Activity implements XMLDelegate
{
	protected AssassinApp app;
	SharedPreferences sharedPrefs;
	
	TextView name, currentKills, currentBounty, currentMoney;
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		sharedPrefs = getSharedPreferences("AssassinPrefs", 0);
	    super.onCreate(savedInstanceState);
	    app = (AssassinApp)getApplication();
	    setContentView(com.badideastudios.mrassassin.R.layout.target);
	    
	    dialog = new ProgressDialog(this);
	    
	    name = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetName);
	    currentKills =  (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetKills);
	    currentBounty = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetBounty);
	    currentMoney =  (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetMoney);
	    
	    // POST
	    CreateUserTask cut = new CreateUserTask(this);
	    //cut.SetAddress("http://mr-assassin.appspot.com/rest/assassin");
	    //cut.SetContent("5");
	    cut.SetAddress("http://mr-assassin.appspot.com/rest/update/assassin");
	    cut.SetContentType("application/xml");
	    String updateC2DM = "<assassin>" +
	    	"<tag>" +
	    	sharedPrefs.getString("Username", "") +
	    	"</tag>" +
	    	"<regID>" +
	    	sharedPrefs.getString("registrationKey", "") +
	    	"</regID>" +
	    	"</assassin>";
	    
	    System.out.println(sharedPrefs.getString("registrationKey", ""));
	    	
	    cut.SetContent(updateC2DM);
	    cut.execute();
	    // GET
	    /*
	    sharedPrefs = getSharedPreferences("AssassinPrefs", 0);
	    AssassinHandler ah = new AssassinHandler();
	    CreateUserTask cut = new CreateUserTask(this,this, ah);
	    cut.SetAddress("http://mr-assassin.appspot.com/rest/get/target");
	    cut.SetContent(sharedPrefs.getString("Username", ""));
	    cut.SetContentType("text/plain");
	    
	    cut.execute();
	    */
		MyDefaultHandler mdh = new MyDefaultHandler();
		mdh.userName = sharedPrefs.getString("Username", "");
		XMLRetrievalClass XMLrc = new XMLRetrievalClass(this, mdh);
		
		try {
			XMLrc.setURL("http://mr-assassin.appspot.com/rest/get/assassins");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLrc.execute();
		//cut.execute();
		
		dialog.show();
		/*
		app.setTargetName(mdh.assassinName);
	    
	    TextView name = new TextView(this);
	    name = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetName);
	    name.setText( "Current Target: " + app.getTargetName() );
	    
	    TextView currentBounty = new TextView(this);
	    currentBounty = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetBounty);
	    currentBounty.setText( "Target Bounty: " + app.getTargetBounty() );
		*/
	}
	
	public void onDestroy()
	{
		super.onDestroy();
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		startService(unregIntent);
	}
	
	public void parseComplete(DefaultHandler handler, Boolean result) {
		dialog.hide();
	//	AssassinHandler ah = (AssassinHandler)handler;
	//	app.setPlayer(ah.assassinList.get(0));
	//	app.setTarget(ah.assassinList.get(1));
		
		MyDefaultHandler mdh = (MyDefaultHandler)handler;
		app.setTarget(mdh.targetAssassin);
		app.setPlayer(mdh.assassin);
/*
		app.setTargetName(mdh.assassin.returnTarget());
		app.setTargetBounty(mdh.targetAssassin.returnBounty());
		app.setPlayerName(mdh.assassin.returnTag());
*/	    
	    name.setText("Current Target: " + app.getTarget().returnTag());
	    currentKills.setText("Target Kills: " + app.getTarget().returnKills());
	    currentBounty.setText("Target Bounty: " + app.getTarget().returnBounty());
	    currentMoney.setText("Target Money: " + app.getTarget().returnMoney());
	}
	
	public AssassinApp returnApp()
	{
		return app;
	}
    
    /** Handle the menu button press and present options */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.options_menu, menu);
    	return true;
    }
    
    /** Handle callback when a menu option is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch( item.getItemId() )
    	{
    	case R.id.settings:
    		Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
    		startActivity(settingsActivity);
    		return true;
    	case R.id.help:
    		Intent helpActivity = new Intent(getBaseContext(), HelpActivity.class);
    		startActivity(helpActivity);
    		return true;
    	case R.id.exit:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
