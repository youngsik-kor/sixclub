package com.mycom.sixclub.service.vo;

public class WorkoutPlanVO {
    private int planNo;
    private int dayOfWeek; 
    private int historyNo;
    
    public WorkoutPlanVO () {}
    
	public int getPlanNo() {
		return planNo;
	}
	public void setPlanNo(int planNo) {
		this.planNo = planNo;
	}
	public int getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getHistoryNo() {
		return historyNo;
	}
	public void setHistoryNo(int historyNo) {
		this.historyNo = historyNo;
	}

	public WorkoutPlanVO(int planNo, int dayOfWeek, int historyNo) {
		super();
		this.planNo = planNo;
		this.dayOfWeek = dayOfWeek;
		this.historyNo = historyNo;
	}
    
}
