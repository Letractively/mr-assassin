package com.badideastudios.mrassassin;

public class AssassinObj 
{
	private String assassinName = "Undefined", targetName = "Undefined", macAddress = "Undefined";
	private double latitude = 0.0, longitude = 0.0;
	private int bounty = 0, kills = 0, money = 0;
	
	public AssassinObj()
	{
		this.assassinName = "Altair";
		this.latitude = 1;
		this.longitude = 1;
		this.targetName = "Nobody";
		this.macAddress = "00:00:00:00:00:00";
		this.bounty = 1000;
		this.kills = 0;
		this.money = 0;
	}
	
	public AssassinObj(String assassinName, int latitude, int longitude, String targetName, String macAddress, int bounty, int kills, int money)
	{
		this.assassinName = assassinName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.targetName = targetName;
		this.macAddress = macAddress;
		this.bounty = bounty;
		this.kills = kills;
		this.money = money;
	}

	public void setTag(String assassinName)
	{
		this.assassinName = assassinName;
	}
	
	public void setTarget(String targetName)
	{
		this.targetName = targetName;
	}
	
	public void setLat(double latitude)
	{
		this.latitude = latitude;
	}
	
	public void setLon(double longitude)
	{
		this.longitude = longitude;
	}
	
	public void setMAC(String macAddress)
	{
		this.macAddress = macAddress;
	}
	
	public void setMoney(int money)
	{
		this.money = money;
	}
	
	public void setKills(int kills)
	{
		this.kills = kills;
	}
	
	public void setBounty(int bounty)
	{
		this.bounty = bounty;
	}
	
	public String returnTag()
	{
		return assassinName;
	}
	
	public double returnLat()
	{
		return latitude;
	}
	
	public double returnLon()
	{
		return longitude;
	}
	
	public String returnMACAddress()
	{
		return macAddress;
	}
	
	public int returnMoney()
	{
		return money;
	}
	
	public int returnKills()
	{
		return kills;
	}
	
	public String returnTarget()
	{
		return targetName;
	}
	
	public int returnBounty()
	{
		return bounty;
	}
}
