package com.badideastudios.mrassassin;

//import android.app.Activity;
import android.os.Bundle;
import android.content.*;
import android.content.res.Resources;
import android.widget.*;

public class TabWidget extends android.app.TabActivity 
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    Resources res = getResources(); // Resource object to get Drawable Objects
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, RadarActivity.class);

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("radar").setIndicator("Radar",
	                      res.getDrawable(R.drawable.ic_tab_radar))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, TargetActivity.class);
	    spec = tabHost.newTabSpec("target").setIndicator("Target",
	                      res.getDrawable(R.drawable.ic_tab_target))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, StatActivity.class);
	    spec = tabHost.newTabSpec("stats").setIndicator("Stats",
	                      res.getDrawable(R.drawable.ic_tab_stats))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}

}
