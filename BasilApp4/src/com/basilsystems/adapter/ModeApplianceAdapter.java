package com.basilsystems.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.basilsystems.activities.R;
import com.basilsystems.callbacks.interfaces.OnApplianceStatusChangeListener;
import com.basilsystems.custom.views.ButtonView;
import com.basilsystems.data.Appliance;
import com.basilsystems.util.DatabaseHandler;
import com.basilsystems.util.SharedPreferenceManager;
import com.basilsystems.util.Util;

public class ModeApplianceAdapter extends ArrayAdapter<Appliance> {
	private final LayoutInflater mInflater;

	private OnApplianceStatusChangeListener onApplianceStatusChangeListener;

	public ModeApplianceAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			view = mInflater.inflate(R.layout.mode_appliance_layout, parent,
					false);
		} else {
			view = convertView;
		}
		view.getLayoutParams().height = parent.getHeight() / getCount();
		final Appliance item = getItem(position);

		seekBar = (SeekBar) view.findViewById(R.id.mode_statusBar);
		button = (ButtonView) view.findViewById(R.id.mode_appliance_button);

		button.setCircleText(item.getApplianceName());

		CheckBox select_appliance = (CheckBox) view
				.findViewById(R.id.select_mode_appliance);
		select_appliance
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

					}
				});
		DatabaseHandler dbHandler = DatabaseHandler.getInstance(getContext());
		List<Appliance> allAppliancesGivenDevice = dbHandler
				.getModeAppliances((String) SharedPreferenceManager
						.getInstance(getContext()).getPreferenceValue(
								Util.SELECTED_MODE_DEVICE,
								SharedPreferenceManager.STRING));
		if (allAppliancesGivenDevice.size() > 0) {
			for (int i = 0; i < allAppliancesGivenDevice.size(); i++) {
				if (allAppliancesGivenDevice.get(i).getID()
						.equals(item.getID())) {
					select_appliance.setChecked(true);
					break;
				}
			}
		}
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
						switchDetails.put("l", (seekBar.getProgress() / 16));

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
					switchDetails.put(
							"s",
							item.getAppliance_id().charAt(
									item.getAppliance_id().length() - 1));
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
					switchDetails.put(
							"s",
							item.getAppliance_id().charAt(
									item.getAppliance_id().length() - 1));
					switchDetails.put("tt", "sp");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendApplianceStatusChange(switchDetails);

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
