package com.basilsystems.activities;

import com.basilsystems.data.Mode;
import com.basilsystems.data.Place;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.Util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlaceNameActivity extends Activity {

	private EditText place_name;
	private Button add_place_button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_place_layout);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(R.string.add_place_button);
		//	    actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(true);

		place_name = (EditText)findViewById(R.id.place_name);
		add_place_button = (Button)findViewById(R.id.add_a_place_button);

		add_place_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(add_place_button.getText().toString() != null && !add_place_button.getText().toString().isEmpty()){
					DatabaseHandler.getInstance(getApplicationContext()).setPlaces(new Place(place_name.getText().toString()));
					Intent placesIntent = new Intent(AddPlaceNameActivity.this, ListPlacesActivity.class);
					startActivity(placesIntent);
				}else
					Util.showToast(getApplicationContext(), "Place name cannot be empty");
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.places_action_bar, menu);
		return true;
	}

	//NEW
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			Toast.makeText(this, "Action refresh selected", Toast.LENGTH_SHORT)
			.show();
			break;
		case R.id.action_settings:
			Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT)
			.show();
			break;

		default:
			break;
		}

		return true;
	}
}
