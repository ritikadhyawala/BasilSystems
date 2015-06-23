package com.basilsystems.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basilsystems.activities.ApplianceActivity;
import com.basilsystems.activities.ListDevicesActivity;
import com.basilsystems.activities.R;
import com.basilsystems.data.Device;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class DeviceAdapter extends ArrayAdapter<Device> {
	private final LayoutInflater mInflater;

	public DeviceAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	 @Override public View getView(final int position, View convertView, ViewGroup parent) {
		 final View view;

		 if (convertView == null) {																			
			 view = mInflater.inflate(R.layout.device_layout, parent, false);
		 } else {
			 view = convertView;
		 }

		 final Device item = getItem(position);
		 ((TextView)view.findViewById(R.id.list_item)).setText(item.getName());

		 LinearLayout deviceLayout = (LinearLayout)view.findViewById(R.id.device_layout);
		 deviceLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(position == 0){
					
				}else if(position == getCount()-1){
					
				}else {
					 Log.i("DataListFragment", "Item clicked: " + item.getName());
			         Intent intent = new Intent(getContext(), ApplianceActivity.class);
			         String device_id = item.getDevice_id();
					SharedPreferenceManager.getInstance(getContext()).setPreference(Util.SELECTED_DEVICE, device_id , SharedPreferenceManager.STRING);
				     intent.putExtra("device_name", device_id);  
			         getContext().startActivity(intent);
				}
				
			}
		});
		 ImageView device_icon = (ImageView)view.findViewById
		 (R.id.device_image);
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

		 Button option_button = (Button)view.findViewById(R.id.device_option_button);
         option_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                 final LinearLayout options_layout = (LinearLayout)view.findViewById(R.id.options_layout_device);
                 options_layout.setVisibility(View.VISIBLE);
                 
                 Button delete_button = (Button)view.findViewById(R.id.delete_device);
                 delete_button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
                          Builder builder = new Builder(getContext());
                          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                       // set title
              			alertDialogBuilder.setTitle("Are you sure?");
               
              			// set dialog message
              			alertDialogBuilder
              				.setMessage("The CloverBoard will be deleted and you will have to configure it again.")
              				.setCancelable(false)
              				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
              					public void onClick(DialogInterface dialog,int id) {
              						// if this button is clicked, close
              						// current activity
//              						MainActivity.this.finish();
              					}
              				  })
              				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
              					public void onClick(DialogInterface dialog,int id) {
              						// if this button is clicked, just close
              						// the dialog box and do nothing
              						dialog.cancel();
              					}
              				});
               
              				// create alert dialog
              				AlertDialog alertDialog = alertDialogBuilder.create();
               
              				// show it
              				alertDialog.show();
						}
					});
                 
                 options_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							options_layout.setVisibility(View.GONE);
						}
					});
					
				}
			});
		 return view;
	 }
} 
