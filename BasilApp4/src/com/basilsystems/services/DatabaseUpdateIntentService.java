package com.basilsystems.services;

import com.basilsystems.util.Util;

import android.app.IntentService;
import android.content.Intent;

public class DatabaseUpdateIntentService extends IntentService {

	public DatabaseUpdateIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {

		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(Util.APPLIANCE_BROADCAST_ACTION);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("", "ritika");
		sendBroadcast(broadcastIntent);
	}

}
