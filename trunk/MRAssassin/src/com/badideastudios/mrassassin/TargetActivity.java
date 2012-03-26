package com.badideastudios.mrassassin;

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
	    
		MyDefaultHandler mdh = new MyDefaultHandler();
		XMLRetrievalClass XMLrc = new XMLRetrievalClass(this, mdh);
		XMLrc.execute();
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
		app.setTargetName(mdh.assassinName);
	    
	    name.setText( "Current Target: " + app.getTargetName() );
	    
		// TODO Auto-generated method stub	
	}
}
