package com.mycom.sixclub.service.dao;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycom.sixclub.service.vo.WorkoutHistoryGroupVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

public interface HistoryDAO {

	List<WorkoutVO> selectSixDayRoutineByEarliestPlan(@Param("userNo") int userNo,
			@Param("dayOfWeek") String dayOfWeek);

	List<Integer> selectAllPlanNosByUser(int userNo);

	List<Integer> selectHistoryNosByPlanNo(int planNo);

	List<WorkoutVO> selectWorkoutsByHistoryNo(int historyNo);

	List<WorkoutHistoryGroupVO> selectOneDayRoutineHistoryGroups(@Param("userNo") int userNo);

	void insertWorkoutHistory(@Param("workoutId") int workoutId, @Param("historyGroupNo") int historyGroupNo);

	int getNextHistoryNo();

	int getNextPlanId();

	void insertWorkoutHistoryGroup(@Param("userNo") int userNo, @Param("date") Date date,
			@Param("historyNo") int historyNo, @Param("woLevel") int woLevel);

	List<WorkoutHistoryGroupVO> getAllWorkoutHistoryGroup(WorkoutHistoryGroupVO whgvo);

	Integer getRecentlyPlanNo(@Param("userNo") Integer userNo);

	List<Integer> getRoutineByPlanNo(@Param("recentPlanNo") Integer recentPlanNo);
	
	int countWorkoutplan();

	int countWorkouthistorygroup();
}
