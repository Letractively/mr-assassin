package com.badideastudios.mrassassin;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyDefaultHandler extends DefaultHandler {
	
	ArrayList<String> nameList;
	StringBuffer buffer;
	String assassinName;
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
		if(localName.equals("name"))
			assassinName = atts.getValue("name");
		// Called each time the parser starts a new element
	}
	
	@Override
	public void characters(char ch[], int start, int length) throws SAXException
	{
		/*
		for(int i = start; i < start + length; i ++)
		{
			buffer.append(ch[i]);
		}
		*/
		// Called when the parser encounters characters between tags
	}
	
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
	//	assassinName = buffer.toString();
		// Called when the parser ends an element		
	}
}
