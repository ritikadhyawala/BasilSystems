package com.basilsystems.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class NotificationPendingIntent extends Activity {

	private TextView notif_message;
	private String message;
	private Button callButton;
	private Button dismiss;
	private Vibrator v;

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notification_page_layout);

	    mNotificationManager = (NotificationManager)
				this.getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, NotificationPendingIntent.class), 0);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Emergency")
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText("Clover Board"))
		.setContentText("Emergency!!");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());



		notif_message = (TextView)findViewById(R.id.notif_message);
		callButton = (Button)findViewById(R.id.call);
		dismiss = (Button)findViewById(R.id.dismiss);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();

		if(extras != null){
			if(extras.containsKey("case")){
				message = (String)extras.get("case");
				if("fire".equals(message)){
					message = "Fire has broke out!";
				}else if("lpg_gas".equals(message)){
					message = "LPG gas has leaked out!";
				}else if("intrusion".equals(message)){
					message = "Someone in your house!";
				}
				notif_message.setText(message);
			}
		}

		v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds

		v.vibrate(5000);

		callButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			    mNotificationManager.cancel(NOTIFICATION_ID);
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:8884503432"));
				startActivity(callIntent);
			}
		});

		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mNotificationManager.cancel(NOTIFICATION_ID);
				v.cancel();
				finish();
			}
		});
	}

}
