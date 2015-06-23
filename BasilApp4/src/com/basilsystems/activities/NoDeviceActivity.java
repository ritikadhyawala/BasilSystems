package com.basilsystems.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class NoDeviceActivity extends Fragment {
	
	private Button add_device_button;
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		 View view = inflater.inflate(R.layout.no_device_layout, container, false);
		 
		 add_device_button = (Button)view.findViewById(R.id.goto_add_device);
		 
		 add_device_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent placesIntent = new Intent(getActivity(), AddDeviceActivity.class);
		        startActivity(placesIntent);
				
			}
		});
	        return view;
	    }


}
