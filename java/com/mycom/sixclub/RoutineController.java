package com.mycom.sixclub;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

@Controller
public class RoutineController {

	@Resource(name = "sixclubService")
	private SixclubService sixClubService;

	@RequestMapping("/routine.do")
	public String routine(@SessionAttribute(value = "userNO", required = false) Integer userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		// 구독 여부 체크
		Boolean isSubscribed = (Boolean) session.getAttribute("isSubscribed");
		if (isSubscribed == null || !isSubscribed) {
			model.addAttribute("msg", "해당 서비스는 구독자만 이용할 수 있습니다.");
			model.addAttribute("url", "subScribe.do");
			return "alert";
		}

		return "/routine/days";
	}

	@RequestMapping("/oneday.do")
	public String oneday(@SessionAttribute(value = "userNO", required = false) Integer userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";
		return "/routine/goal";
	}

	@RequestMapping("/sixdays.do")
	public String sixdays(@SessionAttribute(value = "userNO", required = false) Integer userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";
		return "/routine/sixdays_routine";
	}

	@PostMapping("/sixdays_routine.do")
	public String sixdaysRoutine(@RequestParam(value = "level", required = false) String level,
			@SessionAttribute(value = "userNO", required = false) int userNO, HttpSession session, Model model)
			throws Exception {

		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		if (level == null || level.trim().isEmpty()) {
			model.addAttribute("msg", "운동 난이도를 선택해주세요.");
			model.addAttribute("url", "sixdays.do");
			return "alert";
		}

		int countMain = 3, countSub = 2;
		if ("intermediate".equals(level)) {
			countMain = 4;
			countSub = 2;
		} else if ("advanced".equals(level)) {
			countMain = 5;
			countSub = 3;
		}

		List<WorkoutVO> intervalPool = sixClubService.getRoutine_in();
		List<WorkoutVO> day1 = pickRandom(intervalPool, 5);
		List<WorkoutVO> day3 = pickRandom(intervalPool, 5);
		List<WorkoutVO> day5 = pickRandom(intervalPool, 5);

		List<WorkoutVO> day2 = new ArrayList<WorkoutVO>();
		day2.addAll(sixClubService.getRoutine_we_ch_main(countMain));
		day2.addAll(sixClubService.getRoutine_we_ch_sub(countSub));

		List<WorkoutVO> day4 = new ArrayList<WorkoutVO>();
		day4.addAll(sixClubService.getRoutine_we_ba_main(countMain));
		day4.addAll(sixClubService.getRoutine_we_ba_sub(countSub));

		List<WorkoutVO> day6 = new ArrayList<WorkoutVO>();
		day6.addAll(sixClubService.getRoutine_we_le_main(countMain));
		day6.addAll(sixClubService.getRoutine_we_le_sub(countSub));

		List<List<WorkoutVO>> fullRoutine = new ArrayList<List<WorkoutVO>>();

		fullRoutine.add(day1);
		fullRoutine.add(day2);
		fullRoutine.add(day3);
		fullRoutine.add(day4);
		fullRoutine.add(day5);
		fullRoutine.add(day6);

		session.setAttribute("routineList", fullRoutine);
		session.setAttribute("level", level);
		model.addAttribute("routineList", fullRoutine);

		return "/routine/sixdays_routine_result";
	}

	private List<WorkoutVO> pickRandom(List<WorkoutVO> pool, int count) {
		List<WorkoutVO> copy = new ArrayList<WorkoutVO>(pool);
		Collections.shuffle(copy); // 리스트 섞기

		Set<Integer> seen = new HashSet<Integer>();
		List<WorkoutVO> result = new ArrayList<WorkoutVO>();

		for (WorkoutVO wo : copy) {
			if (seen.add(wo.getWoNo())) { // 중복 WO_NO 제거
				result.add(wo);
				if (result.size() >= count)
					break;
			}
		}

		return result;
	}

	@RequestMapping("/interval.do")
	public String interval(@SessionAttribute(value = "userNO", required = false) Integer userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";
		return "/routine/interval";
	}

	@RequestMapping("/weight.do")
	public String weight(@SessionAttribute(value = "userNO", required = false) Integer userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";
		return "/routine/weight";
	}

	@RequestMapping("/in_level.do")
	public String in_level(@SessionAttribute(value = "userNO", required = false) Integer userNO,
			@RequestParam(value = "level", required = false) String level, HttpSession session, Model model)
			throws Exception {

		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		if (level == null || level.trim().isEmpty()) {
			model.addAttribute("msg", "운동 난이도를 선택해주세요.");
			model.addAttribute("url", "interval.do");
			return "alert";
		}

		List<WorkoutVO> routine = sixClubService.getRoutine_in();
		session.setAttribute("routineList", routine);

		model.addAttribute("routine", routine);
		model.addAttribute("level", level);

		return "/routine/routine_in";
	}

	@PostMapping("/we_level.do")
	public String we_level(@SessionAttribute(value = "userNO", required = false) Integer userNO,
			@RequestParam(value = "level", required = false) String level,
			@RequestParam(value = "type", required = false) String type, HttpSession session, Model model)
			throws Exception {

		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		if (level == null || level.trim().isEmpty()) {
			model.addAttribute("msg", "운동 난이도를 선택해주세요.");
			model.addAttribute("url", "weight.do");
			return "alert";
		}
		if (type == null || type.trim().isEmpty()) {
			model.addAttribute("msg", "운동 부위를 선택해주세요.");
			model.addAttribute("url", "weight.do");
			return "alert";
		}

		int countMain = 3, countSub = 2;
		if ("intermediate".equals(level)) {
			countMain = 4;
		} else if ("advanced".equals(level)) {
			countMain = 5;
			countSub = 3;
		}

		List<WorkoutVO> mainList1 = null, subList1 = null, mainList2 = null, subList2 = null;

		if ("chest&triceps".equals(type)) {
			mainList1 = sixClubService.getRoutine_we_ch_main(countMain);
			subList1 = sixClubService.getRoutine_we_ch_sub(countSub);
			mainList2 = sixClubService.getRoutine_we_ch_main(countMain);
			subList2 = sixClubService.getRoutine_we_ch_sub(countSub);
		} else if ("back&biceps".equals(type)) {
			mainList1 = sixClubService.getRoutine_we_ba_main(countMain);
			subList1 = sixClubService.getRoutine_we_ba_sub(countSub);
			mainList2 = sixClubService.getRoutine_we_ba_main(countMain);
			subList2 = sixClubService.getRoutine_we_ba_sub(countSub);
		} else if ("legs&shoulders".equals(type)) {
			mainList1 = sixClubService.getRoutine_we_le_main(countMain);
			subList1 = sixClubService.getRoutine_we_le_sub(countSub);
			mainList2 = sixClubService.getRoutine_we_le_main(countMain);
			subList2 = sixClubService.getRoutine_we_le_sub(countSub);
		}

		List<WorkoutVO> routine1 = new ArrayList<WorkoutVO>();
		routine1.addAll(mainList1);
		routine1.addAll(subList1);
		routine1 = removeDuplicateWorkouts(routine1);

		List<WorkoutVO> routine2 = new ArrayList<WorkoutVO>();
		routine2.addAll(mainList2);
		routine2.addAll(subList2);
		routine2 = removeDuplicateWorkouts(routine2);

		session.setAttribute("routineList_we1", routine1);
		session.setAttribute("routineList_we2", routine2);
		session.setAttribute("level_we", level);
		session.setAttribute("type_we", type);

		model.addAttribute("routine1", routine1);
		model.addAttribute("routine2", routine2);
		model.addAttribute("level", level);
		model.addAttribute("type", type);

		return "/routine/routine_we";
	}

	private List<WorkoutVO> removeDuplicateWorkouts(List<WorkoutVO> list) {
		Set<Integer> seen = new HashSet<Integer>();
		List<WorkoutVO> result = new ArrayList<WorkoutVO>();

		for (WorkoutVO wo : list) {
			if (seen.add(wo.getWoNo())) {
				result.add(wo);
			}
		}
		return result;
	}

	@PostMapping("/saveWorkoutHistory.do")
	public String saveWorkoutHistory(@RequestParam("userId") int userId, @RequestParam("level") String levelStr,
			@RequestParam("workoutIds") String workoutIdsStr, @RequestParam("selectedGroup") int selectedGroup,
			Model model) {

		try {
			// 난이도 문자열 → 숫자 변환 (if-else 사용)
			int level = 0; // 기본값 beginner
			if ("intermediate".equals(levelStr)) {
				level = 1;
			} else if ("advanced".equals(levelStr)) {
				level = 2;
			}

			// 오늘 날짜
			LocalDate today = LocalDate.now();

			// workoutIds 파싱
			String[] workoutIdArray = workoutIdsStr.split(",");
			List<Integer> workoutIds = new ArrayList<Integer>();
			for (String idStr : workoutIdArray) {
				workoutIds.add(Integer.parseInt(idStr.trim()));
			}

			sixClubService.insertWorkoutHistoryGroup(userId, today, level, workoutIds);

			model.addAttribute("msg", "운동 플랜이 성공적으로 저장되었습니다.");
			return "/routine/savePlan";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("msg", "운동 기록 저장 중 오류 발생");
			model.addAttribute("url", "routine_select.do");
			return "alert";
		}
	}

	@PostMapping("/saveWorkoutPlan.do")
	   public String saveWorkoutPlan(@SessionAttribute(value = "userNO", required = false) int userNO, HttpSession session,
	         Model model) throws Exception {

	      @SuppressWarnings("unchecked")
	      List<List<WorkoutVO>> routineList = (List<List<WorkoutVO>>) session.getAttribute("routineList");
	      String levelStr = (String) session.getAttribute("level");

	      int woLevel = 0;
	      if ("intermediate".equals(levelStr)) {
	         woLevel = 1;
	      } else if ("advanced".equals(levelStr)) {
	         woLevel = 2;
	      }

	      if (routineList == null || routineList.size() != 6) {
	         model.addAttribute("msg", "세션이 만료되었거나 루틴 정보가 부족합니다.");
	         return "alert";
	      }

	      LocalDate today = LocalDate.now();
	      List<Integer> historyGroupIds = new ArrayList<Integer>();
	      List<Integer> dayOfWeekList = new ArrayList<Integer>();

	      for (int i = 0; i < routineList.size(); i++) {
	          List<WorkoutVO> dailyRoutine = routineList.get(i);
	          LocalDate woDate = today.plusDays(i);

	          // 날짜는 그대로 쓰되 요일 번호는 1~6 고정
	          dayOfWeekList.add(i + 1);
	          
	         // workoutId 리스트 생성
	         List<Integer> woNoList = new ArrayList<Integer>();
	         for (WorkoutVO workout : dailyRoutine) {
	            woNoList.add(workout.getWoNo());
	         }

	         // 새 메서드: group + history 일괄 저장
	         int historyNo = sixClubService.insertWorkoutHistoryGroup(userNO, woDate, woLevel, woNoList);

	         historyGroupIds.add(historyNo);
	      }

	      // 2. WorkoutPlan 저장
	      int planNo = sixClubService.insertWorkoutPlan(userNO);

	      // 3. 요일별 WorkoutPlan 저장
	      for (int i = 0; i < 6; i++) {
	         sixClubService.insertWorkoutPlanDay(planNo, dayOfWeekList.get(i), historyGroupIds.get(i));
	      }

	      model.addAttribute("msg", "운동 플랜이 성공적으로 저장되었습니다.");
	      return "/routine/savePlan";
	   }

	private boolean setUserSessionAndModel(Integer userNO, HttpSession session, Model model) throws Exception {
	      if (userNO == null || userNO == 0) {
	         model.addAttribute("msg", "로그인이 필요한 서비스입니다.");
	         model.addAttribute("url", "login.do");
	         return false;
	      }

	      UsersVO user = sixClubService.getUserinfo(userNO);
	      if (user == null) {
	         model.addAttribute("msg", "사용자 정보를 찾을 수 없습니다.");
	         model.addAttribute("url", "login.do");
	         return false;
	      }

	      if (user.getBirthday() == null) {
	         model.addAttribute("msg", "생년월일 정보가 없습니다.");
	         model.addAttribute("url", "login.do");
	         model.addAttribute("user", user);
	         model.addAttribute("age", 0);
	         return false;
	      }

	      Date birthday = user.getBirthday();
	      LocalDate birthDate = birthday instanceof java.sql.Date ? birthday.toLocalDate()
	            : birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	      LocalDate today = LocalDate.now();
	      int age = today.getYear() - birthDate.getYear();
	      if (birthDate.plusYears(age).isAfter(today))
	         age--;

	      boolean isSubscribed = sixClubService.isSubscribe(userNO);
	      
	      
	      session.setAttribute("age", age);
	      session.setAttribute("isSubscribed", isSubscribed);
	      model.addAttribute("isSubscribed", isSubscribed);
	      model.addAttribute("user", user);
	      model.addAttribute("age", age);
	      return true;
	   }
}
