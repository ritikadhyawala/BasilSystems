package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.basilsystems.adapter.ModeAdapter;
import com.basilsystems.data.Mode;
import com.basilsystems.services.MqttService;
import com.basilsystems.services.MqttService.LocalBinder;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

@SuppressLint("NewApi")
public class ListModeActivity extends Fragment implements LoaderManager.LoaderCallbacks<List<Mode>> {
	
	private ModeAdapter modeAdapter;
	private ListView mode_list ;
	
	MqttService mService;
	boolean mBound = false;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.list_mode_layout, null, false);
		
		mode_list = (ListView)view.findViewById(R.id.mode_listview);
		
		modeAdapter = new ModeAdapter(getActivity());
		mode_list.setAdapter(modeAdapter);
		
		getLoaderManager().initLoader(0, null, this);
		
//		mode_radiogroup = (RadioGroup)view.findViewById(R.id.mode_radio_group);
//	    getLoaderManager().initLoader(0, null, this);
//	    mode_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				Util.showToast(getActivity(), "radiobutton pressed");
//				RadioButton selectedMode = (RadioButton)view.findViewById(checkedId);
//				
//				SharedPreferenceManager.getInstance(getActivity()).setPreference(Util.SELECTED_MODE, selectedMode.getText().toString(), SharedPreferenceManager.STRING);
//			    radio_button_clicked = true;
//			    // TODO : Hard code values for away mode .. change it
//			    if("Away".equals(selectedMode.getText().toString())){
//			    	JSONObject appliance_object = new JSONObject();
//					try {
////						if(appliance.getIsSlider()){
////							if(appliance.getStatus() <30){
////								appliance.setStatus(0);
////							}else if(appliance.getStatus() >= 30 && appliance.getStatus() <60){
////								appliance.setStatus(1);
////							}else{
////								appliance.setStatus(2);
////							}
////						}
////						appliance_object.put("status", appliance.getStatus());
////						appliance_object.put("isAuto", appliance.getIsAuto());
////						appliance_object.put("_id", appliance.getAppliance_id().charAt(appliance.getAppliance_id().length()-1));
////						String deviceID = DatabaseHandler.getInstance(getApplicat 	ionContext()).getDeviceID(appliance.getParentID());
////						appliance_object.put("device_id", deviceID);
//						appliance_object.put("message", "m1");
//						appliance_object.put("device_id", "mac_addr1" );
//						JSONArray appliance_json = new JSONArray();
//						appliance_json.put(appliance_object);
////						SocketConnectionManager.client.emit(Util.CHANGE_APPLIANCE_STATUS, appliance_json);
//					}catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
////						
//			    }
//			}
//		});
//	    
	    
		return view;
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
        case R.id.add_mode:
            // Add mode
            return true;
        case R.id.delete_mode:
        	// Delete modew
            return true;
        default:
            return super.onOptionsItemSelected(item);
	}
		
	}
	
	
	
	

     @Override
     public Loader<List<Mode>> onCreateLoader(int arg0, Bundle arg1) {
         System.out.println("DataListFragment.onCreateLoader");
         return new DataListLoader(this.getActivity());
     }

     @Override
     public void onLoadFinished(Loader<List<Mode>> arg0, final List<Mode> data) {
    	 modeAdapter.setData(data);
     }

     @Override
     public void onLoaderReset(Loader<List<Mode>> arg0) {
//         modeAdapter.setData(null);
     }
     
     public static class DataListLoader extends AsyncTaskLoader<List<Mode>> {
         
         List<Mode> mModels;
  
         public DataListLoader(Context context) {
             super(context);
         }
  
         @Override
         public List<Mode> loadInBackground() {
             System.out.println("DataListLoader.loadInBackground");
              
             DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
             if(dbHandler != null){
            	 String place = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
            	 List<Mode> allModes = dbHandler.getModesOfPlace(place);
            	 return allModes;
             }
             return new ArrayList<Mode>();
         }
          
        /**
          * Called when there is new data to deliver to the client.  The
          * super class will take care of delivering it; the implementation
          * here just adds a little more logic.
          */
         @Override public void deliverResult(List<Mode> listOfData) {
             if (isReset()) {
                 // An async query came in while the loader is stopped.  We
                 // don't need the result.
                 if (listOfData != null) {
                     onReleaseResources(listOfData);
                 }
             }
             List<Mode> oldApps = listOfData;
             mModels = listOfData;
  
             if (isStarted()) {
                 // If the Loader is currently started, we can immediately
                 // deliver its results.
                 super.deliverResult(listOfData);
             }
  
             // At this point we can release the resources associated with
             // 'oldApps' if needed; now that the new result is delivered we
             // know that it is no longer in use.
             if (oldApps != null) {
                 onReleaseResources(oldApps);
             }
         }
  
         /**
          * Handles a request to start the Loader.
          */
         @Override protected void onStartLoading() {
             if (mModels != null) {
                 // If we currently have a result available, deliver it
                 // immediately.
                 deliverResult(mModels);
             }
  
  
             if (takeContentChanged() || mModels == null) {
                 // If the data has changed since the last time it was loaded
                 // or is not currently available, start a load.
                 forceLoad();
             }
         }
  
         /**
          * Handles a request to stop the Loader.
          */
         @Override protected void onStopLoading() {
             // Attempt to cancel the current load task if possible.
             cancelLoad();
         }
  
         /**
          * Handles a request to cancel a load.
          */
         @Override public void onCanceled(List<Mode> apps) {
             super.onCanceled(apps);
  
             // At this point we can release the resources associated with 'apps'
             // if needed.
             onReleaseResources(apps);
         }
  
         /**
          * Handles a request to completely reset the Loader.
          */
         @Override protected void onReset() {
             super.onReset();
  
             // Ensure the loader is stopped
             onStopLoading();
  
             // At this point we can release the resources associated with 'apps'
             // if needed.
             if (mModels != null) {
                 onReleaseResources(mModels);
                 mModels = null;
             }
         }
  
         /**
          * Helper function to take care of releasing resources associated
          * with an actively loaded data set.
          */
         protected void onReleaseResources(List<Mode> apps) {}
          
     }
     
     @Override
 	public void onStart() {
 		super.onStart();
 		// Bind to LocalService
 		Intent intent = new Intent(this.getActivity(), MqttService.class);
 		getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
 	}

 	@Override
 	public void onStop() {
 		super.onStop();
 		// Unbind from the service
 		if (mBound) {
 			getActivity().unbindService(mConnection);
 			mBound = false;
 		}
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
 			modeAdapter.setMqttService(mService);
 		}

 		@Override
 		public void onServiceDisconnected(ComponentName arg0) {
 			mBound = false;
 		}
 	};


	} 

