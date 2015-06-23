package com.basilsystems.activities;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.basilsystems.application.BasilApplication;
import com.basilsystems.data.Appliance;
import com.basilsystems.data.Device;
import com.basilsystems.data.Mode;
import com.basilsystems.data.ModeAppliance;
import com.basilsystems.data.ModeDevice;
import com.basilsystems.data.Place;
import com.basilsystems.data.Schedular;
import com.basilsystems.services.MqttService;
import com.basilsystems.services.MqttService.LocalBinder;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class StartActivity extends Activity {

	MqttService mService;
	boolean mBound = false;

	private StartActivityReceiver receiver;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		// new RegisterBackground().execute();

	}

	@Override
	protected void onStart() {
		super.onStart();
		
		startService(new Intent(StartActivity.this, MqttService.class));
		IntentFilter filter = new IntentFilter("getUserData");
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		receiver = new StartActivityReceiver();
		registerReceiver(receiver, filter);
		
		if (BasilApplication.getInstance().isConnectingToInternet()) {
			Intent intent = new Intent(this, MqttService.class);
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		} else {
			moveToListPlaces();
		}
		
	}

	private void moveToListPlaces() {
		if (SharedPreferenceManager.getInstance(getApplicationContext())
				.getPreferenceValue("isAppConfigured",
						SharedPreferenceManager.BOOLEAN) == null) {
			if (mService != null) {
				mService.publishMessage("fromApp", "getUser".getBytes());
			}
		} else {
			startActivity(new Intent(StartActivity.this,
					ListPlacesActivity.class));
		}
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
			mService = ((LocalBinder<MqttService>) service).getService();
			mService.rebroadcastReceivedMessages();
			mBound = true;

			moveToListPlaces();
			// if user is starting for the first time, place a boolean in shared
			// preference manager
			// do all the subscriptions

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	public class StartActivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getExtras().containsKey("getUser")) {
				String userData = intent.getExtras().getString("getUser");
				setDatabase(userData);
			} else if (intent.getExtras().containsKey("getModes")) {
				String userData = intent.getExtras().getString("getModes");
				setModesToDatabase(userData);
			}
			if (intent.getExtras().containsKey("getUserPlacesNames")) {
				String userData = intent.getExtras().getString(
						"getUserPlacesNames");
				setNamesToDatabase(userData);
			}
		}
	}

	public void setDatabase(String data) {
		try {
			DatabaseHandler db_handler = DatabaseHandler
					.getInstance(getApplicationContext());
			db_handler.onUpgrade(db_handler.getDatabase(), MODE_PRIVATE,
					MODE_PRIVATE);
			JSONArray place_array = new JSONArray(data);

			// JSONArray jsonArray = new JSONArray(data);
			//
			// JSONArray place_array = new JSONArray(
			// jsonArray.get(0).toString());

			// JSONArray place_array = user.getJSONArray("places");

			for (int i = 0; i < place_array.length(); i++) {
				JSONObject place_object = place_array.getJSONObject(i);
				Place place = new Place("place1");
				place.setID(place_object.getString("_id"));
				db_handler.setPlaces(place);
				JSONArray device_array = place_object.getJSONArray("devices");
				for (int j = 0; j < device_array.length(); j++) {
					JSONObject device_object = device_array.getJSONObject(j);
					Device device = new Device(device_object.getString("name"),
							place.getID(), device_object.getInt("icon"));
					device.setDevice_id(device_object.getString("_id"));
					db_handler.setDevices(device);
					JSONArray appliance_array = device_object
							.getJSONArray("appliances");
					for (int k = 0; k < appliance_array.length(); k++) {
						JSONObject appliance_object = appliance_array
								.getJSONObject(k);
						Appliance appliance = new Appliance(
								device.getDevice_id(),
								appliance_object.getString("name"),
								appliance_object.getInt("status"),
								appliance_object.getBoolean("isSlider"),
								appliance_object.getBoolean("isAuto"),
								appliance_object.getBoolean("isOn"));
						appliance.setAppliance_id(appliance_object
								.getString("_id"));
						db_handler.addApplianceToDevice(appliance);
						JSONObject schedule_object = appliance_object
								.getJSONObject("schedule");

						Schedular schedular = new Schedular(appliance.getID(),
								schedule_object.getBoolean("only_today"),
								schedule_object.getString("start_time"),
								schedule_object.getString("end_time"),
								schedule_object.getBoolean("is_repeat"),
								schedule_object.getInt("weekly"),
								schedule_object.getBoolean("is_auto"),
								schedule_object.getInt("status"));
						db_handler.setScheduling(schedular);
					}
				}
			}

			mService.publishMessage("fromApp", "getUserPlacesNames".getBytes());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setModesToDatabase(String data) {
		try {
			DatabaseHandler db_handler = DatabaseHandler
					.getInstance(getApplicationContext());
			// JSONArray jsonArray = new JSONArray(data);

			// JSONArray mode_array = (JSONArray) new JSONArray(((new JSONArray(
			// jsonArray.toString())).get(0)).toString());

			JSONArray mode_array = new JSONArray(data);
			// JSONArray place_array = user.getJSONArray("place_id");

			for (int i = 0; i < mode_array.length(); i++) {
				JSONObject mode_object = mode_array.getJSONObject(i);
				String mode_name = mode_object.getString("name");
				db_handler.addMode(new Mode(mode_name, mode_object
						.getString("place_id"), mode_object.getString("_id")));
				JSONArray device_array = mode_object.getJSONArray("devices");
				for (int j = 0; j < device_array.length(); j++) {
					JSONObject device_object = device_array.getJSONObject(j);
					db_handler.setModeDevices(new ModeDevice(device_object
							.getString("device_id"), mode_object
							.getString("_id")));
					JSONArray appliance_array = device_object
							.getJSONArray("appliances");
					for (int k = 0; k < appliance_array.length(); k++) {
						JSONObject appliance_object = appliance_array
								.getJSONObject(k);
						db_handler.setModeAppliances(new ModeAppliance(
								appliance_object.getString("_id"),
								device_object.getString("device_id"),
								appliance_object.getBoolean("isAuto"),
								appliance_object.getBoolean("isSlider"),
								appliance_object.getInt("status"),
								appliance_object.getBoolean("isOn")));
					}
				}
			}
			SharedPreferenceManager.getInstance(getApplicationContext())
					.setPreference("isAppConfigured", true,
							SharedPreferenceManager.BOOLEAN);

			startActivity(new Intent(StartActivity.this,
					ListPlacesActivity.class));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setNamesToDatabase(String data) {
		try {
			DatabaseHandler db_handler = DatabaseHandler
					.getInstance(getApplicationContext());
			// JSONArray jsonArray = new JSONArray(data);
			//
			// JSONArray place_array = (JSONArray) new JSONArray(((new
			// JSONArray(
			// jsonArray.toString())).get(0)).toString());

			// JSONArray place_array = user.getJSONArray("place_id");

			JSONArray place_array = new JSONArray(data);
			for (int i = 0; i < place_array.length(); i++) {
				JSONObject place_object = place_array.getJSONObject(i);
				String place_name = place_object.getString("place_name");
				db_handler.updatePlaceName(place_name,
						place_object.getString("place_id"));
				JSONArray device_array = place_object
						.getJSONArray("device_names");
				for (int j = 0; j < device_array.length(); j++) {
					JSONObject device_object = device_array.getJSONObject(j);
					db_handler.updateDeviceName(
							device_object.getString("device_name"),
							device_object.getString("device_id"));
					JSONArray appliance_array = device_object
							.getJSONArray("appliance_names");
					for (int k = 0; k < appliance_array.length(); k++) {
						JSONObject appliance_object = appliance_array
								.getJSONObject(k);
						db_handler.updateApplianceName(
								appliance_object.getString("appliance_name"),
								appliance_object.getString("appliance_id"));
					}
				}
			}
			mService.publishMessage("fromApp", "getModes".getBytes());

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	class RegisterBackground extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			GoogleCloudMessaging gcm = GoogleCloudMessaging
					.getInstance(getApplicationContext());
			String regid;
			try {
				regid = gcm.register(Util.SENDER_ID);

				JSONObject gcm_object = new JSONObject();
				gcm_object.put("gcm_regid", regid);
				gcm_object.put("type", "gcm_reg");
				mService.publishMessage("gcm_reg", regid.getBytes());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
}
