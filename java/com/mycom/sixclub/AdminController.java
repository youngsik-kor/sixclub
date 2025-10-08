package com.mycom.sixclub;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.EquipVO;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutHistoryGroupVO;
import com.mycom.sixclub.service.vo.WorkoutVO;

@Controller
public class AdminController {
	@Resource(name = "sixclubService")
	private SixclubService sixclubService;

	@RequestMapping(value = "/adminMain.do")
	public String adminMain(Model model) {
		UsersVO vo1 = null;
		// 회원수 전달
		model.addAttribute("mUsersCount", sixclubService.getAllUserinfo(vo1).size());
		// 구독자수 전달
		model.addAttribute("mSubSoo", sixclubService.getSubscribeUserNum());
		// 재구독율 전달
		Integer total = sixclubService.getAllSubscribeNum();
		Integer re = sixclubService.getReSubscribeNum();
		if (re != null && total != null) {
			int ratio = (int) (((double) re / total) * 100);
			System.out.println(re);
			System.out.println(total);
			System.out.println(ratio);
			model.addAttribute("mreSubscribeRatio", ratio);
		} else
			model.addAttribute("mreSubscribeRatio", 0);
		// 최근 구독 회원 전달
		model.addAttribute("mNewSubscribe", sixclubService.getNewSubscribe());
		return "/admin/adminMain";
	}

	@RequestMapping("/getUserList.do")
	public String getList(@RequestParam(value = "spage", required = false, defaultValue = "1") int spage, UsersVO vo1,
	                      Model model) {

	    int listsPerPage = 10; // 한 페이지에 보여줄 게시글 수
	    int totalLists = sixclubService.getAllUserinfo(vo1).size();
	    int totalPages = (int) Math.ceil((double) totalLists / listsPerPage);

	    int startRow = (spage - 1) * listsPerPage + 1;
	    int endRow = spage * listsPerPage;

	    ArrayList<UsersVO> usersLists = sixclubService.getAllinfo_pag(startRow, endRow);
	    for (UsersVO uvo : usersLists) {
	        uvo.setSubscribe(sixclubService.isSubscribe(uvo.getUser_no()));
	    }

	    // 이동식 페이지 블럭 처리 (spage 중심으로 최대 10개만 보이게)
	    int blockSize = 10;
	    int startPage = Math.max(1, spage - blockSize / 2);
	    int endPage = startPage + blockSize - 1;

	    if (endPage > totalPages) {
	        endPage = totalPages;
	        startPage = Math.max(1, endPage - blockSize + 1);
	    }

	    ArrayList<Integer> pageNumbers = new ArrayList<Integer>();
	    for (int i = startPage; i <= endPage; i++) {
	        pageNumbers.add(i);
	    }

	    model.addAttribute("mUsersCount", sixclubService.getAllUserinfo(vo1).size());
	    model.addAttribute("usersLists", usersLists);
	    model.addAttribute("currentPage", spage);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("pageNumbers", pageNumbers);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    return "/admin/userList";
	}

	@RequestMapping(value = "/showStatics.do")
	public String showStatics(UsersVO uvo, SubscribeVO svo, WorkoutHistoryGroupVO whgvo, Model model) {
		List<UsersVO> users = sixclubService.getAllUserinfo(uvo);
		List<SubscribeVO> subscribes = sixclubService.getAllSubscribe(svo);
		List<WorkoutHistoryGroupVO> historygroup = sixclubService.getAllWorkoutHistoryGroup(whgvo);
		int sixRoutineCount = sixclubService.countWorkoutplan();
		int allRoutineCount = sixclubService.countWorkouthistorygroup();
		model.addAttribute("allRoutineCount", allRoutineCount);
		model.addAttribute("sixRoutineCount", sixRoutineCount);
		model.addAttribute("users", users);
		model.addAttribute("subscribe", subscribes);
		model.addAttribute("historygroup", historygroup);
		return "/admin/statics";
	}

	@GetMapping("/admin/statics") // 선택 년도별 월별 사용자를 구하기 위한 코드
	@ResponseBody
	public Map<String, Integer> getDateStatistics(@RequestParam("year") int year, SubscribeVO svo) {

		List<SubscribeVO> allSubscribes = sixclubService.getAllSubscribe(svo);

		Map<String, Integer> monthlyData = new HashMap<String, Integer>();
		// 모든 월을 0으로 초기화
		monthlyData.put("january", 0);
		monthlyData.put("february", 0);
		monthlyData.put("march", 0);
		monthlyData.put("april", 0);
		monthlyData.put("may", 0);
		monthlyData.put("june", 0);
		monthlyData.put("july", 0);
		monthlyData.put("august", 0);
		monthlyData.put("september", 0);
		monthlyData.put("october", 0);
		monthlyData.put("november", 0);
		monthlyData.put("december", 0);

		if (allSubscribes != null) {
			for (SubscribeVO subscribe : allSubscribes) {
				Date startDate = subscribe.getStart_date();
				if (startDate != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);

					// 구독 시작 연도가 요청된 year과 일치하는지 확인
					if (cal.get(Calendar.YEAR) == year) {
						int month = cal.get(Calendar.MONTH); // Calendar.MONTH는 0부터 시작합니다 (0=1월, 1=2월, ...).

						// 해당 월의 카운트를 증가시킵니다.
						switch (month) {
						case 0:
							monthlyData.put("january", monthlyData.get("january") + 1);
							break;
						case 1:
							monthlyData.put("february", monthlyData.get("february") + 1);
							break;
						case 2:
							monthlyData.put("march", monthlyData.get("march") + 1);
							break;
						case 3:
							monthlyData.put("april", monthlyData.get("april") + 1);
							break;
						case 4:
							monthlyData.put("may", monthlyData.get("may") + 1);
							break;
						case 5:
							monthlyData.put("june", monthlyData.get("june") + 1);
							break;
						case 6:
							monthlyData.put("july", monthlyData.get("july") + 1);
							break;
						case 7:
							monthlyData.put("august", monthlyData.get("august") + 1);
							break;
						case 8:
							monthlyData.put("september", monthlyData.get("september") + 1);
							break;
						case 9:
							monthlyData.put("october", monthlyData.get("october") + 1);
							break;
						case 10:
							monthlyData.put("november", monthlyData.get("november") + 1);
							break;
						case 11:
							monthlyData.put("december", monthlyData.get("december") + 1);
							break;
						}
					}
				}
			}
		}
		return monthlyData; // 월별 집계된 데이터를 JSON 형태로 반환합니다.
	}

	@RequestMapping(value = "/goRoutineEdit.do")
	public String goRoutineEdit(@RequestParam(defaultValue = "1") int workoutPage,
			@RequestParam(defaultValue = "1") int equipPage, @RequestParam(required = false) Integer woType,
			Model model) {

		int pageSize = 10;

		int workoutOffset = (workoutPage - 1) * pageSize;
		int equipOffset = (equipPage - 1) * pageSize;

		List<WorkoutVO> pagedWorkout;
		int totalWorkoutCount;

		// woType이 null이면 전체 운동, 있으면 해당 타입만
		if (woType != null) {
			pagedWorkout = sixclubService.getWorkoutPagingByType(woType, workoutOffset, pageSize);
			totalWorkoutCount = sixclubService.getWorkoutCountByType(woType);
		} else {
			pagedWorkout = sixclubService.getWorkoutPaging(workoutOffset, pageSize);
			totalWorkoutCount = sixclubService.getWorkoutCount();
		}

		List<EquipVO> pagedEquip = sixclubService.getEquipPaging(equipOffset, pageSize);
		int totalEquipCount = sixclubService.getEquipCount();

		int totalWorkoutPages = (int) Math.ceil((double) totalWorkoutCount / pageSize);
		int totalEquipPages = (int) Math.ceil((double) totalEquipCount / pageSize);

		model.addAttribute("pagedWorkout", pagedWorkout);
		model.addAttribute("pagedEquip", pagedEquip);

		model.addAttribute("workoutPage", workoutPage);
		model.addAttribute("equipPage", equipPage);

		model.addAttribute("totalWorkoutPages", totalWorkoutPages);
		model.addAttribute("totalEquipPages", totalEquipPages);

		model.addAttribute("selectedWoType", woType);
		return "/admin/routineEdit";
	}

	@RequestMapping(value = "/workoutEdit.do")
	public String workoutEdit(@RequestParam("woNo") int woNo, HttpSession session, Model model) {

		List<WorkoutVO> workout = sixclubService.getWorkout(woNo);

		model.addAttribute("workout", workout);

		return "/admin/workoutEdit";
	}

	@RequestMapping(value = "/equipEdit.do")
	public String equipEdit(@RequestParam("equip_no") int equip_no, HttpSession session, Model model) {

		List<EquipVO> equip = sixclubService.getEquip(equip_no);

		model.addAttribute("equip", equip);
		return "admin/equipEdit";
	}

	@RequestMapping(value = "/insertAction.do")
	public String isnertAction(@RequestParam("action") String action) {

		if ("운동추가하기".equals(action)) {
			return "admin/insertWorkout";
		} else if ("장비추가하기".equals(action)) {
			/* sixclubService.insertEquip(); */
			return "admin/insertEquip";
		}
		return "redirect:/routineEdit";
	}

	@RequestMapping(value = "/workoutAction.do")
	public String workoutAction(@RequestParam("woNo") int woNo, @RequestParam("woType") int woType,
			@RequestParam("woName") String woName, @RequestParam("action") String action) {

		if ("수정하기".equals(action))
			sixclubService.updateWorkout(woNo, woType, woName);
		if ("삭제하기".equals(action))
			sixclubService.deleteWorkout(woNo);

		return "redirect:/goRoutineEdit.do";
	}

	@Value("${file.upload-dir}")
	private String uploadDir;

	@RequestMapping(value = "/equipAction.do", method = RequestMethod.POST)
	public String equipAction(@RequestParam("equip_no") int equip_no,
	                          @RequestParam("equip_name") String equip_name,
	                          @RequestParam("equip_desc") String equip_desc,
	                          @RequestParam(value = "equip_img", required = false) MultipartFile equip_img,
	                          @RequestParam("action") String action,
	                          @RequestParam(value = "old_img", required = false) String oldImgPath,
	                          Model model) throws IOException {

	    String equipDir = uploadDir + "/equip";
	    String newImgPath = null;

	    if ("수정하기".equals(action)) {
	        // 1. 새 이미지가 업로드된 경우
	        if (equip_img != null && !equip_img.isEmpty()) {
	            File uploadPath = new File(equipDir);
	            if (!uploadPath.exists()) {
	                uploadPath.mkdirs();
	            }

	            String filename = equip_img.getOriginalFilename();
	            try {
	                File dest = new File(equipDir, filename);
	                equip_img.transferTo(dest);
	                newImgPath = "/equip/" + filename;  // 새 이미지 경로
	            } catch (IOException e) {
	                e.printStackTrace();
	                model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
	                return "redirect:/equipEdit.do";
	            }
	        } else {
	            // 2. 이미지가 없으면 기존 경로 유지
	            newImgPath = oldImgPath;
	        }

	        sixclubService.updateEquip(equip_no, equip_name, equip_desc, newImgPath);
	    }

	    if ("삭제하기".equals(action)) {
	        sixclubService.deleteEquip(equip_no);
	    }

	    return "redirect:/goRoutineEdit.do";
	}

	@RequestMapping(value = "/insertWorkout.do")
	public String isnertWorkout(@RequestParam("woName") String woName, @RequestParam("woType") int woType) {
		sixclubService.insertWorkout(woName, woType);

		return "redirect:/goRoutineEdit.do";
	}

	@RequestMapping(value = "/insertEquip.do", method = RequestMethod.POST)
	public String insertEquip(@RequestParam("equip_name") String equip_name,
			@RequestParam("equip_desc") String equip_desc,
			@RequestParam(value = "equip_img", required = false) MultipartFile equip_img, HttpServletRequest request,
			Model model) throws IOException {

		// 첨부파일 처리
		String filename = null;
		String equipDir = uploadDir + "/equip";
		if (equip_img != null && !equip_img.isEmpty()) {
			File uploadPath = new File(equipDir);
			if (!uploadPath.exists()) {
				uploadPath.mkdirs();
			}

			filename = equip_img.getOriginalFilename();
			try {
				File dest = new File(equipDir, filename);
				equip_img.transferTo(dest);
			} catch (IOException e) {
				e.printStackTrace();
				model.addAttribute("msg", "파일 업로드 중 오류가 발생했습니다.");
				return "redirect:/equipEdit.do";
			}
		}

		sixclubService.insertEquip(equip_name, equip_desc, "/equip/" + filename);

		return "redirect:/goRoutineEdit.do";
	}
}
