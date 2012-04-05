package com.badideastudios.mrassassin;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class StatActivity extends Activity 
{
	private ArrayList<AssassinObj> testGroup;
	/* Pseudo-enumeration for the statistic types */
	private final int TOP_KILLERS = 0;
	private final int TOP_BOUNTY  = 1;
	private final int TOP_EARNERS = 2;
	private int currentCategory;
	private ListView statsList;
	private TextView categoryText;
	private TextView emptyText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        
        /** Set up views necessary to show our data */
        setContentView(R.layout.stats);
        statsList = (ListView) findViewById(R.id.stat_list_view);
        categoryText = (TextView) findViewById(R.id.category_text);
        emptyText = (TextView) findViewById(R.id.stat_list_empty);
        statsList.setEmptyView(emptyText);
        
        //TEST CODE: Remove when adding real data!
        testGroup = new ArrayList<AssassinObj>();
        testGroup.add( new AssassinObj("Altair", 10, 100, "Somebody", "00:00:00:00:00:00", 5000, 5, 10000) );
        testGroup.add( new AssassinObj("Travis Touchdown", 10, 100, "Somebody", "00:00:00:00:00:00", 2000, 2, 4000) );
        testGroup.add( new AssassinObj("Agent 47", 10, 100, "Somebody", "00:00:00:00:00:00", 4000, 4, 6000) );
        testGroup.add( new AssassinObj("Solid Snake", 10, 100, "Somebody", "00:00:00:00:00:00", 3000, 3, 5000) );
        testGroup.add( new AssassinObj("Sam Fisher", 10, 100, "Somebody", "00:00:00:00:00:00", 7000, 7, 9000) );

        /** Fill all of the data views with our data */
        currentCategory = TOP_KILLERS;
        statsList.setAdapter( new MoneyListAdapter(this, testGroup) );
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
        switch(currentCategory)
        {
    	default:
    	case TOP_KILLERS:
            statsList.setAdapter( new KillListAdapter(this, testGroup) );
    		break;
    	case TOP_BOUNTY:
            statsList.setAdapter( new BountyListAdapter(this, testGroup) );
    		break;
    	case TOP_EARNERS:
            statsList.setAdapter( new MoneyListAdapter(this, testGroup) );
    		break;
        }
    }
    
    /** Handle the menu button press and present options */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.options_menu, menu);
    	return true;
    }
    
    /** Handle callback when a menu option is selected */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch( item.getItemId() )
    	{
    	case R.id.settings:
    		Intent settingsActivity = new Intent(getBaseContext(), SettingsActivity.class);
    		startActivity(settingsActivity);
    		return true;
    	case R.id.help:
    		Intent helpActivity = new Intent(getBaseContext(), HelpActivity.class);
    		startActivity(helpActivity);
    		return true;
    	case R.id.exit:
    		finish();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
}
