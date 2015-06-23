package com.basilsystems.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class SettingsActivity extends Fragment {
	
	boolean mBound = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  final View view = inflater.inflate(R.layout.settings_layout, null, false);
		  
		  Switch controlAccessSwitch = (Switch)view.findViewById(R.id.control_access_switch);
		  
		  controlAccessSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferenceManager.getInstance(getActivity()).setPreference(Util.ACCESS_INTERNET_CONTROL, String.valueOf(isChecked), SharedPreferenceManager.STRING);
				   
				if(isChecked){
//					new TCPConnection().execute();
//					 mService.getLocalControlService().establishTCPConnection();
				}
			}
		});
		  
			
			
           return view;	    
	  }
	
	
}


