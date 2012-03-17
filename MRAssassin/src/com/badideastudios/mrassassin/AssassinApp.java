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
	
	public void onCreate() 
	{ 
		//DELETE THIS LATER! Initializing for test purposes
		targetName = "Altair";
		targetBounty = 1000;
	}
	
	/** Put functions here to access our variables. */
	public String getOurMAC() { return ourBluetoothMAC; }
	public void setOurMAC(String mac) { ourBluetoothMAC = mac; }
	
	public String getTargetName() { return targetName; }
	public int getTargetBounty() { return targetBounty; }
}
