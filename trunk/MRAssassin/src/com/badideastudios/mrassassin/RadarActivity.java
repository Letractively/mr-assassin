package com.badideastudios.mrassassin;

import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.content.IntentFilter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RadarActivity extends Activity implements XMLDelegate 
{
	static final int BLUETOOTH = 60;
	protected AssassinApp app;
	/** Declarations for Radar circle*/
	private RadarView radar;
	BluetoothAdapter ourAdapter;
	private static LocationManager locManager;
	private static NotificationManager noteManager;
	private static SensorManager sensorManager;
	private TextView GPStext;
	
	SharedPreferences sharedPrefs;
	
	private RadarActivity act;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        app = (AssassinApp) getApplication();
        act = this;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        
        /** Set up C2DM */
        
        Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0)); // Boilerplate
        registrationIntent.putExtra("sender", "anotherbadideastudios@gmail.com");//"mthomasleary@gmail.com");
        startService(registrationIntent);

     //   Intent messageIntent = new Intent("com.google.android.c2dm.intent.RECEIVE");
        
    	/** Set up our layout. */
        setContentView(R.layout.radar);
        radar = (RadarView)findViewById(R.id.radarview);
        GPStext = (TextView) findViewById(R.id.status_text_1);
        
        /** Grab Bluetooth adapter. */
        ourAdapter = BluetoothAdapter.getDefaultAdapter();
        if(ourAdapter == null)
        {
        	Toast.makeText(this, "Game cannot work without Bluetooth. Exiting.", Toast.LENGTH_LONG).show();
        	finish();
        }
        
        /** Find out our Bluetooth MAC for the server. */
        app.getPlayer().setMAC( ourAdapter.getAddress() );
       // BMACtext.setText( "Our MAC: " + app.getPlayer().returnMACAddress()); );

        /** Grab GPS sensor and set it up to update automatically. */
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 15, GPSListener);
        
        /** Verify we have a compass sensor. */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> compassSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(compassSensors.size() <= 0)
        {
        	Toast.makeText(this, "No Orientation Sensor. Exiting.", Toast.LENGTH_LONG).show();
        	finish();
        }
        
        /** Acquire notification manager for notifications.*/
        noteManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
        /** Set up Bluetooth for discovery mode for target finding. */
        IntentFilter blueFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(targetFinder, blueFilter);
    }
    
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
        /** Verify that GPS and Bluetooth are available on resume. If not, ask the user to enable. */
        if ( !locManager.isProviderEnabled(LocationManager.GPS_PROVIDER) )
        {
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        	builder.setMessage("This app requires GPS in order to play. Would you like to go to the settings to enable GPS?")
        		   .setCancelable(false)
        		   .setNegativeButton("No", new DialogInterface.OnClickListener()
        		   {
        			   public void onClick(DialogInterface dialog, int id) { RadarActivity.this.finish(); }
        		   })
        		   .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        		   {
        			   public void onClick(DialogInterface dialog, int id) 
        			   { 
        				   Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        				   startActivity(gpsIntent); 
        			   }
        		   });
        	AlertDialog gpsAlert = builder.create();
        	gpsAlert.show();
        }
        if (ourAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivityForResult(discoveryIntent, BLUETOOTH);
        }
        
        /** Reengage our compass sensor only when this app is visible */
        List<Sensor> compassSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
    	sensorManager.registerListener(RadarListener, compassSensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
	 	sensorManager.unregisterListener(RadarListener);
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
		unregisterReceiver(targetFinder);
    }

    @Override
    protected void onDestroy() 
    {
    	super.onDestroy();
    	locManager.removeUpdates(GPSListener);
    }
    
    /** This function handles the callback from the Bluetooth enabler given to the user on startup. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == BLUETOOTH)
    	{
    		if(resultCode == RESULT_CANCELED)
    		{
        		Toast.makeText(this, "Unable to access Bluetooth. Exiting.", Toast.LENGTH_SHORT).show();
    			finish();
    		}
    	}
    }
    
    public void kill(View v)
    {
    	Toast.makeText(this, "Assassination Successful.", Toast.LENGTH_SHORT).show();
    	Button killButton = (Button)findViewById(R.id.kill_button);
    	killButton.setVisibility(View.GONE);
    	CreateUserTask cut = new CreateUserTask(act);
    	cut.SetAddress("http://mr-assassin.appspot.com/rest/update/kill");
    	cut.SetContentType("application/xml");
		cut.setResponse(false);
		cut.SetContent("<assassin>" +
				"<tag>" +
				app.getPlayer().returnTag() +
				"</tag>" +
				"</assassin>");
		cut.execute();
    }
    
    public void warn()
    {
    	int warnID = 1;
    	int icon = R.drawable.dagger;
    	CharSequence tickerText = "Danger!";
    	long when = System.currentTimeMillis();
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "MRAssassins";
    	CharSequence contentText = "An assassin may be close. Be careful.";
    	Intent notificationIntent = new Intent(this, RadarActivity.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	
    	Notification warningNote = new Notification(icon, tickerText, when);
    	warningNote.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	
    	if( sharedPrefs.getBoolean("NoteSoundPref", true) == true)
    		warningNote.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.warning);
    	if( sharedPrefs.getBoolean("NoteVibePref", true) == true)
    		warningNote.defaults |= Notification.DEFAULT_VIBRATE;
    	noteManager.notify(warnID, warningNote);
    }
    
    public void warn(View v)
    {
    	warn();
    }
    
    public void setGPSText(String text)    { GPStext.setText(text); }
    public void setGPSTextColor(int color) { GPStext.setTextColor(color); }
    
    public void test_button(View v)
    {
    	ourAdapter.startDiscovery();
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
    
    private final BroadcastReceiver targetFinder = new BroadcastReceiver() 
    {
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            //When we find a device, check if it's our target
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if( device.getAddress().equals( app.getTarget().returnMACAddress()))//getTargetMAC() ) )
                {
                	//If so, show our button
                	Button killButton = (Button)findViewById(R.id.kill_button);
                	killButton.setVisibility(0);
                	ourAdapter.cancelDiscovery();
                }
            }
        }
    };
    
    /*GPS Listener*/
    private LocationListener GPSListener = new LocationListener()
    {
    	public void onLocationChanged(Location location)
    	{
    		/**If our new location information has accuracy info available and 
    		 * has more accuracy than our old location, then update location.
    		 * Also update location if the distance from our new fix to the old is further than 10m.
    		 */
    		Location oldLoc = app.getOurLocation();
    		if( (location.getAccuracy() < oldLoc.getAccuracy() && location.getAccuracy() != 0.0) ||
    			location.distanceTo(oldLoc) > 10)
    		{
    			app.updateLocation(location);
    		}
            /** Set up C2DM */
            Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
            registrationIntent.putExtra("app", PendingIntent.getBroadcast(act, 0, new Intent(), 0)); // Boilerplate
            registrationIntent.putExtra("sender", "anotherbadideastudios@gmail.com");//"mthomasleary@gmail.com");
            startService(registrationIntent);
            System.out.println("Registration update successful");
    		
    		
    		CreateUserTask cut = new CreateUserTask(act);
    		MyDefaultHandler mdh = new MyDefaultHandler();
    		sharedPrefs = getSharedPreferences("AssassinPrefs", 0);
    		mdh.userName = sharedPrefs.getString("Username", "");
    //		XMLRetrievalClass XMLrc = new XMLRetrievalClass((XMLDelegate) act, mdh);
    //		try {
	//			XMLrc.setURL("http://mr-assassin.appspot.com/rest/get/assassins");
	//		} catch (MalformedURLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
    		
    		cut.SetAddress("http://mr-assassin.appspot.com/rest/update/position");
    		cut.SetContentType("application/xml");
    		cut.setResponse(false);
    		String registrationKey = sharedPrefs.getString("registrationKey", "");
    		System.out.println("Registration key is " + registrationKey);
    		//System.out.println("C2DM Prefs is " + sharedPrefs.getString("c2dmPref", ""));
    		cut.SetContent("<assassin>" +
					"<tag>" +
					app.getPlayer().returnTag() +
					"</tag>" +
					"<lat>" +
					app.getPlayer().returnLoc().getLatitude() +
					"</lat>" +
					"<lon>" +
					app.getPlayer().returnLoc().getLongitude() +
					"</lon>" +
					"<mac>" +
					app.getPlayer().returnMACAddress() +
					"</mac>" +
					"<regID>" +
					registrationKey +
					"</regID>" +
					"</assassin>");
    		cut.SetLat(location.getLatitude());
    		cut.SetLon(location.getLongitude());
    		cut.SetName(app.getPlayer().returnTag());//appgetOurName());
    		cut.SetMAC(app.getPlayer().returnMACAddress());//app.getOurMAC());
    	/*	
    		CreateUserTask cut2 = new CreateUserTask(act);
    		cut2.SetAddress("http://mr-assassin.appspot.com/rest/get/target");
    		cut2.SetContentType("text/plain");
    		cut2.setResponse(true);
    		cut2.SetContent("<assassin>" +
    				"<tag>" +
    				app.getPlayer().returnTag() +
    				"</tag>" +
    				"</assassin>");
    		
    		*/
    	//	XMLrc.execute();
    		cut.execute();
    	//	cut2.execute();
    	}
    	
    	public void onProviderDisabled(String provider)
    	{
    		//Ask the user to give us back GPS. If not, quit.
    	}
    	
    	public void onProviderEnabled(String provider)
    	{
    		//Not really a lot to do here. Pop up that it's enabled?
    	}
    	
    	public void onStatusChanged(String provider, int status, Bundle stuff)
    	{ /** This function apparently does absolutely nothing in the current API. */ }
    };

    /*Orientation Listener*/
	private SensorEventListener RadarListener = new SensorEventListener()
    { 
    	public void onAccuracyChanged(Sensor sensor, int accuracy) 
    	{
    	}

    	public void onSensorChanged(SensorEvent event) 
    	{
    		radar.update(event.values, app.getOurLocation(), app.getTarget().returnLoc(), app.getGeoField());
    	}
    };
  
	public void parseComplete(DefaultHandler handler, Boolean result) {
		
		//AssassinHandler ah = (AssassinHandler)handler;
		//app.setTarget(ah.assassinList.get(0));
		//app.setTarget(ah.assassinList.get(1));
		MyDefaultHandler mdh = (MyDefaultHandler)handler;
		app.setTarget(mdh.targetAssassin);
		app.setPlayer(mdh.assassin);
	/*	app.setTargetName(mdh.assassin.returnTarget());
		app.setTargetBounty(mdh.targetAssassin.returnBounty());
		app.setTargetMAC(mdh.targetAssassin.returnMACAddress());
		Location targetLoc = new Location("");
		targetLoc.setLatitude(mdh.targetAssassin.returnLat());
		targetLoc.setLongitude(mdh.targetAssassin.returnLon());
		app.setPlayerName(mdh.assassin.returnTag());
		app.setTargetLocation(targetLoc);
	*/	
	}
}