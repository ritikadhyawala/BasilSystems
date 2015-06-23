package com.basilsystems.data;

public class ModeAppliance {
	
	private String mode_device_id;
	private boolean isAuto;
	private boolean isSlider;
	private int status;
	private String id;
	private boolean isOn;
	public boolean isOn() {
		return isOn;
	}
	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMode_device_id() {
		return mode_device_id;
	}
	public ModeAppliance(String id, String mode_device_id, boolean isAuto,
			boolean isSlider, int status, boolean isOn) {
		super();
		this.mode_device_id = mode_device_id;
		this.id = id;
		this.isAuto = isAuto;
		this.isSlider = isSlider;
		this.status = status;
		this.isOn = isOn;
	}
	public boolean isAuto() {
		return isAuto;
	}
	public boolean isSlider() {
		return isSlider;
	}
	public int getStatus() {
		return status;
	}

}
