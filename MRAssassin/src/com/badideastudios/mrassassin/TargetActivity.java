package com.badideastudios.mrassassin;

import java.net.MalformedURLException;

import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class TargetActivity extends Activity implements XMLDelegate
{
	protected AssassinApp app;
	
	TextView name, currentBounty;
	ProgressDialog dialog;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    app = (AssassinApp)getApplication();
	    setContentView(com.badideastudios.mrassassin.R.layout.target);
	    
	    dialog = new ProgressDialog(this);
	    
	    name = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetName);
	    currentBounty = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetBounty);
	    
	    // POST
	    //CreateUserTask cut = new CreateUserTask(this);
	    //cut.SetAddress("http://mr-assassin.appspot.com/rest/assassin");
	    //cut.SetInformation("I'mmakinganotehere,hugesuccess", 100000001);
	    
	    // GET
		MyDefaultHandler mdh = new MyDefaultHandler();
		XMLRetrievalClass XMLrc = new XMLRetrievalClass(this, mdh);
		try {
			XMLrc.setURL("http://mr-assassin.appspot.com/rest/get/assassins");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		XMLrc.execute();
		//cut.execute();

		dialog.show();
		/*
		app.setTargetName(mdh.assassinName);
	    
	    TextView name = new TextView(this);
	    name = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetName);
	    name.setText( "Current Target: " + app.getTargetName() );
	    
	    TextView currentBounty = new TextView(this);
	    currentBounty = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetBounty);
	    currentBounty.setText( "Target Bounty: " + app.getTargetBounty() );
		*/
	}
	public void parseComplete(DefaultHandler handler, Boolean result) {
		dialog.hide();
		MyDefaultHandler mdh = (MyDefaultHandler)handler;
		app.setTargetName(mdh.assassin.returnTarget());
		app.setTargetBounty(mdh.targetAssassin.returnBounty());
	    
	    name.setText( "Current Target: " + app.getTargetName() );
	    currentBounty.setText("Target Bounty: " + app.getTargetBounty());
	    //name.setTag("Target Bounty: " + app.getTargetBounty());
	    
		// TODO Auto-generated method stub	
	}
	
	public AssassinApp returnApp()
	{
		return app;
	}
}
