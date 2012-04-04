package com.badideastudios.mrassassin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


import android.app.Application;
import android.hardware.GeomagneticField;
import android.location.Location;

/** This is the best location to store variables that are important to multiple activities.*/
public class AssassinApp extends Application
{
	/** Player Information */
	private AssassinObj target, player;
	private String playerName;
	private int ourBounty;
	private int currentMoney;
	
	/** Sensor Information */
	private String ourBluetoothMAC;
	private Location lastBestLocation;
	private GeomagneticField field;
	
	/** Target Information */
	private String targetName;
	private int targetBounty = 3;
	private Location targetLocation;
	private String targetMAC;
	
	/** Misc Information */
	private String version;
	/* Current server address */
	// mr-assassin.appspot.com/rest/assassin
	
	
	public void onCreate() 
	{ 
		version = "1.0 r36";
		//Initializing fields to prevent null references
		player = new AssassinObj();
		target = new AssassinObj();
		playerName = "Test Player";
		ourBounty = 400;
		currentMoney = 1500;
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
		 * Emer Tech Court: Lat: 30.622116 Long: -96.338950
		 */
	//	targetLocation.setLatitude(30.640709);
	//	targetLocation.setLongitude(-96.317943);
	//	targetMAC = "98:4B:4A:80:F1:36";
	}
	
	// Variable functions
	public String getVersion() { return version; }
	public String getOurName() { return player.returnTag(); }
	// Information functions
	public void setTarget(AssassinObj target)
	{
		this.target = target;
	}
	
	public void setPlayer(AssassinObj player)
	{
		this.player = player;
	}
	
	public AssassinObj getPlayer()
	{
		return player;
	}
	
	public AssassinObj getTarget()
	{
		return target;
	}
	
	public void setOurMAC(String mac)
	{
		ourBluetoothMAC = mac;
	}
	
	public Location getPlayerLocation()
	{
		return player.returnLoc();
	}

	public Location getTargetLocation()
	{
		return target.returnLoc();
	}
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
	public Location getOurLocation() { return player.returnLoc(); }
	public String printOurLocation() 
	{ 
		return "Lat: " + lastBestLocation.getLatitude() + "\nLong: " + lastBestLocation.getLongitude() + "\nSpeed: " + lastBestLocation.getSpeed(); 
	}
	public void updateLocation(Location newLocation) { player.setLoc(newLocation); }
}
