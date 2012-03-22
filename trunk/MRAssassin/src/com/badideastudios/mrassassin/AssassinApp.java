package com.badideastudios.mrassassin;

import android.app.Application;
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
	
	/** Target Information */
	private String targetName;
	private int targetBounty;
	private Location targetLocation;
	
	public void onCreate() 
	{ 
		//DELETE THIS LATER! Initializing for test purposes
		targetName = "Altair";
		targetBounty = 1000;
		targetLocation = new Location("");
		targetLocation.setLatitude(30.62216);
		targetLocation.setLongitude(-96.33892);
	}
	
	/** Put functions here to access our variables. */
	public String getOurMAC() { return ourBluetoothMAC; }
	public void setOurMAC(String mac) { ourBluetoothMAC = mac; }
	
	public String getTargetName() { return targetName; }
	public int getTargetBounty() { return targetBounty; }
	public Location getTargetLocation() { return targetLocation; }
	
	//Location helper functions
	public String printOurLocation() 
	{ 
		return "Lat: " + lastBestLocation.getLatitude() + "\nLong: " + lastBestLocation.getLongitude() + "\nSpeed: " + lastBestLocation.getSpeed(); 
	}
	public void updateLocation(Location newLocation) { lastBestLocation = newLocation; }
	
}
