package com.mycom.sixclub.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycom.sixclub.service.vo.CommentsVO;
import com.mycom.sixclub.service.vo.EquipVO;
import com.mycom.sixclub.service.vo.PostsVO;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutHistoryGroupVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

public interface SixclubService {

	ArrayList<EquipVO> getAllEquip();

	ArrayList<WorkoutVO> getAllWorkout();

	List<Integer> recommendWoType(int equip_no);

	// 유저정보관련

	UsersVO getUserinfo_id(String userId);

	UsersVO getUserinfo(int userNO);

	void insertUser(UsersVO userVO);

	void updateUser(UsersVO vo);

	void deleteUser(int userNO);

	void updateUser_pwinfo(String encoding_pw, String salt, String id);

	UsersVO getUserinfo_name(String name, String phone);

	void subSave(SubscribeVO vo);

	boolean isSubscribe(Integer userNO);

	SubscribeVO getSubscribe(Integer userNO);

	List<SubscribeVO> getSubscribeHistory(Integer userNO);

	// 고객센터 관련

	int getPostCount();

	ArrayList<PostsVO> getAllContents(int startRow, int endRow);

	PostsVO getContents(int pid);

	void insertPost(PostsVO postVO);

	void updatePost(PostsVO postVO);

	void deletePost(int pid);

	ArrayList<CommentsVO> getComments(int pid);

	void insertComment(CommentsVO commentVO);

	void deleteComment(int cid);

	int getCommentCount(int pid);

	// 운동루틴 관련
	List<WorkoutVO> getRoutine_in();

	List<WorkoutVO> getRoutine_we_ch_main(int count);

	List<WorkoutVO> getRoutine_we_ch_sub(int count);

	List<WorkoutVO> getRoutine_we_ba_main(int count);

	List<WorkoutVO> getRoutine_we_ba_sub(int count);

	List<WorkoutVO> getRoutine_we_le_main(int count);

	List<WorkoutVO> getRoutine_we_le_sub(int count);

	// 운동 기록 저장
	int insertWorkoutHistoryGroup(int userNo, LocalDate date, int woLevel, List<Integer> workoutIds);

	void insertWorkoutHistory(int workoutId, int historyGroupNo);

	// 플랜 생성 및 요일 저장
	int insertWorkoutPlan(int userNo); // plan_no 생성

	void insertWorkoutPlanDay(int planNo, int dayOfWeek, int historyGroupNo);

	// 플랜 기반 루틴 조회
	List<List<WorkoutVO>> selectSixDayRoutineByEarliestPlan(int userNo, LocalDate startDate);

	// 운동 기록 조회
	List<Integer> getAllPlanNosByUser(int userNo);

	List<Integer> getHistoryNosByPlanNo(int planNo); // 요일 순 정렬

	List<List<WorkoutVO>> getRoutineListByHistoryNos(List<Integer> historyNos);

	List<WorkoutHistoryGroupVO> selectOneDayRoutineHistoryGroups(int userNo);

	Integer getRecentlyPlanNo(Integer userNo);

	List<Integer> getRoutineByPlanNo(Integer recentPlanNo);

	// 시퀀스
	int getNextHistoryNo();

	int getNextPlanId();

	// 관리자 페이지

	// 관리자 메인 & 유저관리

	List<UsersVO> getAllUserinfo(UsersVO vo1);

	ArrayList<UsersVO> getAllinfo_pag(int startRow, int endRow);

	Integer getSubscribeUserNum();

	Integer getAllSubscribeNum();

	Integer getReSubscribeNum();

	List<SubscribeVO> getNewSubscribe();

	List<SubscribeVO> getAllSubscribe(SubscribeVO svo);

	// 운동관리

	List<WorkoutVO> getWorkoutPaging(int offset, int pageSize);

	List<EquipVO> getEquipPaging(int offset, int pageSize);

	int getWorkoutCount();

	int getEquipCount();

	List<WorkoutVO> getWorkout(int woNo);

	List<EquipVO> getEquip(int equip_no);

	void updateWorkout(int woNo, int woType, String woName);

	void deleteWorkout(int woNo);

	void updateEquip(int equip_no, String equip_name, String equip_desc, String equip_img);

	void deleteEquip(int equip_no);

	void insertWorkout(String woName, int woType);

	void insertEquip(String equip_name, String equip_desc, String equip_img);

	String getEquipImagePath(int equip_no);

	List<WorkoutHistoryGroupVO> getAllWorkoutHistoryGroup(WorkoutHistoryGroupVO whgvo);

	int countWorkoutplan();

	int countWorkouthistorygroup();

	List<WorkoutVO> getWorkoutPagingByType(int woType, int offset, int pageSize);

	int getWorkoutCountByType(int woType);

}
