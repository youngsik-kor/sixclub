package com.mycom.sixclub.service.vo;

import java.sql.Date;

public class WorkoutVO {

	private int woNo;
	private String woName;
	private int woType;
	private int dayOfWeek;
	private Date woDate;
	private int woLevel;
	private int historyNo; 


	public int getHistoryNo() {
	    return historyNo;
	}


	public void setHistoryNo(int historyNo) {
	    this.historyNo = historyNo;
	}

	public int getWoLevel() {
	    return woLevel;
	}

	public void setWoLevel(int woLevel) {
	    this.woLevel = woLevel;
	}

	public Date getWoDate() {
	    return woDate;
	}

	public void setWoDate(Date woDate) {
	    this.woDate = woDate;
	}

	public int getDayOfWeek() {
	    return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
	    this.dayOfWeek = dayOfWeek;
	}
	
	public WorkoutVO() {
	}

	public int getWoNo() {
		return woNo;
	}

	public void setWoNo(int woNo) {
		this.woNo = woNo;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public int getWoType() {
		return woType;
	}

	public void setWoType(int woType) {
		this.woType = woType;
	}

	public WorkoutVO(int woNo, String woName, int woType, int dayOfWeek) {
		super();
		this.woNo = woNo;
		this.woName = woName;
		this.woType = woType;
		this.dayOfWeek = dayOfWeek;
	}

	public WorkoutVO(int historyNo, int woNo, String woName, int woType, int dayOfWeek, Date woDate, int woLevel) {
	    this.historyNo = historyNo;
	    this.woNo = woNo;
	    this.woName = woName;
	    this.woType = woType;
	    this.dayOfWeek = dayOfWeek;
	    this.woDate = woDate;
	    this.woLevel = woLevel;
	}

}
