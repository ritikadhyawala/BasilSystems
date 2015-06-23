package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.basilsystems.adapter.ModeApplianceAdapter;
import com.basilsystems.data.Appliance;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.Util;

public class ListModeApplianceActivity extends ListActivity  {

	private ModeApplianceAdapter applianceAdapter;
	
	private String device_name;
	boolean mBound = false;
	
	
	@Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    applianceAdapter = new ModeApplianceAdapter(this);
	    applianceAdapter.setData(getApplianceData());
	    getListView().setAdapter(applianceAdapter);
	}
	
//	@Override
//	public void onDestroy() {
//	    super.onDestroy();
//	    
//	    for(int i = 0 ; i < applianceAdapter.getCount() ; i++){
//	    	View view = getListView().getChildAt(i);
//	    	Appliance appliance = applianceAdapter.getItem(i);
//	    	appliance.setAppliance_id(applianceAdapter.getItem(i).getAppliance_id());
//	    	
//	    	final ToggleButton autoButton = (ToggleButton)view.findViewById(R.id.autoButton);
//			
//			int status;
//			if(appliance.getIsSlider()){
//				SeekBar seekBar = (SeekBar)view.findViewById(R.id.statusBar);
//				status = seekBar.getProgress();
//			}else{
//				Switch button_switch = (Switch)view.findViewById(R.id.appliance_button);
//				if(button_switch.isChecked()){
//					status = 1;
//				}else{
//					status = 0;
//				}
//			}
//             appliance.setStatus(status);
//             appliance.setIsAuto(autoButton.isChecked());
//	    	 DatabaseHandler.getInstance(getApplicationContext()).setModeAppliances(new ModeAppliance(appliance.getAppliance_id(), appliance.getParentID(), appliance.getIsAuto(), appliance.getIsSlider(), appliance.getStatus(), appliance.getIsOn()));
//	    }
//	}
//	
	
	
	private List<Appliance> getApplianceData() {

		DatabaseHandler dbHandler = DatabaseHandler.getInstance(getApplicationContext());
		if(dbHandler != null){
			List<Appliance> allAppliancesGivenDevice = new ArrayList<Appliance>();
//			List<Appliance> allAppliancesGivenDevice = dbHandler.getModeAppliances((String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.SELECTED_MODE_DEVICE, SharedPreferenceManager.STRING));
//		    if(allAppliancesGivenDevice.size()  == 0 ){
		    	allAppliancesGivenDevice = dbHandler.getAllAppliancesGivenDevice(Util.getSelectedStrings(Util.SELECTED_MODE_DEVICE, getApplicationContext()));
//		    }
			return allAppliancesGivenDevice;
		}
         return null;
	}

	
	
	 @Override
     public void onListItemClick(ListView l, View v, int position, long id) {
         // Insert desired behavior here.
         Log.i("DataListFragment", "Item clicked: " + id);
     }

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.appliances_menu, menu);
			
			ActionBar actionBar = getActionBar();
			
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(device_name);
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
		        case R.id.rename:
		            // Red item was selected
		            return true;
		        case R.id.miscellaneous:
		            // Green item was selected
		            return true;
		        default:
		            return super.onOptionsItemSelected(item);
			}
		}
		

	} 


