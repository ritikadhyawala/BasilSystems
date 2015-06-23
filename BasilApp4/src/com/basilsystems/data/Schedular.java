package com.basilsystems.data;

public class Schedular {
	
	
	public Schedular(String appliance_id, boolean isToday, String startTime,
			String endTime, boolean isRepeat, int weekday, boolean isAuto,
			int status) {
		super();
		this.appliance_id = appliance_id;
		this.isToday = isToday;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isRepeat = isRepeat;
		this.weekday = weekday;
		this.isAuto = isAuto;
		this.status = status;
	}
	
	private String appliance_id;
	private String schedule_id;
	public String getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}
	public String getAppliance_id() {
		return appliance_id;
	}
	public boolean isToday() {
		return isToday;
	}
	public String getStartTime() {
		return startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public boolean isRepeat() {
		return isRepeat;
	}
	public int getWeekday() {
		return weekday;
	}
	public boolean isAuto() {
		return isAuto;
	}
	public int getStatus() {
		return status;
	}
	private boolean isToday;
	private String startTime;
	private String endTime;
	private boolean isRepeat;
	private int weekday;
	private boolean isAuto;
	private int status;
	
	
	

}
