package com.basilsystems.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.basilsystems.util.DatabaseHandler;
import com.google.gson.Gson;

public class BasilApplication extends Application {

	private boolean mBound = false;
	public Gson gson = null;
	public DatabaseHandler dbHandler = null;

	private static BasilApplication basilApplicaton = null;

	public static BasilApplication getInstance(){
		return basilApplicaton;
	}



	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		basilApplicaton = this;

		gson = new Gson();
		dbHandler = DatabaseHandler.getInstance(getApplicationContext());
		
//		String tokenValue = (String)SharedPreferenceManager.getInstance(getApplicationContext()).getPreferenceValue(Util.TOKEN, SharedPreferenceManager.STRING);
//		if(tokenValue != null && !tokenValue.isEmpty()){
//			// 0. open ListPlaceActivity
//			// 1. Check internet connection
//
//			if(isConnectingToInternet()){
//				// if yes
//				// Start SocketCOnnection  
//				mService.connectFirstTimeSocket(tokenValue);
//			}
//		}else{
//			// if no token 
//			// open LoginPage
//		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}

}
