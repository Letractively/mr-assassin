package com.badideastudios.mrassassin;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoneyListAdapter extends BaseAdapter
{
    private ArrayList<AssassinObj> list;
	private LayoutInflater mInflater;
 
    public MoneyListAdapter(Context context, ArrayList<AssassinObj> list) 
    {
        mInflater = LayoutInflater.from(context);
        this.list = list;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        if (convertView == null) 
        {
            convertView = mInflater.inflate(R.layout.stat_list_item, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.stat_title_text);
        TextView sub = (TextView) convertView.findViewById(R.id.stat_summary_text);
 
        AssassinObj assassin = list.get(position);
        title.setText( assassin.returnTag() );
        sub.setText( "$" + Integer.toString( assassin.returnMoney() ) );
        sub.setTextColor(Color.YELLOW);
        return convertView;
    }
 
    public int getCount() 
    {
        return list.size();
    }
 
    public Object getItem(int arg0) 
    {
        return list.get(arg0);
    }
 
    public long getItemId(int arg0) 
    {
        return arg0;
    }
}
