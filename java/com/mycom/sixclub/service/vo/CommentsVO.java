package com.mycom.sixclub.service.vo;

import java.sql.Date;
import java.util.ArrayList;

public class CommentsVO {
	int pid, cid, cuser_no;
	Integer ccid;
	String ccontents, cuser_id;
	Date cd;
	ArrayList<CommentsVO> replies = new ArrayList<CommentsVO>();
	private boolean isOwner;
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	public Integer getCcid() {
		return ccid;
	}
	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}
	public String getCcontents() {
		return ccontents;
	}
	public void setCcontents(String ccontents) {
		this.ccontents = ccontents;
	}
	public int getCuser_no() {
		return cuser_no;
	}
	public void setCuser_no(int cuser_no) {
		this.cuser_no = cuser_no;
	}
	public String getCuser_id() {
		return cuser_id;
	}
	public void setCuser_id(String cuser_id) {
		this.cuser_id = cuser_id;
	}
	public Date getCd() {
		return cd;
	}
	public void setCd(Date cd) {
		this.cd = cd;
	}
	public ArrayList<CommentsVO> getReplies() {
		return replies;
	}
	public void setReplies(ArrayList<CommentsVO> replies) {
		this.replies = replies;
	}
	
	public String getCcontentsWithBr() {
		if (ccontents == null)
			return "";
		return ccontents.replaceAll("(\r\n|\r|\n)", "<br>");
	}
	
	public boolean getIsOwner() {
	    return isOwner;
	}

	public void setIsOwner(boolean isOwner) {
	    this.isOwner = isOwner;
	}

}
