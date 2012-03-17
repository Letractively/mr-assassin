package com.badideastudios.mrassassin;

import android.location.*;
import android.os.Bundle;

public class GPSListener implements LocationListener
{
	public void onLocationChanged(Location location)
	{
		//Modify our current location if it's the best one we have.
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
			//Not really sure what the difference is here
			break;
		case LocationProvider.AVAILABLE:
			//We can reestablish the bonus counter.
			break;
		}
	}
}
