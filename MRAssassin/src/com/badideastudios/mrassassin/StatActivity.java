package com.badideastudios.mrassassin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StatActivity extends Activity 
{
	private enum categories { TOP_KILLERS, TOP_MONEY, TOP_BOUNTY };
	private TextView categoryText;
	private int currentCategory;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        categoryText = (TextView) findViewById(R.id.category_text);
        currentCategory = 0;
	}
	
	/** Handle left and right button presses*/
    
    public void left_button(View v)
    {
    }
    
    public void right_button(View v)
    {
    	
    }

    public String print_category()
    {
    	return "";
    }
}
