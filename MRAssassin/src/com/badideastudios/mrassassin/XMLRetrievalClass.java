package com.badideastudios.mrassassin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.os.AsyncTask;
/*
 * XMLRetreivalClass is used to asynchronously submit a GET command to the server, and 
 * receive the necessary information
 */
public class XMLRetrievalClass extends AsyncTask<String, Void, Boolean>{

	private URL targetURL;
	DefaultHandler handler;
	XMLDelegate delegate;
	
	public XMLRetrievalClass(XMLDelegate delegate, DefaultHandler handler)
	{
		this.delegate = delegate;
		this.handler = handler;
	}
	
	@Override
	protected Boolean doInBackground(String... urls) {

		URL xmlURL;
		try
		{
			xmlURL = targetURL;//new URL("http://dev.fanjingo.com:8080/TsnService/tsn/list/twitter/names/tsn/15");//new URL(urls[0]);
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			
			XMLReader xr = sp.getXMLReader();
			
			xr.setContentHandler(handler);
			
			xr.parse(new InputSource(xmlURL.openStream()));
			
			return true;
			
		}catch(MalformedURLException e)
		{
			e.printStackTrace();
		}catch(ParserConfigurationException e)
		{
			e.printStackTrace();
		}catch(SAXException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		delegate.parseComplete(handler, result);
	}
	
	public void setURL(String url) throws MalformedURLException
	{
		targetURL = new URL(url);
	}
}
