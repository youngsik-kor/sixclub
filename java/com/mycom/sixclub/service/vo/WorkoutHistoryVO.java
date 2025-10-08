package com.mycom.sixclub.service.vo;


public class WorkoutHistoryVO {
    private int historyNo;
    private int woNo;
    
    public WorkoutHistoryVO() {}
    
    
    
	public int getHistoryNo() {
		return historyNo;
	}
	public void setHistoryNo(int historyNo) {
		this.historyNo = historyNo;
	}
	public int getWoNo() {
		return woNo;
	}
	public void setWoNo(int woNo) {
		this.woNo = woNo;
	}



	public WorkoutHistoryVO(int historyNo, int woNo) {
		super();
		this.historyNo = historyNo;
		this.woNo = woNo;
	}
    
}
