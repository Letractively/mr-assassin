package com.badideastudios.mrassassin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StatActivity extends Activity 
{
	private enum Category { TOP_KILLERS, TOP_MONEY, TOP_BOUNTY };
	private TextView categoryText;
	private Category currentCategory;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
	    super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        currentCategory = Category.TOP_KILLERS;
        categoryText = (TextView) findViewById(R.id.category_text);
        categoryText.setText( currentCategory.name() );
	}
	
	/** Handle left and right button presses*/
    
    public void left_button(View v)
    {
    }
    
    public void right_button(View v)
    {
    	
    }
}
