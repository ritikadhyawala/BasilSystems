package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.basilsystems.data.Device;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ModeActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;
	
	private String PLACE_SELECTED = "";
	
	private ActionBar actionBar;
	
	private List<Device> devices = new ArrayList<Device>();
//	private String[] actions = {"Refresh", "Add Device", "Device Delete"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.bottom_tabs);
 
		getAllDevices();
  				
		setTabs();
		
		
//		ActionBar actionBar = getActionBar();
//		
//		
//		
//		 /** Create an array adapter to populate dropdownlist */
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, actions);
// 
//        /** Enabling dropdown list navigation for the action bar */
//        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
//        
// 
//        /** Defining Navigation listener */
//        ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {
// 
//            @Override
//            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
//                Toast.makeText(getBaseContext(), "You selected : " + actions[itemPosition]  , Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        };
// 
//        /** Setting dropdown items and item navigation listener for the actionbar */
//        actionBar.setListNavigationCallbacks(adapter, navigationListener);
//    

	}

	private void setTabs()
	{
		if(devices.size() > 0){
			addTab("Home", R.drawable.tab_home, ListDevicesActivity.class);
		}else{
			addTab("Home", R.drawable.tab_home, NoDeviceActivity.class);
		}
		addTab("Modes", R.drawable.tab_modes, ListModeActivity.class);

		addTab("Settings", R.drawable.tab_settings, SettingsActivity.class);

		mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_indicator);
		mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.tab_indicator);
		mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.tab_indicator);
		mTabHost.getTabWidget().setStripEnabled(false);
		mTabHost.getTabWidget().setDividerDrawable(null);	
	}

	private void getAllDevices(){
		
		DatabaseHandler dbHAndler = DatabaseHandler.getInstance(getApplicationContext());
		if(dbHAndler != null){
			PLACE_SELECTED = (String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
			devices = dbHAndler.getAllDevices(PLACE_SELECTED);
		}
		
	}
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost, false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		String place = (String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
		title.setText(DatabaseHandler.getInstance(getApplicationContext()).getPlaceName(place));
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		mTabHost.addTab(mTabHost.newTabSpec(labelId).setIndicator("", getResources().getDrawable(drawableId)),
				c, null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_menu, menu);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		String place = (String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
		actionBar.setTitle(DatabaseHandler.getInstance(getApplicationContext()).getPlaceName(place));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	        case R.id.refresh:
	            // Red item was selected
	            return true;
	        case R.id.add_device:
	        	Intent intent = new Intent(ModeActivity.this, AddDeviceActivity.class);
		        startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
		}
	}

}
