package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.basilsystems.adapter.ModeDeviceAdapter;
import com.basilsystems.data.Device;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ListModeDevicesActivity extends Fragment implements LoaderManager.LoaderCallbacks<List<Device>> {

	ModeDeviceAdapter modeDeviceAdapter;
	
	private GridView grid_view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.mode_device_grid_layout, null, false);
		grid_view = (GridView)view.findViewById(R.id.mode_device_grid);	    
		modeDeviceAdapter = new ModeDeviceAdapter(this.getActivity());
	    	grid_view.setAdapter(modeDeviceAdapter);
	    	
	    	grid_view.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> grid, View v,
						int position, long id) {
					if(position == 0){
						
					}else if(position == modeDeviceAdapter.getCount()-1){
						
					}else {
						
					}
					
				}
			});
		 // Start out with a progress indicator.

	        // Prepare the loader.  Either re-connect with an existing one,
	        // or start a new one.
	    	getLoaderManager().initLoader(0, null, this);
	    	return view;
	    
	  }
	
	
//	 @Override
//     public void onListItemClick(ListView l, View v, int position, long id) {
//         // Insert desired behavior here.
//         Log.i("DataListFragment", "Item clicked: " + id);
//         Intent intent = new Intent(ListModeDevicesActivity.this.getActivity(), ApplianceActivity.class);
//         String device_name = ((TextView)v.findViewById(R.id.list_item)).getText().toString();
//		SharedPreferenceManager.getInstance(getActivity()).setPreference(Util.SELECTED_DEVICE, place + "_" + device_name , SharedPreferenceManager.STRING);
//	     intent.putExtra("device_name", device_name);  
//         startActivity(intent);
//     }

     @Override
     public Loader<List<Device>> onCreateLoader(int arg0, Bundle arg1) {
         System.out.println("DataListFragment.onCreateLoader");
         return new DataListLoader(getActivity());
     }

     @Override
     public void onLoadFinished(Loader<List<Device>> arg0, List<Device> data) {
         modeDeviceAdapter.setData(data);
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
         modeDeviceAdapter.setData(null);
     }
     
     public static class DataListLoader extends AsyncTaskLoader<List<Device>> {
         
         List<Device> mModels;
  
         public DataListLoader(Context context) {
             super(context);
         }
  
         @Override
         public List<Device> loadInBackground() {
             System.out.println("DataListLoader.loadInBackground");
              
//              // You should perform the heavy task of getting data from 
//              // Internet or database or other source 
//              // Here, we are generating some Sample data
//  
//             // Create corresponding array of entries and load with data.
//             List<Devices> entries = new ArrayList<Devices>(5);
//             entries.add(new Devices("Java", "2"));
//             entries.add(new Devices("C++", "9"));
//             entries.add(new Devices("Python", "6"));
//             entries.add(new Devices("JavaScript", "10"));
             DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
             if(dbHandler != null){
            	 String place = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_PLACE, SharedPreferenceManager.STRING);
            	 List<Device> allDevices = dbHandler.getAllDevices(place);
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

