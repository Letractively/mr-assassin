package com.badideastudios.mrassassin;

public class AssassinObj {
	private String assassinName, targetName, macAddress;
	private double latitude, longitude;
	private int money, bounty;
	
	public AssassinObj(String assassinName, int latitude, int longitude, String targetName, String macAddress, int bounty, int money)
	{
		this.assassinName = assassinName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.targetName = targetName;
		this.macAddress = macAddress;
		this.bounty = bounty;
		this.money = money;
	}
	
	public AssassinObj() {
		// TODO Auto-generated constructor stub
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
	public void setBounty(int bounty)
	{
		this.bounty = bounty;;
	}
	
	
	public String returnTag()
	{
		return assassinName;
	}
	
	public String returnMACAddress()
	{
		return macAddress;
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
