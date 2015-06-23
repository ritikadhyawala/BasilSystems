package com.basilsystems.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.basilsystems.activities.R;
import com.basilsystems.custom.views.CustomRadioButton;
import com.basilsystems.data.ApplianceIndex;
import com.basilsystems.services.MqttService;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class NetworkSwitchSelectionAdapter extends ArrayAdapter<ApplianceIndex> {
    
	private final LayoutInflater mInflater;
	private MqttService mqttService;
 
    public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	public NetworkSwitchSelectionAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(List<ApplianceIndex> data) {
        clear();
        if (data != null) {
            for (ApplianceIndex appEntry : data) {
                add(appEntry);
            }
        }
    }
 
    
    /**
     * Populate new items in the list.
     */
    @Override public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
 
        if (convertView == null) {
            view = mInflater.inflate(R.layout.radiogroup_layout, parent, false);
        } else {
        	view = convertView;
        }
            String selected_appliance = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_MODE, SharedPreferenceManager.STRING);
            CustomRadioButton radioButton = (CustomRadioButton)view.findViewById(R.id.radio_button);
			if(selected_appliance == null || selected_appliance.isEmpty()){
            	selected_appliance = getItem(0).getApplianceName();
			}	
			if(getItem(position).equals(selected_appliance)){
        		radioButton.setChecked(true);
        	}else{
        		radioButton.setChecked(false);
        	}
			
			
			final TextView radio_text = (TextView)view.findViewById(R.id.radio_text);
			radio_text.setText(getItem(position).getApplianceName());
        
			radioButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					notifyDataSetChanged();
					ViewGroup mode_list = (ViewGroup)view.getParent();
				    mode_list.invalidate();
					Util.showToast(getContext(), "The Appliance is selected");
				}
			});
        return view;
    }
} 
