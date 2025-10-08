package com.mycom.sixclub.service.vo;

import java.sql.Date;
import java.util.List;

public class WorkoutHistoryGroupVO {
    private int historyNo;
    private int userNo;
    private Date woDate;
    private int woLevel;
    private List<WorkoutVO> workouts;

    public WorkoutHistoryGroupVO() {}

    public WorkoutHistoryGroupVO(int historyNo, int userNo, Date woDate, int woLevel) {
        this.historyNo = historyNo;
        this.userNo = userNo;
        this.woDate = woDate;
        this.woLevel = woLevel;
    }

    public int getHistoryNo() {
        return historyNo;
    }

    public void setHistoryNo(int historyNo) {
        this.historyNo = historyNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public Date getWoDate() {
        return woDate;
    }

    public void setWoDate(Date woDate) {
        this.woDate = woDate;
    }

    public int getWoLevel() {
        return woLevel;
    }

    public void setWoLevel(int woLevel) {
        this.woLevel = woLevel;
    }

	public List<WorkoutVO> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<WorkoutVO> workouts) {
		this.workouts = workouts;
	}
}
