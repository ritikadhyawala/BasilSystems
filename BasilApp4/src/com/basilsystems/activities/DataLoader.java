package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

public class DataLoader extends AsyncTaskLoader<List<Object>>{


	List<Object> mModels;

	public DataLoader(Context context) {
		super(context);
	}

	@Override
	public List<Object> loadInBackground() {
		System.out.println("DataListLoader.loadInBackground");

		//			DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
		//			if(dbHandler != null){
		//				// String place = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_PLACE, Util.STRING);
		//				List<Place> allPlaces = dbHandler.getAllPlaces();
		//				return allPlaces;
		//			}
		return new ArrayList<Object>();
	}

	/**
	 * Called when there is new data to deliver to the client.  The
	 * super class will take care of delivering it; the implementation
	 * here just adds a little more logic.
	 */
	@Override public void deliverResult(List<Object> listOfData) {
		if (isReset()) {
			// An async query came in while the loader is stopped.  We
			// don't need the result.
			if (listOfData != null) {
				onReleaseResources(listOfData);
			}
		}
		List<Object> oldApps = listOfData;
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
	@Override public void onCanceled(List<Object> apps) {
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
	protected void onReleaseResources(List<Object> apps) {}


}

