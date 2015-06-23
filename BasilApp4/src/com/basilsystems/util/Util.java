package com.basilsystems.util;

import android.content.Context;
import android.widget.Toast;

public class Util {
	
//	public static final String URL = "http://cloverboard-servercode.jit.su/";
//	public static final String URL = "https://cloverboard-c9-ritikadhyawala.c9.io/";
//	public static final String URL = "http://192.168.1.102/";
	public static final String URL = "http://ec2-54-148-57-118.us-west-2.compute.amazonaws.com/";
	
//	public static final String URL = "http://54.68.61.214";
	public static final String CONNECTION = "connection";
	
	public static void showToast(Context context, String text){
		if(context != null && text != null && !text.isEmpty()){
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
		}
		
	}

	public static final String TOKEN = "token";
	public static final String RESP_BROADCAST_ACTION = "com.basilsystems.broadcast";
	public static final String APPLIANCE_BROADCAST_ACTION = "com.basilsystems.broadcast.appliance";
	public static final String LOGIN_ACTIVITY = "LoginActivity";
	public static final String FROM_ACTIVITY = "from_activity";
	public static final String SENDER_ID = "263514921368";
	public static final String GCM_REGID = "gcm_regid";
	public static final String UPDATE_DATA = "updateData";
	public static final String ADDPLACE = "addPlace";
	
	public static String PLACE = "place";
	public static String DEVICE = "device";
	
	public static String SELECTED_PLACE = "selected_place";
	public static String SELECTED_DEVICE = "selected_device";
	public static String SELECTED_APPLIANCE = "selected_appliance";
	
	public static String SELECTED_MODE = "selected_mode1";
	public static String SELECTED_MODE_DEVICE = "selected_mode_device";
	public static final String GET_USER = "getUser";
	public static final String EVENT2 = "event";
	public static final String MESSAGE = "message";
	public static final String DB_UPDATE = "db_update";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String CHANGE_APPLIANCE_STATUS = "change_appliance_status";
	
	public static String getSelectedStrings(String selection, Context context) {
		return (String)SharedPreferenceManager.getInstance(context).getPreferenceValue(selection, SharedPreferenceManager.STRING);
	}

	public static final String GET_USER_PLACES_NAMES = "getUserPlacesNames";
	public static final String GET_MODES = "getModes";
	public static final String UPDATE_NAME = "update_name";
	
	public static final int LIVING_ROOM = 0;
	public static final int BEDROOM = 1;
	public static final int KITCHEN = 2;
	public static final int STUDY_ROOM = 3;
	public static final int ALL_APPLIANCES = 4;
	public static final int ADD_DEVICE = 5;
	public static final String LIST_PLACES_ACTIVITY = "ListPlacesActivity";
	public static final String ACCESS_INTERNET_CONTROL = "AccessInternetControl";
	public static final String REMOTE_NAME = "remoteName";
	public static final String REMOTE_NAMES = "remote_names";
	public static final String GOT_RAW_CODE = "GotRawCode";

	public static final String IR_REMOTE = "irRemote";

	public static final String COM_BASILSYSTEMS_BROADCAST_REMOTE = "com.basilsystems.broadcast.remote";

	public static final String DB_VERSION = "DbVersion";
	
}
