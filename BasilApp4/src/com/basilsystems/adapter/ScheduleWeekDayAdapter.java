package com.basilsystems.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.basilsystems.activities.R;

public class ScheduleWeekDayAdapter extends ArrayAdapter<String> {
    
	private final LayoutInflater mInflater;
 
    public ScheduleWeekDayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(String[] data) {
        clear();
        if (data != null) {
            for (String appEntry : data) {
                add(appEntry);
            }
        }
    }
 
    
    /**
     * Populate new items in the list.
     */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;
 
        if (convertView == null) {
            view = mInflater.inflate(R.layout.schedular_weekdays_list_item, parent, false);
        } else {
            view = convertView;
        }
        String item = getItem(position);
        
        
        CheckBox checkBox = (CheckBox)view.findViewById(R.id.week_name);
		checkBox.setText(item);
		
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					//Add to the database here
				}
			}
		});
        
        
 
        
        return view;
    }
} 
