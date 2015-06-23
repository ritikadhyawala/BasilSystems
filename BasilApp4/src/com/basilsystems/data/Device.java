package com.basilsystems.data;

public class Device {
	
	private String name;
	private String device_id;
	private int icon;
	
	public int getIcon() {
		return icon;
	}


	public void setIcon(int icon) {
		this.icon = icon;
	}


	public String getDevice_id() {
		return device_id;
	}


	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}


//	public String getId() {
//		return getPlace_id() + "_" + getName();
//	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	private String place_id;

	public Device(String name, String place_id, int icon) {
       this.name = name;
       this.place_id = place_id;
       this.icon = icon;
	}

}
