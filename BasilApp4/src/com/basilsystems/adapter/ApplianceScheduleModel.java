package com.basilsystems.adapter;

public class ApplianceScheduleModel {
	private String startTime;
	private String endTime;
	private Boolean isScheduledToday;
	private Boolean isScheduledDaily;
	private String weekdays;
	private Boolean isScheduleRepeat;

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Boolean getIsScheduledToday() {
		return isScheduledToday;
	}
	public void setIsScheduledToday(Boolean isScheduledToday) {
		this.isScheduledToday = isScheduledToday;
	}
	public Boolean getIsScheduledDaily() {
		return isScheduledDaily;
	}
	public void setIsScheduledDaily(Boolean isScheduledDaily) {
		this.isScheduledDaily = isScheduledDaily;
	}
	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public Boolean getIsScheduleRepeat() {
		return isScheduleRepeat;
	}
	public void setIsScheduleRepeat(Boolean isScheduleRepeat) {
		this.isScheduleRepeat = isScheduleRepeat;
	}
}
