package com.badideastudios.mrassassin;

import android.app.Application;
import android.hardware.GeomagneticField;
import android.location.Location;

/** This is the best location to store variables that are important to multiple activities.*/
public class AssassinApp extends Application
{
	/** Player Information */
	private String playerName;
	private int ourBounty;
	private int currentMoney;
	
	/** Sensor Information */
	private String ourBluetoothMAC;
	private Location lastBestLocation;
	private GeomagneticField field;
	
	/** Target Information */
	private String targetName;
	private int targetBounty;
	private Location targetLocation;
	
	public void onCreate() 
	{ 
		//Initializing location and field to prevent null references
		lastBestLocation = new Location("");
		lastBestLocation.setLatitude(1);
		lastBestLocation.setLongitude(1);
		lastBestLocation.setAltitude(1);
		lastBestLocation.setSpeed(0);
		field = new GeomagneticField(
				Double.valueOf(lastBestLocation.getLatitude()).floatValue(),
				Double.valueOf(lastBestLocation.getLongitude()).floatValue(),
				Double.valueOf(lastBestLocation.getAltitude()).floatValue(),
				System.currentTimeMillis());
		
		//DELETE THIS LATER! Initializing for test purposes
		targetName = "Altair";
		targetBounty = 1000;
		targetLocation = new Location("");
		targetLocation.setLatitude(30.640723);
		targetLocation.setLongitude(-96.318018);
	}
	
	/** Put functions here to access our variables. */
	public String getOurMAC() { return ourBluetoothMAC; }
	public void setOurMAC(String mac) { ourBluetoothMAC = mac; }
	
	public String getTargetName() { return targetName; }
	public int getTargetBounty() { return targetBounty; }
	public Location getTargetLocation() { return targetLocation; }
	
	//Location helper functions
	public GeomagneticField getGeoField() { return field; }
	public void setGeoField(Location location)
	{
		field = new GeomagneticField(
				Double.valueOf(location.getLatitude()).floatValue(),
				Double.valueOf(location.getLongitude()).floatValue(),
				Double.valueOf(location.getAltitude()).floatValue(),
				System.currentTimeMillis());
	}
	public Location getOurLocation() { return lastBestLocation; }
	public String printOurLocation() 
	{ 
		return "Lat: " + lastBestLocation.getLatitude() + "\nLong: " + lastBestLocation.getLongitude() + "\nSpeed: " + lastBestLocation.getSpeed(); 
	}
	public void updateLocation(Location newLocation) { lastBestLocation = newLocation; }
	
}
