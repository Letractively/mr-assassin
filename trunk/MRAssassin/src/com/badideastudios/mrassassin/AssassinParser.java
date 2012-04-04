package com.badideastudios.mrassassin;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.XMLReader;

public class AssassinParser {
	
	public AssassinObj parse(String str)
	{
		AssassinObj assassin = new AssassinObj();
		
		SAXParserFactory spf = SAXParserFactory.newInstance();
//		SAXParser sp = spf.newSAXParser();
//		XMLReader xr = sp.getXMLReader();
		
//		xr.parse(str);
		return assassin;
	}

}
