package com.badideastudios.mrassassin;

import java.util.List;

import android.app.Activity;
import android.bluetooth.*;
import android.content.Context;
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
	/** Declarations for Radar circle*/
	private RadarView radar;
	private boolean sensorActive;
	private static SensorManager sensorManager;
    private String bluetoothMAC;
    
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
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar);
        radar = (RadarView)findViewById(R.id.radarview);
        
        /** Grab Bluetooth adapter and acquire our MAC address for server use.*/
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
        TextView BMACtext = (TextView) findViewById(R.id.macText);
        bluetoothMAC = deviceAdapter.getAddress();
        BMACtext.setText("Our MAC: " + bluetoothMAC);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        /** Grab GPS sensor and set it up to update automatically. */
        
        /** Grab the compass sensor and set it up to update automatically. */
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
}