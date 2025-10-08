package com.mycom.sixclub.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycom.sixclub.service.vo.EquipVO;

public interface EquipDAO {

	ArrayList<EquipVO> getAllEquip();

	List<Integer> recommendWoType(@Param("equip_no") int equip_no);

	List<EquipVO> getEquip(@Param("equip_no") int equip_no);

	void insertEquip(@Param("equip_name") String equip_name, @Param("equip_desc") String equip_desc,
			@Param("equip_img") String equip_img);

	int getEquipCount();

	List<EquipVO> getEquipPaging(@Param("offset") int offset, @Param("pageSize") int pageSize);

	void updateEquip(@Param("equip_no") int equip_no, @Param("equip_name") String equip_name,
			@Param("equip_desc") String equip_desc, @Param("equip_img") String equip_img);

	void deleteEquip(@Param("equip_no") int equip_no);

	String getEquipImagePath(@Param("equip_no") int equip_no);

}
