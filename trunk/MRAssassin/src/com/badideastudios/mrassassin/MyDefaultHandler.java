package com.badideastudios.mrassassin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyDefaultHandler extends DefaultHandler {
	
	StringBuffer buffer; 
	ArrayList<AssassinObj> assassinList = new ArrayList<AssassinObj>();
	AssassinObj assassin = new AssassinObj();
	AssassinObj targetAssassin, currentAssassin;

	@Override
	public void startDocument() throws SAXException
	{
		// Called when the document starts
	}
	
	@Override
	public void endDocument() throws SAXException
	{
		System.out.println(assassinList.size());
		if(assassinList.size() > 0)
		{
			for(int i = 0; i < assassinList.size(); i++)
			{
				String userTag = assassinList.get(i).returnTag();
				String userName = "howlingblue";
					if(userTag.equals(userName)) // FOR TESTING PURPOSES, MANUALLY ENTER ID
					{
						assassin = assassinList.get(i);
					}		
			}
			
			for(int i = 0; i < assassinList.size(); i++)
			{
				if(assassinList.get(i).returnTag() != null)
				{
					if(assassinList.get(i).returnTag().equals(assassin.returnTarget()))
					{
						targetAssassin = assassinList.get(i);
					}
				}
				
			}
		}
		
		// Called when the document ends
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if(localName.equals("assassin"))
			currentAssassin = new AssassinObj();
		if(localName.equals("tag"))
			buffer = new StringBuffer();
		else if(localName.equals("target"))
			buffer = new StringBuffer();
		else if(localName.equals("bounty"))
			buffer = new StringBuffer();
		else if(localName.equals("lon"))
			buffer = new StringBuffer();
		else if(localName.equals("lat"))
			buffer = new StringBuffer();
		else if(localName.equals("money"))
			buffer = new StringBuffer();
		else if(localName.equals("macaddr"))
			buffer = new StringBuffer();
			//assassinName = atts.getValue("name");
		// Called each time the parser starts a new element
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		
		for(int i = start; i < start + length; i ++)
		{
			buffer.append(ch[i]);
		}
		
		// Called when the parser encounters characters between tags
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		
		if(localName.equals("tag"))
			currentAssassin.setTag(buffer.toString());
		else if(localName.equals("target"))
			currentAssassin.setTarget(buffer.toString());
		else if(localName.equals("bounty"))
			currentAssassin.setBounty(Integer.parseInt(buffer.toString()));
		else if(localName.equals("lon"))
			currentAssassin.setLon(Double.parseDouble(buffer.toString()));
		else if(localName.equals("lat"))
			currentAssassin.setLat(Double.parseDouble(buffer.toString()));
		else if(localName.equals("money"))
			currentAssassin.setBounty(Integer.parseInt(buffer.toString()));
		else if(localName.equals("macaddr"))
			currentAssassin.setMAC(buffer.toString());
		else if(localName.equals("assassin"))
			assassinList.add(currentAssassin);
		// Called when the parser ends an element		
	}
}
