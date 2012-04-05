package com.badideastudios.mrassassin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
        TextView overview = (TextView) findViewById(R.id.overview_summary);
        TextView radar = (TextView) findViewById(R.id.radar_summary);
        TextView target = (TextView) findViewById(R.id.target_summary);
        TextView stats = (TextView) findViewById(R.id.stat_summary);
        
        overview.setText("Welcome to MRAssassin, new recruit!\nIn this game, you will be searching around in the real world to track down other assassins. "
        			+ "Each following section describes one of the tabs on your phone.\n");
        radar.setText("On this tab, the top half displays important status information regarding sensors and the game.\n" + 
        			"On the bottom half, the radar points towards your target at all times. As you get closer, the radar pointer will get larger." +
        			"When you reach kill distance, the radar will become a full circle. At that point, the assassinate button will appear. Press the button to kill the target.\n");
        target.setText("On this tab, information about your target is shown. The target's name and stats are shown for you to see.\n");
        stats.setText("On this tab, statistics are shown that rank all current assassins. You can press the arrows at the top in order to check the other categories.");
	}
	
	public void exit(View v) { finish(); }
}
