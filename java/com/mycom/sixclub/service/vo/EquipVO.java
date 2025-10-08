package com.mycom.sixclub.service.vo;

import java.util.List;

public class EquipVO {
	private int equip_no;
	private String equip_name, equip_desc, equip_img;
	
	private List<String> equip_brand_list;
	
	public String getEquip_img() {
		return equip_img;
	}
	public void setEquip_img(String equip_img) {
		this.equip_img = equip_img;
	}
	public int getEquip_no() {
		return equip_no;
	}
	public void setEquip_no(int equip_no) {
		this.equip_no = equip_no;
	}
	public String getEquip_name() {
		return equip_name;
	}
	public void setEquip_name(String equip_name) {
		this.equip_name = equip_name;
	}
	public String getEquip_desc() {
		return equip_desc;
	}
	public void setEquip_desc(String equip_desc) {
		this.equip_desc = equip_desc;
	}
	public List<String> getEquip_brand_list() {
		return equip_brand_list;
	}
	public void setEquip_brand_list(List<String> equip_brand_list) {
		this.equip_brand_list = equip_brand_list;
	}


}
