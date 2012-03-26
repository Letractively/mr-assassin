package com.badideastudios.mrassassin;

import java.util.List;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RadarActivity extends Activity 
{
	protected AssassinApp app;
	/** Declarations for Radar circle*/
	private RadarView radar;
	private boolean blueActive;
	private boolean sensorActive;
	private static LocationManager locManager;
	private static SensorManager sensorManager;
	private TextView locText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        app = (AssassinApp) getApplication();
        
    	/** Set up our layout. */
        setContentView(R.layout.radar);
        radar = (RadarView)findViewById(R.id.radarview);
        
        /** Grab Bluetooth adapter and acquire our MAC address for server use. */
        BluetoothAdapter ourAdapter = BluetoothAdapter.getDefaultAdapter();
        if(ourAdapter == null)
        {
        	Toast.makeText(this, "No Bluetooth. Exiting.", Toast.LENGTH_LONG).show();
        	finish();
        }
        int BLUETOOTH = 1;
        if (!ourAdapter.isEnabled()) 
        {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivityForResult(discoveryIntent, BLUETOOTH);
        }
        app.setOurMAC( ourAdapter.getAddress() );
        TextView BMACtext = (TextView) findViewById(R.id.macText);
        BMACtext.setText( "Our MAC: " + app.getOurMAC() );
        IntentFilter blueFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(targetFinder, blueFilter);

        /** Grab GPS sensor and set it up to update automatically. */
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15, 15, GPSListener);
        locText = (TextView) findViewById(R.id.locText);
        
        /** Grab the compass sensor and set it up to update automatically. */
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> compassSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        if(compassSensors.size() > 0)
        {
        	sensorManager.registerListener(RadarListener, compassSensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        	sensorActive = true;
        }
        else
        {
        	Toast.makeText(this, "No Orientation Sensor. Exiting.", Toast.LENGTH_LONG).show();
        	sensorActive = false;
        	finish();
        }
    }

    @Override
    protected void onDestroy() 
    {
    	super.onDestroy();
    	if(sensorActive)
    	{
    	 	sensorManager.unregisterListener(RadarListener);
    	}
    	if(blueActive)
    	{
    		unregisterReceiver(targetFinder);
    	}
    }
    
    public void kill(View v)
    {
    	Toast.makeText(this, "Kill completed.", Toast.LENGTH_SHORT).show();
    	Button killButton = (Button)findViewById(R.id.kill_button);
    	killButton.setVisibility(View.GONE);
    	
    }
    
    public void show_button(View v)
    {
    	//Button killButton = (Button)findViewById(R.id.kill_button);
    	//killButton.setVisibility(0);
        BluetoothAdapter ourAdapter = BluetoothAdapter.getDefaultAdapter();
    	ourAdapter.startDiscovery();
    }
    
    public void updateLocationText() { locText.setText(app.printOurLocation()); }
    
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
    		updateLocationText();
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