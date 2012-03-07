package com.badideastudios.mrassassin;

import android.R;
import android.R.string;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TargetActivity extends Activity {

	public String targetName = "Ezio";
	public int targetBounty = 500;
	/** Called when the activity is first created. */
	@Override

	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(com.badideastudios.mrassassin.R.layout.target);
	    
	    TextView name = new TextView(this);
	    name = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetName);
	    name.setText("Current Target: " + targetName);
	    
	    TextView currentBounty = new TextView(this);
	    currentBounty = (TextView)findViewById(com.badideastudios.mrassassin.R.id.targetBounty);
	    currentBounty.setText("Target Bounty: " + targetBounty);
	    

	}

}
