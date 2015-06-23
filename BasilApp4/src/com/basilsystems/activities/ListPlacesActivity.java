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

import com.basilsystems.adapter.PlacesAdapter;
import com.basilsystems.data.Place;
import com.basilsystems.util.DatabaseHandler;

public class ListPlacesActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Place>> {

	private PlacesAdapter placeAdapter;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.places);

		getListView().setBackgroundResource(R.drawable.blurred_background);
		getListView().setDividerHeight(7);
		placeAdapter = new PlacesAdapter(this);
		//		List<Place> allPlaces = DatabaseHandler.getInstance(getApplicationContext()).getAllPlaces();
		//		allPlaces.add(new Place(""));
		//		placeAdapter.setData(allPlaces);
		//		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		//		        android.R.layout.simple_list_item_1, values);
		setListAdapter(placeAdapter);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);

	}

	@Override
	public void onBackPressed(){
//		Intent placeIntent = new Intent(ListPlacesActivity.this, StartActivity.class);
//		placeIntent.putExtra(Util.FROM_ACTIVITY, Util.LIST_PLACES_ACTIVITY);
//        startActivity(placeIntent);
		finish();
	}

	//	 @Override
	//     public void onListItemClick(ListView l, View v, int position, long id) {
	//         // Insert desired behavior here.
	//         Log.i("DataListFragment", "Item clicked: " + id);
	//         //TODO : change the destination activity to modeactivity
	//         Intent intent = new Intent(ListPlacesActivity.this, ModeActivity.class);
	//         SharedPreferenceManager preferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
	//         preferenceManager.setPreference(Util.SELECTED_PLACE, ((Place)getListAdapter().getItem(position)).getID(), SharedPreferenceManager.STRING);
	//		 startActivity(intent);
	//     }

	@Override
	public Loader<List<Place>> onCreateLoader(int arg0, Bundle arg1) {
		System.out.println("DataListFragment.onCreateLoader");
		return new DataListLoader(this);
	}

	@Override
	public void onLoadFinished(Loader<List<Place>> arg0, List<Place> data) {
		data.add(new Place(""));
		placeAdapter.setData(data);
		System.out.println("DataListFragment.onLoadFinished");
		// The list should now be shown.


		//TODO: Handle for activity being resumed
		//         if (isResumed()) {
		//             setListShown(true);
		//         } else {
		//             setListShownNoAnimation(true);
		//         }
	}

	@Override
	public void onLoaderReset(Loader<List<Place>> arg0) {
		placeAdapter.setData(null);
	}

	public static class DataListLoader extends AsyncTaskLoader<List<Place>>{


		List<Place> mModels;

		public DataListLoader(Context context) {
			super(context);
		}

		@Override
		public List<Place> loadInBackground() {
			System.out.println("DataListLoader.loadInBackground");

			DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
			if(dbHandler != null){
				// String place = (String)SharedPreferenceManager.getInstance(getContext()).getPreferenceValue(Util.SELECTED_PLACE, Util.STRING);
				List<Place> allPlaces = dbHandler.getAllPlaces();
				return allPlaces;
			}
			return new ArrayList<Place>();
		}

		/**
		 * Called when there is new data to deliver to the client.  The
		 * super class will take care of delivering it; the implementation
		 * here just adds a little more logic.
		 */
		@Override public void deliverResult(List<Place> listOfData) {
			if (isReset()) {
				// An async query came in while the loader is stopped.  We
				// don't need the result.
				if (listOfData != null) {
					onReleaseResources(listOfData);
				}
			}
			List<Place> oldApps = listOfData;
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
		@Override public void onCanceled(List<Place> apps) {
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
		protected void onReleaseResources(List<Place> apps) {}


	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh:
			// Red item was selected
			return true;
		case R.id.add_device:
			//	        	Intent intent = new Intent(ModeActivity.this, AddDeviceActivity.class);
			//		        startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}