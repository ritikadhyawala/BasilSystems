package com.basilsystems.broadcast.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.basilsystems.activities.NotificationPendingIntent;


public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle intentExtras = intent.getExtras();
		if(intentExtras != null){
			if(intentExtras.containsKey("message")){
				showNotificationDialog(context, "intrusion");
			}
		}
	}

	private void showNotificationDialog(Context context, String message) {
		Intent trIntent = new Intent("android.intent.action.MAIN");
		trIntent.setClass(context, NotificationPendingIntent.class);
		trIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		trIntent.putExtra("case", message );
		context.startActivity(trIntent);
	}

}
