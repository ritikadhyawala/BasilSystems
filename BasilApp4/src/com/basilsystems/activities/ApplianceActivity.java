package com.basilsystems.activities;

import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.basilsystems.adapter.ApplianceAdapter;
import com.basilsystems.callbacks.interfaces.OnApplianceStatusChangeListener;
import com.basilsystems.data.Appliance;
import com.basilsystems.services.MqttService;
import com.basilsystems.services.MqttService.LocalBinder;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

@SuppressLint("NewApi")
public class ApplianceActivity extends ListActivity {

	public ApplianceAdapter applianceAdapter;

	MqttService mService;
	boolean mBound = false;

	private ApplianceReceiver receiver;

	private String device_name;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey("device_name")) {
			device_name = bundle.getString("device_name");
		}

		getListView().setDividerHeight(4);
		getListView().setBackgroundResource(R.drawable.blurred_background);
		applianceAdapter = new ApplianceAdapter(this,
				new OnApplianceStatusChangeListener() {

					@Override
					public void onApplianceStatusChange(JSONObject message) {
							mService.publishMessage("mac1", message
									.toString().getBytes());

					}
				});
		List<Appliance> applianceData = getApplianceData();
		applianceAdapter.setData(applianceData);
		getListView().setDividerHeight(2);
		setListAdapter(applianceAdapter);

		// getListView().setBackground(getResources().getDrawable(R.drawable.blurred_background));

		
		receiver = new ApplianceReceiver();
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService
		Intent intent = new Intent(this, MqttService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		IntentFilter filter = new IntentFilter(Util.APPLIANCE_BROADCAST_ACTION);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
		unregisterReceiver(receiver);
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			LocalBinder binder = (LocalBinder) service;
			mService = (MqttService) binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	private List<Appliance> getApplianceData() {

		DatabaseHandler dbHandler = DatabaseHandler
				.getInstance(getApplicationContext());
		if (dbHandler != null) {
			List<Appliance> allAppliancesGivenDevice = dbHandler
					.getAllAppliancesGivenDevice((String) SharedPreferenceManager
							.getInstance(getApplicationContext())
							.getPreferenceValue(Util.SELECTED_DEVICE,
									SharedPreferenceManager.STRING));
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
		actionBar.setTitle(DatabaseHandler.getInstance(getApplicationContext())
				.getDeviceName(device_name));
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

	public class ApplianceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			List<Appliance> applianceData = getApplianceData();
//			applianceData.add(new Appliance("", "", 0, false, false, false));
			applianceAdapter.setData(applianceData);
			applianceAdapter.notifyDataSetChanged();

		}
	}
}
