package com.basilsystems.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basilsystems.activities.ListModeApplianceActivity;
import com.basilsystems.activities.ListModeDeviceActivity1;
import com.basilsystems.activities.R;
import com.basilsystems.data.Device;
import com.basilsystems.data.ModeDevice;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ModeDeviceAdapter extends ArrayAdapter<Device> {
    
	private final LayoutInflater mInflater;
	
	private String selected_mode;
 
    public ModeDeviceAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        selected_mode = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_MODE, SharedPreferenceManager.STRING);
    }
 
    public void setData(List<Device> data) {
        clear();
        if (data != null) {
            for (Device appEntry : data) {
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
            view = mInflater.inflate(R.layout.mode_device_layout, parent, false);
        } else {
            view = convertView;
        }
        final Device item = getItem(position);
        ((TextView)view.findViewById(R.id.mode_device_item)).setText(item.getName());
        
        LinearLayout mode_device_layout = (LinearLayout)view.findViewById(R.id.mode_device_layout);
        
        mode_device_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("DataListFragment", "Item clicked: " + item.getName());
				Intent intent = new Intent(getContext(), ListModeApplianceActivity.class);
				SharedPreferenceManager.getInstance(getContext()).setPreference(Util.SELECTED_MODE_DEVICE, item.getDevice_id() , SharedPreferenceManager.STRING);
		        getContext().startActivity(intent);
				
			}
		});
          
        ImageView device_icon = (ImageView)view.findViewById(R.id.mode_device_image);
		 int icon = item.getIcon();
		 switch(icon){
		 case 0:
			 device_icon.setImageResource(R.drawable.living);
			 break;

		 case 1: 
			 device_icon.setImageResource(R.drawable.bedroom);
			 break;

		 case 2: 
			 device_icon.setImageResource(R.drawable.kitchen1);
			 break;

		 case 3: 
			 device_icon.setImageResource(R.drawable.study);
			 break;
			 
		 case 4: 
			 device_icon.setImageResource(R.drawable.all_appliances);
			 break;
			 
		 case 5: 
			 device_icon.setImageResource(R.drawable.plus_icon);
			 break;
		 }
        CheckBox select_mode = (CheckBox)view.findViewById(R.id.select_mode_device);
        List<ModeDevice> allModeDevices = DatabaseHandler.getInstance(getContext()).getAllModeDevices(selected_mode);
        if(allModeDevices != null && allModeDevices.size() > 0){
        	for(ModeDevice modeDevice : allModeDevices){
        		if(item.getDevice_id().equals(modeDevice.getDeviceID())){
					select_mode.setChecked(true);
        		}else{
        			select_mode.setChecked(false);
        		}
        	}
        }
        
        select_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					DatabaseHandler.getInstance(getContext()).setModeDevices(new ModeDevice(item.getDevice_id(), selected_mode));
				}else{
					DatabaseHandler.getInstance(getContext()).deleteModeDevice(new ModeDevice(item.getDevice_id(), selected_mode));
				}
			}
		});
        
 
        return view;
    }
} 
