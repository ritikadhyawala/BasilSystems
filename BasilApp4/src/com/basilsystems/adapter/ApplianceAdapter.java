package com.basilsystems.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.basilsystems.activities.NetworkSwitchSelectionActivity;
import com.basilsystems.activities.R;
import com.basilsystems.activities.ScheduleActivity;
import com.basilsystems.callbacks.interfaces.OnApplianceStatusChangeListener;
import com.basilsystems.custom.views.ButtonView;
import com.basilsystems.data.Appliance;

public class ApplianceAdapter extends ArrayAdapter<Appliance> {
	private static final String ADD_NEW_REMOTE = "Add new remote";

	private final LayoutInflater mInflater;

	private OnApplianceStatusChangeListener onApplianceStatusChangeListener;

	public ApplianceAdapter(Context context,
			OnApplianceStatusChangeListener onApplianceStatusChangeListener) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.onApplianceStatusChangeListener = onApplianceStatusChangeListener;
	}

	public void setData(List<Appliance> data) {
		clear();
		if (data != null) {
			for (Appliance appEntry : data) {
				add(appEntry);
			}
		}
	}

	/**
	 * Populate new items in the list.
	 */
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		final View view;
		final SeekBar seekBar;
		final ButtonView button;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.appliance_layout_list, parent,
					false);
		} else {
			view = convertView;
		}
		view.getLayoutParams().height = parent.getHeight() / getCount();
		final Appliance item = getItem(position);

		seekBar = (SeekBar) view.findViewById(R.id.statusBar);
		button = (ButtonView) view.findViewById(R.id.appliance_button);

		button.setCircleText(item.getApplianceName());

		if (item.getIsSlider()) {
			seekBar.setMax(100);
			seekBar.setMinimumWidth(40);

			seekBar.setProgress(item.getStatus());
			seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					JSONObject switchDetails = new JSONObject();
					try {
						switchDetails.put(
								"s",
								item.getAppliance_id().charAt(
										item.getAppliance_id().length() - 1));
						switchDetails.put("tt", "lt");
						switchDetails.put("l", (int)(seekBar.getProgress() / 6.5));

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendApplianceStatusChange(switchDetails);
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
				}
			});

		} else {
			seekBar.setVisibility(View.GONE);
		}
		button.setVisibility(View.VISIBLE);
		if (item.getStatus() == 0) {
			button.setChecked(false);
		} else {
			button.setChecked(true);
		}

		if (item.getIsOn()) {
			button.setChecked(true);
		} else {
			button.setChecked(false);
		}

		if (item.getIsAuto()) {
			button.setAuto(true);
		} else {
			button.setAuto(false);
		}

		button.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				int auto;

				if (item.getIsAuto()) {
					auto = 0;
					item.setIsAuto(false);
					((ButtonView) v).setAuto(false);
				} else {
					auto = 1;
					item.setIsAuto(true);
					((ButtonView) v).setAuto(true);
				}
				JSONObject switchDetails = new JSONObject();
				try {
					switchDetails.put("s", (char) item.getAppliance_id()
							.charAt(item.getAppliance_id().length() - 1));
					switchDetails.put("tt", "lp");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendApplianceStatusChange(switchDetails);
				return true;
			}
		});

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int progress;
				if (item.getIsOn()) {
					progress = 0;
					item.setIsOn(false);
					((ButtonView) v).setChecked(false);
				} else {
					progress = 1;
					item.setIsOn(true);
					((ButtonView) v).setChecked(true);
				}
				JSONObject switchDetails = new JSONObject();
				try {
					switchDetails.put("s", (char) item.getAppliance_id()
							.charAt(item.getAppliance_id().length() - 1));
					switchDetails.put("tt", "sp");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendApplianceStatusChange(switchDetails);
			}
		});

		Button options_button = (Button) view.findViewById(R.id.options_button);
		options_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final LinearLayout optionsLayout = (LinearLayout) view
						.findViewById(R.id.options_layout_appliance);
				optionsLayout.setVisibility(View.VISIBLE);

				Button scheduleButton = (Button) view
						.findViewById(R.id.schedule_appliance);

				scheduleButton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getContext(),
								ScheduleActivity.class);
						getContext().startActivity(intent);
					}
				});

				Button assign_button = (Button) view
						.findViewById(R.id.assign_button);
				Button assign_theme = (Button) view
						.findViewById(R.id.assign_theme);
                // to assign any network button functionality
				
				assign_button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// open a list with all the appliances with radio button
						// add isHidden property to the appliance
						// Assign the device id to parent device id 
						getContext().startActivity(new Intent(getContext(), NetworkSwitchSelectionActivity.class));
					}
				});
                // to assign any theme to any button
				assign_theme.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

				optionsLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						optionsLayout.setVisibility(View.GONE);
					}
				});
			}
		});

		return view;
	}

	private void sendApplianceStatusChange(JSONObject update) {
		onApplianceStatusChangeListener.onApplianceStatusChange(update);

	}

	// private void setNewStatus(final Appliance item, int progress) {
	// // int position1 = getPosition(item);
	// // remove(item);
	// item.setStatus(progress);
	// // insert(item, position1);
	// sendApplianceStatusChange(item);
	// }
}
