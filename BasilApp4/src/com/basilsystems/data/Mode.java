package com.basilsystems.data;

public class Mode {
	
	private String mode_name;
	private String mode_id;
	public String getMode_id() {
		return mode_id;
	}
	public void setMode_id(String mode_id) {
		this.mode_id = mode_id;
	}
	public String getMode_name() {
		return mode_name;
	}
	public String getPlace_name() {
		return place_id;
	}
	public Mode(String mode_name, String place_id, String mode_id) {
		super();
		this.mode_name = mode_name;
		this.mode_id = mode_id;
		this.place_id = place_id;
	}
	private String place_id;

}
