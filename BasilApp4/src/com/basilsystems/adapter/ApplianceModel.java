package com.basilsystems.adapter;

public class ApplianceModel {

	private String parentDeviceName;
	private String applianceName;
	private Boolean isSlider;
	private int status;
	private Boolean isAuto;
	
	public ApplianceModel(String parentDeviceName, String applianceName, int status, Boolean isSlider, Boolean isAuto ){
		this.parentDeviceName = parentDeviceName;
		this.applianceName = applianceName;
		this.status = status;
		this.isAuto = isAuto;
		this.isSlider = isSlider;
	}
	
    
    public String getParentDeviceName() {
		return parentDeviceName;
	}
	public void setParentDeviceName(String parentDeviceName) {
		this.parentDeviceName = parentDeviceName;
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
