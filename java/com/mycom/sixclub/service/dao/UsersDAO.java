package com.mycom.sixclub.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycom.sixclub.service.vo.UsersVO;

public interface UsersDAO {
	UsersVO getUserinfo_id(String userId);

	UsersVO getUserinfo(int userNO);

	void insertUser(UsersVO userVO);

	void updateUser(UsersVO vo);

	void deleteUser(int userNO);

	void updateUser_pwinfo(@Param("passwd") String passwd, @Param("salt") String salt,
			@Param("user_id") String user_id);

	UsersVO getUserinfo_name(@Param("user_name") String user_name, @Param("phone") String phone);

	// 관리자 페이지

	List<UsersVO> getAllinfo();
	
	ArrayList<UsersVO> getAllinfo_pag(@Param("startRow") int startRow, @Param("endRow") int endRow);
}
