package com.badideastudios.mrassassin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LeaderboardHandler extends DefaultHandler {
	
	StringBuffer buffer; 

	ArrayList<AssassinObj> assassinList = new ArrayList<AssassinObj>();
	AssassinObj assassin = new AssassinObj();


	@Override
	public void startDocument() throws SAXException
	{
		// Called when the document starts
	}
	
	@Override
	public void endDocument() throws SAXException
	{		
		// Called when the document ends
	}
	
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException
	{
		if(localName.equals("following"))
		{
			System.out.println("Do nozing!");
		}
		if(localName.equals("assassin"))
			assassin = new AssassinObj();
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
			assassin.setTag(buffer.toString());
		else if(localName.equals("target"))
			assassin.setTarget(buffer.toString());
		else if(localName.equals("bounty"))
			assassin.setBounty(Integer.parseInt(buffer.toString()));
		else if(localName.equals("lon"))
			assassin.setLon(Double.parseDouble(buffer.toString()));
		else if(localName.equals("lat"))
			assassin.setLat(Double.parseDouble(buffer.toString()));
		else if(localName.equals("money"))
			assassin.setBounty(Integer.parseInt(buffer.toString()));
		else if(localName.equals("macaddr"))
			assassin.setMAC(buffer.toString());
		else if(localName.equals("assassin"))
			assassinList.add(assassin);
		// Called when the parser ends an element		
	}
}
