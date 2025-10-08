package com.mycom.sixclub.service.impl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.dao.EquipDAO;
import com.mycom.sixclub.service.dao.HistoryDAO;
import com.mycom.sixclub.service.dao.PostsDAO;
import com.mycom.sixclub.service.dao.RoutineDAO;
import com.mycom.sixclub.service.dao.SubscribeDAO;
import com.mycom.sixclub.service.dao.UsersDAO;
import com.mycom.sixclub.service.vo.CommentsVO;
import com.mycom.sixclub.service.vo.EquipVO;
import com.mycom.sixclub.service.vo.PostsVO;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutHistoryGroupVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

@Service("sixclubService")
public class SixClubServiceImpl implements SixclubService {
	@Autowired // 생성자, setter infection 없이 초기치, 자료전달이 가능
	private EquipDAO equip;

	@Autowired
	private UsersDAO users;

	@Autowired
	private PostsDAO posts;

	@Autowired
	private RoutineDAO routine;

	@Autowired
	private HistoryDAO routine_his;

	@Autowired
	private SubscribeDAO subscribe;

	// 유저 정보 관련
	@Override
	public UsersVO getUserinfo(int userNO) {
		return users.getUserinfo(userNO);
	}

	@Override
	public UsersVO getUserinfo_id(String userId) {
		// TODO Auto-generated method stub
		return users.getUserinfo_id(userId);
	}

	@Override
	public void insertUser(UsersVO userVO) {
		users.insertUser(userVO);
	}

	@Override
	public void updateUser(UsersVO vo) {
		users.updateUser(vo);
	}

	@Override
	public void deleteUser(int userNO) {
		users.deleteUser(userNO);
	}

	@Override
	public void updateUser_pwinfo(String pw, String salt, String id) {
		users.updateUser_pwinfo(pw, salt, id);

	}

	@Override
	public UsersVO getUserinfo_name(String user_name, String phone) {
		return users.getUserinfo_name(user_name, phone);
	}

	// 구독 관련
	@Override
	public void subSave(SubscribeVO vo) {
		subscribe.subInsert(vo);
	}

	@Override
	public SubscribeVO getSubscribe(Integer userNO) {
		// TODO Auto-generated method stub
		System.err.println();
		return subscribe.getSubscribe(userNO);
	}

	@Override
	public boolean isSubscribe(Integer userNO) {
		if (subscribe.getSubscribe(userNO) != null)
			return true;
		else
			return false;
	}

	@Override
	public List<SubscribeVO> getSubscribeHistory(Integer userNO) {
		return subscribe.getSubscribeHistory(userNO);

	}

	// 고객센터 관련

	@Override
	public int getPostCount() {
		return posts.getPostCount();
	}

	@Override
	public ArrayList<PostsVO> getAllContents(int startRow, int endRow) {
		return posts.getAllContents(startRow, endRow);
	}

	@Override
	public PostsVO getContents(int pid) {
		// TODO Auto-generated method stub
		return posts.getContents(pid);
	}

	@Override
	public void insertPost(PostsVO postVO) {
		posts.insertPost(postVO);

	}

	@Override
	public void updatePost(PostsVO postVO) {
		posts.updatePost(postVO);

	}

	@Override
	public void deletePost(int pid) {
		posts.deletePost(pid);

	}

	@Override
	public ArrayList<CommentsVO> getComments(int pid) {
		return posts.getComments(pid);
	}

	@Override
	public void insertComment(CommentsVO commentVO) {
		posts.insertComment(commentVO);

	}

	@Override
	public void deleteComment(int cid) {
		posts.deleteComment(cid);

	}

	@Override
	public int getCommentCount(int pid) {
		// TODO Auto-generated method stub
		return posts.getCommentCount(pid);
	}

	// 운동 루틴관련
	@Override
	public List<WorkoutVO> getRoutine_in() {
		return routine.getRoutine_in();
	}

	@Override
	public List<WorkoutVO> getRoutine_we_ch_main(int count) {
		return routine.getRoutine_we_ch_main(count);
	}

	@Override
	public List<WorkoutVO> getRoutine_we_ch_sub(int count) {
		return routine.getRoutine_we_ch_sub(count);
	}

	@Override
	public List<WorkoutVO> getRoutine_we_ba_main(int count) {
		return routine.getRoutine_we_ba_main(count);
	}

	@Override
	public List<WorkoutVO> getRoutine_we_ba_sub(int count) {
		return routine.getRoutine_we_ba_sub(count);
	}

	@Override
	public List<WorkoutVO> getRoutine_we_le_main(int count) {
		return routine.getRoutine_we_le_main(count);
	}

	@Override
	public List<WorkoutVO> getRoutine_we_le_sub(int count) {
		return routine.getRoutine_we_le_sub(count);
	}

	// 3. 운동 히스토리 그룹 생성
	@Override
	@Transactional
	public int insertWorkoutHistoryGroup(int userNo, LocalDate date, int woLevel, List<Integer> workoutIds) {
		// 1. historyNo 새로 가져오기
		int historyNo = routine_his.getNextHistoryNo();
		System.out.println(">>> [LOG] insertWorkoutHistoryGroup - generated historyNo = " + historyNo);

		// 2. WorkoutHistoryGroup 삽입
		Date sqlDate = Date.valueOf(date);
		routine_his.insertWorkoutHistoryGroup(userNo, sqlDate, historyNo, woLevel);
		System.out.println(">>> WorkoutHistoryGroup Insert 완료됨: historyNo = " + historyNo);

		// 3. 연결된 WorkoutHistory들 삽입
		for (int workoutId : workoutIds) {
			routine_his.insertWorkoutHistory(workoutId, historyNo);
			System.out.println(">>> WorkoutHistory Insert: workoutId = " + workoutId + ", historyNo = " + historyNo);
		}

		return historyNo;
	}

	// 4. 운동 히스토리 생성
	@Override
	@Transactional
	public void insertWorkoutHistory(int workoutId, int historyGroupNo) {
		routine_his.insertWorkoutHistory(workoutId, historyGroupNo);
	}

	@Override
	public int insertWorkoutPlan(int userNo) {
		int planId = routine_his.getNextPlanId();
		return planId;
	}

	@Override
	public void insertWorkoutPlanDay(int planNo, int dayOfWeek, int historyNo) {
		routine.insertWorkoutPlan(planNo, dayOfWeek, historyNo);
	}

	@Override
	public List<List<WorkoutVO>> selectSixDayRoutineByEarliestPlan(int userNo, LocalDate startDate) {
		List<List<WorkoutVO>> routineList = new ArrayList<List<WorkoutVO>>();

		for (int i = 0; i < 6; i++) {
			LocalDate date = startDate.plusDays(i);
			String dayOfWeek = String.valueOf(date.getDayOfWeek().getValue()); // 숫자 요일을 문자열로 변환

			List<WorkoutVO> dailyRoutine = routine_his.selectSixDayRoutineByEarliestPlan(userNo, dayOfWeek);

			routineList.add(dailyRoutine != null ? dailyRoutine : new ArrayList<WorkoutVO>());
		}

		return routineList;
	}

	// 1. 사용자 전체 플랜 번호 가져오기
	@Override
	public List<Integer> getAllPlanNosByUser(int userNo) {
		return routine_his.selectAllPlanNosByUser(userNo);
	}

	// 2. 특정 플랜 번호에 해당하는 history_no 목록 (요일 순)
	@Override
	public List<Integer> getHistoryNosByPlanNo(int planNo) {
		return routine_his.selectHistoryNosByPlanNo(planNo);
	}

	// 3. 여러 history_no에 해당하는 루틴 리스트 가져오기
	@Override
	public List<List<WorkoutVO>> getRoutineListByHistoryNos(List<Integer> historyNos) {
		List<List<WorkoutVO>> routineList = new ArrayList<List<WorkoutVO>>();
		for (Integer historyNo : historyNos) {
			List<WorkoutVO> dailyRoutine = routine_his.selectWorkoutsByHistoryNo(historyNo);
			routineList.add(dailyRoutine != null ? dailyRoutine : new ArrayList<WorkoutVO>());
		}
		return routineList;
	}

	@Override
	public List<WorkoutHistoryGroupVO> selectOneDayRoutineHistoryGroups(int userNo) {
		return routine_his.selectOneDayRoutineHistoryGroups(userNo);
	}

	@Override
	public Integer getRecentlyPlanNo(Integer userNo) {
		return routine_his.getRecentlyPlanNo(userNo);
	}

	/* change */
	@Override
	public List<Integer> getRoutineByPlanNo(Integer recentPlanNo) {
		return routine_his.getRoutineByPlanNo(recentPlanNo);
	}

	@Override
	public int getNextHistoryNo() {
		return routine_his.getNextHistoryNo();
	}

	@Override
	public int getNextPlanId() {
		return routine_his.getNextPlanId();
	}

	// 장비 탭
	@Override
	@Transactional
	public ArrayList<EquipVO> getAllEquip() {
		return equip.getAllEquip();
	}

	@Override
	public List<Integer> recommendWoType(int equip_no) {
		return equip.recommendWoType(equip_no);
	}

	// 관리자 페이지 용

	// 관리자 메인 & 유저관리
	@Override
	public List<UsersVO> getAllUserinfo(UsersVO vo1) {

		return users.getAllinfo();
	}

	@Override
	public ArrayList<UsersVO> getAllinfo_pag(int startRow, int endRow) {
		return users.getAllinfo_pag(startRow, endRow);
	}

	@Override
	public Integer getSubscribeUserNum() {
		return subscribe.getSubscribeUserNum();
	}

	@Override
	public Integer getAllSubscribeNum() {
		return subscribe.getAllSubscribeNum();
	}

	@Override
	public Integer getReSubscribeNum() {
		return subscribe.getReSubscribeNum();
	}

	@Override
	public List<SubscribeVO> getNewSubscribe() {
		return subscribe.getNewSubscribe();
	}

	@Override
	public List<SubscribeVO> getAllSubscribe(SubscribeVO svo) {
		// TODO Auto-generated method stub
		return subscribe.getAllSubscribe(svo);
	}

	// 운동관리

	@Override
	@Transactional
	public ArrayList<WorkoutVO> getAllWorkout() {
		return routine.getAllWorkout();
	}

	@Override
	public List<EquipVO> getEquipPaging(int offset, int pageSize) {
		return equip.getEquipPaging(offset, pageSize);
	}

	@Override
	public List<WorkoutVO> getWorkoutPaging(int offset, int pageSize) {
		return routine.getWorkoutPaging(offset, pageSize);
	}

	@Override
	public List<WorkoutVO> getWorkout(int woNo) {
		return routine.getWorkout(woNo);
	}

	@Override
	public List<EquipVO> getEquip(int equip_no) {
		return equip.getEquip(equip_no);
	}

	@Override
	public void updateWorkout(int woNo, int woType, String woName) {
		routine.updateWorkout(woNo, woType, woName);
	}

	@Override
	public void deleteWorkout(int woNo) {
		routine.deleteWorkout(woNo);
	}

	@Override
	public void updateEquip(int equip_no, String equip_name, String equip_desc, String equip_img) {
		equip.updateEquip(equip_no, equip_name, equip_desc, equip_img);
	}

	@Override
	public void deleteEquip(int equip_no) {
		equip.deleteEquip(equip_no);
	}

	@Override
	public void insertWorkout(String woName, int woType) {
		routine.insertWorkout(woName, woType);
	}

	@Override
	public void insertEquip(String equip_name, String equip_desc, String equip_img) {
		equip.insertEquip(equip_name, equip_desc, equip_img);
	}

	@Override
	public String getEquipImagePath(int equip_no) {
		return equip.getEquipImagePath(equip_no);
	}

	@Override
	public int getEquipCount() {
		return equip.getEquipCount();
	}

	@Override
	public int getWorkoutCount() {
		return routine.getWorkoutCount();
	}

	@Override
	public List<WorkoutHistoryGroupVO> getAllWorkoutHistoryGroup(WorkoutHistoryGroupVO whgvo) {
		return routine_his.getAllWorkoutHistoryGroup(whgvo);
	}

	@Override
	public int countWorkoutplan() {
		// TODO Auto-generated method stub
		return routine_his.countWorkoutplan();
	}

	@Override
	public int countWorkouthistorygroup() {
		// TODO Auto-generated method stub
		return routine_his.countWorkouthistorygroup();
	}

	@Override
	public List<WorkoutVO> getWorkoutPagingByType(int woType, int offset, int pageSize) {
		return routine.getWorkoutPagingByType(woType, offset, pageSize);
	}

	@Override
	public int getWorkoutCountByType(int woType) {
		return routine.getWorkoutCountByType(woType);
	}

}
