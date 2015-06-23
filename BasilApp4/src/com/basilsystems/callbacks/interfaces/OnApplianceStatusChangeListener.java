package com.basilsystems.callbacks.interfaces;

import org.json.JSONObject;

import com.basilsystems.data.Appliance;

public interface OnApplianceStatusChangeListener {

	public void onApplianceStatusChange(JSONObject update);
}
