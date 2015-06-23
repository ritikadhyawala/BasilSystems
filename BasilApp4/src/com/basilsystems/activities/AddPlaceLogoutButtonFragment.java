package com.basilsystems.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AddPlaceLogoutButtonFragment extends Fragment {

	
	private Button add_place_button;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.add_places_layout_fragment,
	        container, false);
	    add_place_button = (Button)view.findViewById(R.id.add_a_place);
	    add_place_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent placesIntent = new Intent(getActivity(), AddPlaceNameActivity.class);
		        startActivity(placesIntent);
			}
		});
	    return view;
	  }
}
