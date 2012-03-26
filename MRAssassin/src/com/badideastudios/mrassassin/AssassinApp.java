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
	private String targetMAC;
	
	public void onCreate() 
	{ 
		//Initializing fields to prevent null references
		ourBluetoothMAC = "00:00:00:00:00";
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
		/* ARCC: Lat: 30.619425 Long: -96.338041
		 * Complex: Lat: 30.640709 Long: -96.317943
		 */
		targetLocation.setLatitude(30.640709);
		targetLocation.setLongitude(-96.317943);
		targetMAC = "98:4B:4A:80:F1:36";
	}
	
	/** Put functions here to access our variables. */
	public String getOurMAC() { return ourBluetoothMAC; }
	public void setOurMAC(String mac) { ourBluetoothMAC = mac; }
	
	public String getTargetName() { return targetName; }
	public int getTargetBounty() { return targetBounty; }
	public Location getTargetLocation() { return targetLocation; }
	public String getTargetMAC() { return targetMAC; }
	
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
