package com.basilsystems.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basilsystems.activities.ListModeDeviceActivity1;
import com.basilsystems.activities.R;
import com.basilsystems.custom.views.CustomRadioButton;
import com.basilsystems.data.Mode;
import com.basilsystems.services.MqttService;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ModeAdapter extends ArrayAdapter<Mode> {
    
	private final LayoutInflater mInflater;
	private MqttService mqttService;
 
    public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	public ModeAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(List<Mode> data) {
        clear();
        if (data != null) {
            for (Mode appEntry : data) {
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
             String selected_mode = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_MODE, SharedPreferenceManager.STRING);
            CustomRadioButton radioButton = (CustomRadioButton)view.findViewById(R.id.radio_button);
			if(selected_mode == null || selected_mode.isEmpty()){
            	SharedPreferenceManager.getInstance(getContext()).setPreference(Util.SELECTED_MODE, getItem(0).getMode_name(), SharedPreferenceManager.STRING);
            	selected_mode = getItem(0).getMode_name();
			}	
			if(getItem(position).getMode_name().equals(selected_mode)){
        		radioButton.setChecked(true);
        	}else{
        		radioButton.setChecked(false);
        	}
			
			radioButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewGroup mode_list = (ViewGroup)view.getParent();
				    mode_list.invalidate();
				}
			});
			
			LinearLayout linear_layout = (LinearLayout)view.findViewById(R.id.mode_linear_layout);
			linear_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(), ListModeDeviceActivity1.class);
			         SharedPreferenceManager.getInstance(getContext()).setPreference(Util.SELECTED_MODE, v.getTag() , SharedPreferenceManager.STRING);
			         getContext().startActivity(intent);
//				
					
				}
			});
			final TextView radio_text = (TextView)view.findViewById(R.id.radio_text);
			radio_text.setText(getItem(position).getMode_name());
        
			radioButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SharedPreferenceManager.getInstance(getContext()).setPreference(Util.SELECTED_MODE, radio_text.getText(), SharedPreferenceManager.STRING);
					notifyDataSetChanged();
					Util.showToast(getContext(), "The theme is set");
					JSONObject mode_object = new JSONObject();
					if("Away".equals(radio_text.getText())){
						try {
							mode_object.put("T", "A");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mqttService.publishMessage("mac1", mode_object.toString().getBytes());
					}else if("Night".equals(radio_text.getText())){
						try {
							mode_object.put("T", "N");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						mqttService.publishMessage("mac1", mode_object.toString().getBytes());					}
					
				}
			});
        return view;
    }
} 
