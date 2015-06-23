package com.basilsystems.data;

public class ModeDevice {

	private String device_id;
	private String mode_id;

	public String getModeId(){
		 return mode_id;
	}
	
	public String getDeviceID() {
		return device_id;
	}
	public ModeDevice(String device_id, String mode_id) {
		super();
		this.device_id = device_id;
		this.mode_id = mode_id;
	}

}
