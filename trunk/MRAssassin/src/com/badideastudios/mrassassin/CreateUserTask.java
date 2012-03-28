package com.badideastudios.mrassassin;

import java.lang.ref.WeakReference;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.os.AsyncTask;

/*
 * CreateUserTask is used to create an asynchronous task 
 * for performing XML POST commands and relaying our client information
 * back to the server
 */
public class CreateUserTask extends AsyncTask<Void, Void, Boolean>{
	public WeakReference<Activity> parentActivity;
	private String name, targetURL, mac;
	private int score;
	private double lat, lon;
	
	
	public CreateUserTask(Activity activity)
	{
		parentActivity = new WeakReference<Activity>(activity);
	}

	@Override
	protected void onPreExecute()
	{
		
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {

		try
		{
			// Create the XML string we want to send
			String xmlString = new String();
			xmlString = 
			//	"<assassins>" +
				"<assassin>" +
				//"<score>" +
				//score +
				//"</score>" +
				"<tag>" +
				name +
				"</tag>" +
				"<lat>" +
				lat +
				"</lat>" +
				"<lon>" +
				lon +
				"</lon>" +
				"<mac>" +
				mac +
				"</mac>" +
				"</assassin>";// +
			//	"</assassins>";
			// And create the address we want to use (for now, we'll just use one address)
			String postURL = targetURL;
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(postURL);
			
			// Create a string entity of our XML string
			StringEntity se = new StringEntity(xmlString, HTTP.UTF_8);
			// Set ContentType to "application/xml" so the server will accept it
			se.setContentType("application/xml");
			httppost.setEntity(se);
			HttpResponse httpresponse = httpclient.execute(httppost);
			System.out.println("HEAR YE HEAR YE, " + httpresponse.getStatusLine().getStatusCode());
			
			return (httpresponse.getStatusLine().getStatusCode() == 200);
		}catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
		

	@Override
	protected void onPostExecute(Boolean result)
	{
		if(result)
		{
			System.out.println("Successful delivery");
		}
		else System.out.println("Shamefrul dispray");
	}

	public void SetInformation(String name, int score)
	{
		this.name = name;
		this.score = score;
	}
	
	public void SetAddress(String address)
	{
		targetURL = address;
	}
	
	public void SetMAC(String mac)
	{
		this.mac = mac;
	}
	
	public void SetName(String tag)
	{
		this.name = tag;
	}
	
	public void SetLat(Double lat)
	{
		this.lat = lat;
	}
	
	public void SetLon(Double lon)
	{
		this.lon = lon;
	}
	

}
