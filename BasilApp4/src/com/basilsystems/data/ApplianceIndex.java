package com.basilsystems.data;

public class ApplianceIndex {

	String applianceName;

	public ApplianceIndex(String applianceName, String applianceID) {
		super();
		this.applianceName = applianceName;
		this.applianceID = applianceID;
	}

	String applianceID;

	public String getApplianceName() {
		return applianceName;
	}

	public String getApplianceID() {
		return applianceID;
	}

}
