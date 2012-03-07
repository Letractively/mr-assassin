package com.badideastudios.mrassassin;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class RadarActivity extends Activity 
{
	private static SensorManager sensorManager;
	private boolean sensorActive;
	private RadarView radar;
	
    private SensorEventListener RadarListener = new SensorEventListener()
    { 
    	public void onAccuracyChanged(Sensor sensor, int accuracy) 
    	{
    	}

    	public void onSensorChanged(SensorEvent event) 
    	{
    		radar.updateDirection((float)event.values[0]);
    	}
    };
	
    /** */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar);
        
        radar = (RadarView)findViewById(R.id.radarview);
     
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> compassSensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
     
        if(compassSensors.size() > 0)
        {
        	sensorManager.registerListener(RadarListener, compassSensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        	sensorActive = true;
        	Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();
       
        }
        else{
         Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
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