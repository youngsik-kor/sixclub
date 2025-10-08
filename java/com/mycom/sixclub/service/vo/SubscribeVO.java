package com.mycom.sixclub.service.vo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SubscribeVO {
	int subscribe_no, user_no;
	Date start_date, end_date;
	String user_name;
	int monthBetween;

	public int getSubscribe_no() {
		return subscribe_no;
	}

	public void setSubscribe_no(int subscribe_no) {
		this.subscribe_no = subscribe_no;
	}

	public int getUser_no() {
		return user_no;
	}

	public void setUser_no(int user_no) {
		this.user_no = user_no;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public int getMonthBetween() {

		return monthBetween;
	}

	public void setMonthBetween() {

		LocalDate start = this.start_date.toLocalDate(); // java.sql.Date → LocalDate
		LocalDate end = this.end_date.toLocalDate();

		this.monthBetween = (int) ChronoUnit.MONTHS.between(start, end) ;
	}
}
