package com.basilsystems.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ServerConnectionService extends Service {

	// Binder given to clients
		private final IBinder mBinder = new LocalBinder();
		public static LocalControlService localControlService = null;


		/**
		 * Class used for the client Binder.  Because we know this service always
		 * runs in the same process as its clients, we don't need to deal with IPC.
		 */
		public class LocalBinder extends Binder {
			public ServerConnectionService getService() {
				// Return this instance of LocalService so clients can call public methods
				//TODO : check for internet connection and server availability
				if(localControlService == null){
					localControlService = new LocalControlService(getApplicationContext());
				}
				return ServerConnectionService.this;
			}
		}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	// function connect to socketConnectionManager
	public void connectSocket(){
		
	}
	// function disconnect websocket
	// function sendMessage
	// function receiveMessage
	// 

}
