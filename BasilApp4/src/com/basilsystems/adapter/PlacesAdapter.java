package com.basilsystems.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basilsystems.activities.ModeActivity;
import com.basilsystems.activities.R;
import com.basilsystems.data.Place;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class PlacesAdapter extends ArrayAdapter<Place> {
    
	private final LayoutInflater mInflater;
 
    public PlacesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(List<Place> data) {
        clear();
        if (data != null) {
            for (Place appEntry : data) {
                add(appEntry);
            }
        }
    }
 
    
    /**
     * Populate new items in the list.
     */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
 
        if (convertView == null) {
            view = mInflater.inflate(R.layout.home_layout_list, parent, false);
        } else {
            view = convertView;
        }
        if(position != getCount() - 1 ){
        	final Place item = getItem(position);
            ((TextView)view.findViewById(R.id.list_item)).setText(item.getName());
            
            LinearLayout place_layout = (LinearLayout)view.findViewById(R.id.place_layout);
            place_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					 Intent intent = new Intent(getContext(), ModeActivity.class);
			         SharedPreferenceManager preferenceManager = SharedPreferenceManager.getInstance(getContext());
			         preferenceManager.setPreference(Util.SELECTED_PLACE, item.getID(), SharedPreferenceManager.STRING);
					 getContext().startActivity(intent);
				}
			});
            
            Button option_button = (Button)view.findViewById(R.id.place_option_button);
            option_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    final LinearLayout options_layout = (LinearLayout)view.findViewById(R.id.options_layout_place);
                    options_layout.setVisibility(View.VISIBLE);
                    
                    Button delete_button = (Button)view.findViewById(R.id.delete_place);
                    delete_button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
                             Builder builder = new Builder(getContext());
                             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                          // set title
                 			alertDialogBuilder.setTitle("Are you sure?");
                  
                 			// set dialog message
                 			alertDialogBuilder
                 				.setMessage("All the Clover Boards will be deleted and you will have to configure them again.")
                 				.setCancelable(false)
                 				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                 					public void onClick(DialogInterface dialog,int id) {
                 						// if this button is clicked, close
                 						// current activity
//                 						MainActivity.this.finish();
                 					}
                 				  })
                 				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                 					public void onClick(DialogInterface dialog,int id) {
                 						// if this button is clicked, just close
                 						// the dialog box and do nothing
                 						dialog.cancel();
                 					}
                 				});
                  
                 				// create alert dialog
                 				AlertDialog alertDialog = alertDialogBuilder.create();
                  
                 				// show it
                 				alertDialog.show();
						}
					});
                    
                    options_layout.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							options_layout.setVisibility(View.GONE);
						}
					});
					
				}
			});
            
        }else{
        	LinearLayout addPlaceLayout = (LinearLayout)view.findViewById(R.id.add_place_layout);
        	addPlaceLayout.setVisibility(View.VISIBLE);
        }
        
        return view;
    }
} 
