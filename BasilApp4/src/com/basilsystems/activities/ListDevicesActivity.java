package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.basilsystems.adapter.DeviceAdapter;
import com.basilsystems.data.Device;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ListDevicesActivity extends Fragment  implements LoaderManager.LoaderCallbacks<List<Device>> {

	DeviceAdapter deviceAdapter;
	
	private GridView grid_view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.home_grid_layout, null, false);
		grid_view = (GridView)view.findViewById(R.id.gridView1);	    
	    	deviceAdapter = new DeviceAdapter(this.getActivity());
	    	grid_view.setAdapter(deviceAdapter);
	    	
		 // Start out with a progress indicator.

	        // Prepare the loader.  Either re-connect with an existing one,
	        // or start a new one.
	    	getLoaderManager().initLoader(0, null, this);
	    	return view;
	    
	  }
	
	
     @Override
     public Loader<List<Device>> onCreateLoader(int arg0, Bundle arg1) {
         System.out.println("DataListFragment.onCreateLoader");
         return new DataListLoader(getActivity());
     }

     @Override
     public void onLoadFinished(Loader<List<Device>> arg0, List<Device> data) {
         deviceAdapter.setData(data);
         System.out.println("DataListFragment.onLoadFinished");
         // The list should now be shown.
//         if (isResumed()) {
//             setListShown(true);
//         } else {
//             setListShownNoAnimation(true);
//         }
     }

     @Override
     public void onLoaderReset(Loader<List<Device>> arg0) {
         deviceAdapter.setData(null);
     }
     
     public static class DataListLoader extends AsyncTaskLoader<List<Device>> {
         
         List<Device> mModels;
  
         public DataListLoader(Context context) {
             super(context);
         }
  
         @Override
         public List<Device> loadInBackground() {
             System.out.println("DataListLoader.loadInBackground");
              
             DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
             if(dbHandler != null){
            	 String place = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
            	 List<Device> allDevices = dbHandler.getAllDevices(place);
            	 Device dumb_device = new Device("", "", 4);
            	 dumb_device.setName("All appliances");
            	 allDevices.add(0, dumb_device);
            	 dumb_device =  new Device("", "", 5);
            	 dumb_device.setName("Add Clover Board");
            	 allDevices.add(dumb_device);
            	 return allDevices;
             }
             return new ArrayList<Device>();
         }
          
        /**
          * Called when there is new data to deliver to the client.  The
          * super class will take care of delivering it; the implementation
          * here just adds a little more logic.
          */
         @Override public void deliverResult(List<Device> listOfData) {
             if (isReset()) {
                 // An async query came in while the loader is stopped.  We
                 // don't need the result.
                 if (listOfData != null) {
                     onReleaseResources(listOfData);
                 }
             }
             List<Device> oldApps = listOfData;
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
         @Override public void onCanceled(List<Device> apps) {
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
         protected void onReleaseResources(List<Device> apps) {}
          
     }

	} 

