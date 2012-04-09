package com.badideastudios.mrassassin;

import android.app.Application;
import android.hardware.GeomagneticField;
import android.location.Location;

/** This is the best location to store variables that are important to multiple activities.*/
public class AssassinApp extends Application
{
	/** Player Information */
	private AssassinObj target, player;
	
	/** Sensor Information */
	private Location lastBestLocation;
	private GeomagneticField field;
	
	/** Misc Information */
	private String version;
	/* Current server address */
	// mr-assassin.appspot.com/rest/assassin
	
	
	public void onCreate() 
	{ 
		//Populate version information for menu
		version = "1.0 r36";
		
		//Initialize fields to prevent null references
		player = new AssassinObj();
		target = new AssassinObj();
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
