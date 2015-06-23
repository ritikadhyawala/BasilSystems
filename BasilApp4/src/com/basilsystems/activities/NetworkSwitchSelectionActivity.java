package com.basilsystems.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.MenuItem;

import com.basilsystems.adapter.NetworkSwitchSelectionAdapter;
import com.basilsystems.data.Appliance;
import com.basilsystems.data.ApplianceIndex;
import com.basilsystems.util.DatabaseHandler;

public class NetworkSwitchSelectionActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<List<ApplianceIndex>> {

	private NetworkSwitchSelectionAdapter networkSwitchSelectionAdapter;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Select any Appliance");

		getListView().setBackgroundResource(R.drawable.blurred_background);
		getListView().setDividerHeight(7);
		networkSwitchSelectionAdapter = new NetworkSwitchSelectionAdapter(this);
		setListAdapter(networkSwitchSelectionAdapter);

		// Prepare the loader. Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public Loader<List<ApplianceIndex>> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<ApplianceIndex>> arg0, List<ApplianceIndex> data) {
		networkSwitchSelectionAdapter.setData(data);
		System.out.println("DataListFragment.onLoadFinished");
		// The list should now be shown.

		// TODO: Handle for activity being resumed
		// if (isResumed()) {
		// setListShown(true);
		// } else {
		// setListShownNoAnimation(true);
		// }
	}

	@Override
	public void onLoaderReset(Loader<List<ApplianceIndex>> arg0) {
		networkSwitchSelectionAdapter.setData(null);
	}

	public static class DataListLoader extends AsyncTaskLoader<List<ApplianceIndex>> {

		List<ApplianceIndex> mModels;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public List<ApplianceIndex> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			List<ApplianceIndex> allAppliancesIndexes = new ArrayList<ApplianceIndex>();
			DatabaseHandler dbHandler = DatabaseHandler
					.getInstance(getContext());
			if(dbHandler != null){
				List<Appliance> allAppliances = dbHandler.getAllAppliances();
				if(allAppliances.size() > 0){
					for(int i = 0; i < allAppliances.size() ; i++){
						allAppliancesIndexes.add(new ApplianceIndex(allAppliances.get(i).getParentID() + allAppliances.get(i).getApplianceName(), allAppliances.get(i).getAppliance_id()));
					}
				}
			}
			return allAppliancesIndexes;
		}

		/**
		 * Called when there is new data to deliver to the client. The super
		 * class will take care of delivering it; the implementation here just
		 * adds a little more logic.
		 */
		@Override
		public void deliverResult(List<ApplianceIndex> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped. We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<ApplianceIndex> oldApps = listOfData;
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
		@Override
		protected void onStartLoading() {
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
		@Override
		protected void onStopLoading() {
			// Attempt to cancel the current load task if possible.
			cancelLoad();
		}

		/**
		 * Handles a request to cancel a load.
		 */
		@Override
		public void onCanceled(List<ApplianceIndex> apps) {
			super.onCanceled(apps);

			// At this point we can release the resources associated with 'apps'
			// if needed.
			onReleaseResources(apps);
		}

		/**
		 * Handles a request to completely reset the Loader.
		 */
		@Override
		protected void onReset() {
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
		 * Helper function to take care of releasing resources associated with
		 * an actively loaded data set.
		 */
		protected void onReleaseResources(List<ApplianceIndex> apps) {
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			// Red item was selected
			return true;
		case R.id.add_device:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
