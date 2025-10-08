package com.mycom.sixclub;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutHistoryGroupVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

@Controller
public class HistoryController {
	@Resource(name = "sixclubService")
	private SixclubService sixclubService;

	// 6일치 history

	@RequestMapping("/historysixday.do")
	public String historyAllPlans(@SessionAttribute(value = "userNO", required = false) int userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		// 1. 해당 사용자의 모든 plan_no 조회
		List<Integer> planNos = sixclubService.getAllPlanNosByUser(userNO);
		if (planNos == null || planNos.isEmpty()) {
			model.addAttribute("msg", "저장된 운동 계획이 없습니다.");
			model.addAttribute("url", "myPage.do");
			return "alert";
		}

		// 2. 각 plan_no에 대해 6일 루틴 추출
		List<List<List<WorkoutVO>>> allRoutineList = new ArrayList<List<List<WorkoutVO>>>();
		List<String> planStartDates = new ArrayList<String>();

		for (Integer planNo : planNos) {
			List<Integer> historyNos = sixclubService.getHistoryNosByPlanNo(planNo);
			List<List<WorkoutVO>> routineList = sixclubService.getRoutineListByHistoryNos(historyNos);

			String dateStr = routineList.get(0).get(0).getWoDate().toString();

			planStartDates.add(dateStr);
			allRoutineList.add(routineList);
		}

		model.addAttribute("allRoutineList", allRoutineList);
		model.addAttribute("planStartDates", planStartDates);
		return "mypage/history_sixday";
	}

	@RequestMapping("/historyday.do")
	public String historydayPlans(@SessionAttribute(value = "userNO", required = false) int userNO, HttpSession session,
			Model model) throws Exception {
		if (!setUserSessionAndModel(userNO, session, model))
			return "alert";

		List<WorkoutHistoryGroupVO> RoutineList = sixclubService.selectOneDayRoutineHistoryGroups(userNO);

		if (RoutineList == null || RoutineList.isEmpty()) {
			model.addAttribute("msg", "저장된 운동 계획이 없습니다.");
			model.addAttribute("url", "myPage.do");
			return "alert";
		}

		model.addAttribute("RoutineList", RoutineList);
		return "mypage/history_day";
	}

	private boolean setUserSessionAndModel(Integer userNO, HttpSession session, Model model) throws Exception {
		if (userNO == null || userNO == 0) {
			model.addAttribute("msg", "로그인이 필요한 서비스입니다.");
			model.addAttribute("url", "login.do");
			return false;
		}

		UsersVO user = sixclubService.getUserinfo(userNO);
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

		boolean isSubscribed = sixclubService.isSubscribe(userNO);

		session.setAttribute("age", age);
		session.setAttribute("isSubscribed", isSubscribed);
		model.addAttribute("isSubscribed", isSubscribed);
		model.addAttribute("user", user);
		model.addAttribute("age", age);
		return true;
	}
}
