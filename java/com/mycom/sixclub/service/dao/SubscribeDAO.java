package com.mycom.sixclub.service.dao;

import java.util.List;

import com.mycom.sixclub.service.vo.SubscribeVO;

public interface SubscribeDAO {
	SubscribeVO getSubscribe(Integer userNO);

	void subInsert(SubscribeVO vo);

	int getSubscribeUserNum();

	Integer getReSubscribeNum();

	Integer getAllSubscribeNum();

	List<SubscribeVO> getNewSubscribe();
	
	List<SubscribeVO> getAllSubscribe(SubscribeVO svo);
	
	List<SubscribeVO>	getSubscribeHistory(Integer userNO);
}
