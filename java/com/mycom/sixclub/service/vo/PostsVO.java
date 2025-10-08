package com.mycom.sixclub.service.vo;

import java.util.Date;

public class PostsVO {
	private int pid, puser_no, secret, commentCount;
	private String title, pcontents, attachment;
	private String puser_id;
	private boolean subscribe;
	int monthBetween;


	public boolean isSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}

	public String getPuser_id() {
		return puser_id;
	}

	public void setPuser_id(String puser_id) {
		this.puser_id = puser_id;
	}

	public int getPuser_no() {
		return puser_no;
	}

	public void setPuser_no(int puser_no) {
		this.puser_no = puser_no;
	}

	private Date pd;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPcontents() {
		return pcontents;
	}

	public void setPcontents(String pcontents) {
		this.pcontents = pcontents;
	}

	public Date getPd() {
		return pd;
	}

	public void setPd(Date pd) {
		this.pd = pd;
	}

	public int getSecret() {
		return secret;
	}

	public void setSecret(int secret) {
		this.secret = secret;
	}

	// 줄바꿈 포함한 getter
	public String getPcontentsWithBr() {
		if (pcontents == null)
			return "";
		return pcontents.replaceAll("(\r\n|\r|\n)", "<br>");
	}
	
	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getMonthBetween() {
		return monthBetween;
	}

	public void setMonthBetween(int monthBetween) {
		this.monthBetween = monthBetween;
	}

	
	
}
