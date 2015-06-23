package com.basilsystems.data;

public class Appliance {

	private String parent_id;
	private String applianceName;
	private Boolean isSlider;
	private int status;
	private Boolean isAuto;
	private Boolean isOn;

	public String getParent_id() {
		return parent_id;
	}


	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}


	public Boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(Boolean isOn) {
		this.isOn = isOn;
	}
	private String appliance_id;
	
	public String getAppliance_id() {
		return appliance_id;
	}


	public void setAppliance_id(String appliance_id) {
		this.appliance_id = appliance_id;
	}


	public String getID(){
		return appliance_id;
	}
	
	public Appliance(String parent_id, String applianceName, int status, Boolean isSlider, Boolean isAuto, Boolean isOn){
		this.parent_id = parent_id;
		this.applianceName = applianceName;
		this.status = status;
		this.isAuto = isAuto;
		this.isSlider = isSlider;
		this.isOn = isOn;
	}
	
    
    public String getParentID() {
		return parent_id;
	}
	public String getApplianceName() {
		return applianceName;
	}
	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}
	public Boolean getIsSlider() {
		return isSlider;
	}
	public void setIsSlider(Boolean isSlider) {
		this.isSlider = isSlider;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Boolean getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(Boolean isAuto) {
		this.isAuto = isAuto;
	}
}
