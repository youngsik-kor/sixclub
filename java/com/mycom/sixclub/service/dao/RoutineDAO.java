package com.mycom.sixclub.service.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycom.sixclub.service.vo.WorkoutVO;

public interface RoutineDAO {
	ArrayList<WorkoutVO> getAllWorkout();

	List<WorkoutVO> getWorkout(@Param("woNo") int woNo);

	int getWorkoutCount();

	void updateWorkout(@Param("woNo") int woNo, @Param("woType") int woType, @Param("woName") String woName);

	void deleteWorkout(@Param("woNo") int woNo);

	List<WorkoutVO> getWorkoutPaging(@Param("offset") int offset, @Param("pageSize") int pageSize);

	void insertWorkout(@Param("woName") String woName, @Param("woType") int woType);

	List<WorkoutVO> getRoutine_in();

	List<WorkoutVO> getRoutine_we_ch_main(int count);

	List<WorkoutVO> getRoutine_we_ch_sub(int count);

	List<WorkoutVO> getRoutine_we_ba_main(int count);

	List<WorkoutVO> getRoutine_we_ba_sub(int count);

	List<WorkoutVO> getRoutine_we_le_main(int count);

	List<WorkoutVO> getRoutine_we_le_sub(int count);

	void insertWorkoutPlan(@Param("planNo") int planNo, @Param("dayOfWeek") int dayOfWeek,
			@Param("historyNo") int historyNo);

	List<WorkoutVO> getWorkoutPagingByType(@Param("woType") int woType, @Param("offset") int offset,
			@Param("pageSize") int pageSize);

	int getWorkoutCountByType(@Param("woType") int woType);
}
