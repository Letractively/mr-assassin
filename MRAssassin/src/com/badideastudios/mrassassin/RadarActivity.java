package com.badideastudios.mrassassin;

import java.util.List;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
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
        BluetoothAdapter deviceAdapter = BluetoothAdapter.getDefaultAdapter();
        if(deviceAdapter == null)
        {
        	Toast.makeText(this, "No Bluetooth. Exiting.", Toast.LENGTH_LONG).show();
        	finish();
        }
        int REQUEST_ENABLE_BT = 1;
        if (!deviceAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        app.setOurMAC( deviceAdapter.getAddress() );
        TextView BMACtext = (TextView) findViewById(R.id.macText);
        BMACtext.setText( "Our MAC: " + app.getOurMAC() );

        /** Grab GPS sensor and set it up to update automatically. */
        locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30, 30, GPSListener);
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
    }
    
    public void updateLocationText() { locText.setText(app.printOurLocation()); }
    
    /*Listener Declarations*/
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
    
	private SensorEventListener RadarListener = new SensorEventListener()
    { 
    	public void onAccuracyChanged(Sensor sensor, int accuracy) 
    	{
    	}

    	public void onSensorChanged(SensorEvent event) 
    	{
    		radar.updateDirection(event.values);
    	}
    };
}