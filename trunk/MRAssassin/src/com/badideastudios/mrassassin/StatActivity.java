package com.badideastudios.mrassassin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StatActivity extends Activity 
{
	private AssassinApp app;
	/* Pseudo-enumeration for the statistic types */
	private final int TOP_KILLERS = 0;
	private final int TOP_BOUNTY  = 1;
	private final int TOP_EARNERS = 2;
	private int currentCategory;
	private ListView statsList;
	private TextView categoryText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);
        app = (AssassinApp) getApplication();

        currentCategory = TOP_KILLERS;
        statsList = (ListView) findViewById(R.id.stat_list_view);
        categoryText = (TextView) findViewById(R.id.category_text);
        refreshPage();
	}
	
	/** Handle left and right button presses*/
    public void left_button(View v)
    {
    	if(currentCategory != TOP_KILLERS)
    		currentCategory--;
    	else
    		currentCategory = TOP_EARNERS;
        refreshPage();
    }
    
    public void right_button(View v)
    {
    	if(currentCategory != TOP_EARNERS)
    		currentCategory++;
    	else
    		currentCategory = TOP_KILLERS;
        refreshPage();
    }
    
    private String printTitle(int category)
    {
    	switch(category)
    	{
    	default:
    	case TOP_KILLERS:
    		return "Top Killers:";
    	case TOP_BOUNTY:
    		return "Top Bounty Hunters:";
    	case TOP_EARNERS:
    		return "Top Earners:";
    	}
    }
    
    private void refreshPage()
    {
        categoryText.setText( printTitle(currentCategory) );
    }
}
