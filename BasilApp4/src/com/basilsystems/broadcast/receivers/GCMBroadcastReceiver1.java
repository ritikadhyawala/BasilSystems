///*package com.basilsystems.broadcast.receivers;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.basilsystems.application.BasilApplication;
//import com.basilsystems.data.DBVersions;
//import com.basilsystems.data.Mode;
//import com.basilsystems.data.ModeDevice;
//import com.basilsystems.util.SharedPreferenceManager;
//import com.basilsystems.util.Util;
//
//public class GCMBroadcastReceiver1 extends BroadcastReceiver {
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//
//		//0. Check for socketConnection
//		// if the socket is connected
//		if(intent.getExtras().containsKey(Util.CONNECTION)){
//			String text = intent.getStringExtra(Util.CONNECTION);
//			if(Boolean.parseBoolean(text)){
//				Util.showToast(context, "Connected to server");
//				// send the request to check db version to the server
//				String dbVersion = (String)SharedPreferenceManager.getInstance(context).getPreferenceValue(Util.DB_VERSION, SharedPreferenceManager.STRING);
//				if(dbVersion == null){
//					String dbVersionString = BasilApplication.getInstance().gson.toJson(new DBVersions());
//					SharedPreferenceManager.getInstance(context).setPreference(Util.DB_VERSION, dbVersionString, SharedPreferenceManager.STRING);
//				}else{
//					try {
//						BasilApplication.getInstance().mService.sendMessage("DBVersion", new JSONArray(dbVersion));
//					} catch (JSONException e) {
//						Log.d("DBVersion", "Cannot convert dbVersion format to jsonArray");
//						e.printStackTrace();
//					}					
//				}
//			}
//		}
//		// Check for db timestamp and sync
//		//   if yes send broadcast to rest of the activities
//		else if(intent.getExtras().containsKey(Util.DB_UPDATE)){
//			String dbUpdateText = intent.getStringExtra(Util.DB_UPDATE);
//			String dbVersion = intent.getStringExtra(Util.DB_VERSION);
//			DBVersions dbVersionObject = BasilApplication.getInstance().gson.fromJson(dbVersion, DBVersions.class);
//			try {
//				JSONObject dbUpdateData = new JSONObject(dbUpdateText);
//				if(dbUpdateData.has("m")){
//					dbVersionObject.setMode(dbVersionObject.getMode());
//					JSONArray modeJsonArray = (JSONArray)dbUpdateData.get("m");
//					if(modeJsonArray.length() > 0){
//						for(int i=0; i<modeJsonArray.length() ; i++){
//							JSONObject modeJsonObject = modeJsonArray.getJSONObject(i);
//							BasilApplication.getInstance().dbHandler.addMode(new Mode(modeJsonObject.getString("name"), modeJsonObject.getString("place_id"), modeJsonObject.getString("mode_id")));
//						}
//					}
//				}
//				if(dbUpdateData.has("md")){
//					dbVersionObject.setModeDevice(dbVersionObject.getModeDevice());
//					JSONArray modeDeviceJsonArray = (JSONArray)dbUpdateData.get("md");
//					if(modeDeviceJsonArray.length() > 0){
//						for(int i=0; i<modeDeviceJsonArray.length() ; i++){
//							JSONObject modeDeviceJsonObject = modeDeviceJsonArray.getJSONObject(i);
//							BasilApplication.getInstance().dbHandler.setModeDevices(new ModeDevice(modeDeviceJsonObject.getString("device_id"), modeDeviceJsonObject.getString("mode_id")));
//						}
//					}
//				}
//				if(dbUpdateData.has("ma")){
//					dbVersionObject.setModeAppliance(dbVersionObject.getModeAppliance());
//				}
//				if(dbUpdateData.has("pn")){
//					dbVersionObject.setPlaceName(dbVersionObject.getPlaceName());
//				}
//				if(dbUpdateData.has("dn")){
//					dbVersionObject.setDeviceName(dbVersionObject.getDeviceName());
//				}
//				if(dbUpdateData.has("an")){
//					dbVersionObject.setApplianceName(dbVersionObject.getApplianceName());
//				}
//				if(dbUpdateData.has("p")){
//					dbVersionObject.setPlace(dbVersionObject.getPlace());
//				}
//				if(dbUpdateData.has("d")){
//					dbVersionObject.setDevice(dbVersionObject.getDevice());
//				}
//				if(dbUpdateData.has("a")){
//					dbVersionObject.setAppliance(dbVersionObject.getAppliance());
//					JSONArray applianceJsonArray = (JSONArray)dbUpdateData.get("a");
//					if(applianceJsonArray.length() > 0){
//						for(int i=0; i<applianceJsonArray.length() ; i++){
//							JSONObject applianceJsonObject = applianceJsonArray.getJSONObject(i);
//							BasilApplication.getInstance().dbHandler.updateAppliance(applianceJsonObject.getString("appliance_id"), applianceJsonObject);
//						}
//					}
//				}
//				SharedPreferenceManager.getInstance(context).setPreference(Util.DB_VERSION, BasilApplication.getInstance().gson.toJson(dbVersionObject), SharedPreferenceManager.STRING);} catch (JSONException e) {
//					Log.d("Db Update", "The string from the server cannot be converted to json");
//					e.printStackTrace();
//				}
//		}
//
//		//1. Check for change in appliance status
//		// if yes broadcast it
//	}
//}
