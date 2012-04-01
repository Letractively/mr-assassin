package com.badideastudios.mrassassin;

import java.net.MalformedURLException;
import java.util.List;

import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.provider.Settings;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

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
	private TextView BMACtext;
	
	private RadarActivity act;
	private Activity act2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        app = (AssassinApp) getApplication();
        act = this;
        act2 = (Activity)this;
        
        
    	/** Set up our layout. */
        setContentView(R.layout.radar);
        radar = (RadarView)findViewById(R.id.radarview);     
        BMACtext = (TextView) findViewById(R.id.macText);
        
        /** Grab Bluetooth adapter. */
        ourAdapter = BluetoothAdapter.getDefaultAdapter();
        if(ourAdapter == null)
        {
        	Toast.makeText(this, "Game cannot work without Bluetooth. Exiting.", Toast.LENGTH_LONG).show();
        	finish();
        }
        
        /** Find out our Bluetooth MAC for the server. */
        app.setOurMAC( ourAdapter.getAddress() );
        BMACtext.setText( "Our MAC: " + app.getOurMAC() );

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
    	noteManager.notify(warnID, warningNote);
    }
    
    public void test_button(View v)
    {
    	ourAdapter.startDiscovery();
    }
    
    public void test_button2(View v)
    {
    	warn();
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
                if( device.getAddress().equals( app.getTargetMAC() ) )
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
    		//Modify our current location if it's the best one we have.
    		app.updateLocation(location);
    		
    		CreateUserTask cut = new CreateUserTask(act);
    		MyDefaultHandler mdh = new MyDefaultHandler();
    		XMLRetrievalClass XMLrc = new XMLRetrievalClass((XMLDelegate) act, mdh);
    		try {
				XMLrc.setURL("http://mr-assassin.appspot.com/rest/get/assassins");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		cut.SetAddress("http://mr-assassin.appspot.com/rest/update/position");
    		cut.SetLat(location.getLatitude());
    		cut.SetLon(location.getLongitude());
    		cut.SetName(app.getOurName());
    		cut.SetMAC(app.getOurMAC());
    		
    		XMLrc.execute();
    		cut.execute();
    		//if( location.distanceTo( app.getTargetLocation() ) < 6 )
    		//{
    	    	//ourAdapter.startDiscovery();
    		//}
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
    	{
    		/* This is called when the GPS status alters */
    		switch (status) 
    		{
    		case LocationProvider.OUT_OF_SERVICE:
    			//We need to notify the server that we are out
    			break;
    		case LocationProvider.TEMPORARILY_UNAVAILABLE:
    			//This means we're taking a break, since we haven't met time or distance inaccuracy level to update.
    			break;
    		case LocationProvider.AVAILABLE:
    			//We can reestablish the bonus counter.
    			break;
    		}
    	}
    };

    /*Orientation Listener*/
	private SensorEventListener RadarListener = new SensorEventListener()
    { 
    	public void onAccuracyChanged(Sensor sensor, int accuracy) 
    	{
    	}

    	public void onSensorChanged(SensorEvent event) 
    	{
    		radar.update(event.values, app.getOurLocation(), app.getTargetLocation(), app.getGeoField());
    	}
    };

	public void parseComplete(DefaultHandler handler, Boolean result) {
		
		MyDefaultHandler mdh = (MyDefaultHandler)handler;
		app.setTargetName(mdh.assassin.returnTarget());
		app.setTargetBounty(mdh.targetAssassin.returnBounty());
		app.setTargetMAC(mdh.targetAssassin.returnMACAddress());
		Location targetLoc = new Location("");
		targetLoc.setLatitude(mdh.targetAssassin.returnLat());
		targetLoc.setLongitude(mdh.targetAssassin.returnLon());
		app.setPlayerName(mdh.assassin.returnTag());
		app.setTargetLocation(targetLoc);
		
	}
}