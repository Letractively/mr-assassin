package com.badideastudios.mrassassin;

import java.util.List;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Bundle;
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

public class RadarActivity extends Activity 
{
	static final int BLUETOOTH = 60;
	protected AssassinApp app;
	/** Declarations for Radar circle*/
	private RadarView radar;
	BluetoothAdapter ourAdapter;
	private static LocationManager locManager;
	private static SensorManager sensorManager;
	private TextView BMACtext;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        app = (AssassinApp) getApplication();
        
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
    }
    
    @Override
    protected void onStart()
    {
    	super.onStart();
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
        
        /** Set up Bluetooth for discovery mode for target finding. */
        IntentFilter blueFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(targetFinder, blueFilter);
        
        /** Reengage our compass sensor only when this app is visible */
        List<Sensor> compassSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
    	sensorManager.registerListener(RadarListener, compassSensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
    }
    
    @Override
    protected void onPause()
    {
    	super.onPause();
	 	sensorManager.unregisterListener(RadarListener);
		unregisterReceiver(targetFinder);
    }
    
    @Override
    protected void onStop()
    {
    	super.onStop();
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
    
    public void test_button(View v)
    {
    	ourAdapter.startDiscovery();
    }
    
    private final BroadcastReceiver targetFinder = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //When we find a device, check if it's our target
            if (BluetoothDevice.ACTION_FOUND.equals(action)) 
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            	//Toast.makeText(context, device.getAddress(), Toast.LENGTH_SHORT).show();
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
}