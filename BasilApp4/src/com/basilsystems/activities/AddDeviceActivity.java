package com.basilsystems.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basilsystems.data.Appliance;
import com.basilsystems.data.Device;
import com.basilsystems.util.DatabaseHandler;                                                                      
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class AddDeviceActivity extends Activity {

	private static final String NETWORK_PASSWORD2 = "network_password";
	private static final String SSID2 = "ssid";
	
	
	private EditText device_name;
	private EditText ssid;
	private EditText network_password;
	private Button add_device_button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_device_layout);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.add_device);
		//	    actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(true);
		
		device_name = (EditText)findViewById(R.id.device_name);
		ssid = (EditText)findViewById(R.id.ssid);
		network_password = (EditText)findViewById(R.id.ssid_password);
		
		ssid.setText((String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(SSID2, SharedPreferenceManager.STRING));
		network_password.setText((String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(NETWORK_PASSWORD2, SharedPreferenceManager.STRING));
		
        add_device_button = (Button)findViewById(R.id.add_device_button);

        add_device_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(device_name.getText().toString().isEmpty()){
					Util.showToast(getApplicationContext(), "Device Name cannot be empty");
				}else{
					String selected_place = (String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
				    Device newDevice = new Device(device_name.getText().toString(), selected_place, 1);
					DatabaseHandler.getInstance(getApplicationContext()).setDevices(newDevice);
				    addAppliancesToDevice(newDevice);
				    Intent intent = new Intent(AddDeviceActivity.this, ModeActivity.class);
			        startActivity(intent);
				}
			}
		});
	}
	
	private void addAppliancesToDevice(Device device){
		addAppliance(device, "Fan", true);
		addAppliance(device, "Dimming Light", true);
		addAppliance(device, "Light", false);
		addAppliance(device, "AC", false);
		addAppliance(device, "Diffuser Lamp", false);
		
	}

	private void addAppliance(Device device, String applianceName, Boolean isSlider) {
		Appliance appliance = new Appliance( device.getDevice_id(), applianceName, 0, isSlider, false, true);
		DatabaseHandler.getInstance(getApplicationContext()).addApplianceToDevice(appliance);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.places_action_bar, menu);
		return true;
	}

	//NEW
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			Toast.makeText(this, "Action refresh selected", Toast.LENGTH_SHORT)
			.show();
			break;
		case R.id.action_settings:
			Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT)
			.show();
			break;

		default:
			break;
		}

		return true;
	}
}
