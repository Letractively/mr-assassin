package com.badideastudios.mrassassin;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CreditsActivity extends ListActivity
{
	static final String[] contributors = new String[] 
			{ "Bad Idea Studios:", "Vincent Kocks", "Matthew Leary", "Andrew Mauney",
			  "Kedric Sewell", "Icon Artists:", "Martina Šmejkalová", "Free App Icons" };

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	
		setListAdapter(new ArrayAdapter<String>(this, R.layout.credit_view,contributors));
	
		ListView creditList = getListView();
		creditList.setTextFilterEnabled(true);
	
		/*creditList.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
			    // When clicked, show a toast with the TextView text
			    Toast.makeText(getApplicationContext(),
				((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});*/
	
	}
}
