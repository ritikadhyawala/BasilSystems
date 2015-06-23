package com.basilsystems.data;

public class Place {
	

	private String name;
	private String place_id;

	public String getID() {
		return place_id;
	}

	public void setID(String iD) {
		place_id = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Place(String name) {
         this.name = name;
	}

}
