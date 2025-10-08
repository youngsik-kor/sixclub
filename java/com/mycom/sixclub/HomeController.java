package com.mycom.sixclub;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.EquipVO;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;
import com.mycom.sixclub.service.vo.WorkoutVO;
import com.mycom.sixclub.util.Email_injeung;
import com.mycom.sixclub.util.MaxAgeSetting;
import com.mycom.sixclub.util.PwEncoding;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Resource(name = "sixclubService") // @Autowired : type
	// ����X spring�� ������
	private SixclubService sixclubService;

	@RequestMapping(value = "/main.do") // main page
	public String main(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {
		
		if (userNO!=null) {
			UsersVO user = sixclubService.getUserinfo(userNO);
			user.setSubscribe(sixclubService.isSubscribe(userNO));
			
			if(user.isSubscribe()) {
				SubscribeVO svo = sixclubService.getSubscribe(userNO);
				svo.setMonthBetween();
				
				model.addAttribute("user", user);
				model.addAttribute("sub",svo);
			}
		}
		
		return "main";
	}

	@Value("${file.upload-dir}")
	private String uploadDir;

	@RequestMapping(value = "/equipList.do")
	public String equipList(Model model) {
		ArrayList<EquipVO> alist = sixclubService.getAllEquip();
		
		for (EquipVO vo : alist) {
			switch (vo.getEquip_no()) {
			case 1:  // 운동복
				vo.setEquip_brand_list(Arrays.asList(
						"Xexymix 여성 피트니스 패션 선두주자, 기능성과 디자인 겸비",
						"HDEX 스트릿 감성과 퍼포먼스를 결합한 국내 브랜드",
						"Under Armour 고기능성 스포츠웨어의 대표 브랜드",
						"Gymshark 젊은 리프터들 사이에서 인기 있는 트렌디 브랜드",
						"Ryderwear 근육 라인을 강조한 보디빌딩 특화 운동복"
					));
				break;
				
			case 4: // 팔꿈치 보호대
				vo.setEquip_brand_list(Arrays.asList(
						"SBD 압박력과 지지력이 뛰어난 고성능 슬리브.",
						"Rehband 착용감과 움직임 자유도를 고려한 인체공학적 설계.",
						"Iron Bull Strength 두툼한 재질로 무거운 중량에서도 안정적.",
						"Strength Shop 파워리프팅 특화, 가격대비 성능 우수.",
						"Gymreapers 실용성과 디자인의 균형을 맞춘 신흥 브랜드."
					));
					break;
					
			case 5: // 손목 보호대
				vo.setEquip_brand_list(Arrays.asList(
					"SBD 파워리프팅 대회 기준 제품, 강한 지지력.",
					"Rogue Fitness 다양한 강도와 내구성 좋은 인기 브랜드.",
					"Strength Shop 가성비 뛰어나고 실전용으로 적합.",
					"Gasp 헤비 리프터용 두꺼운 손목 보호대 제공.",
					"Gymreapers 최근 떠오르는 가성비·디자인 겸비 브랜드."
				));
				break;
			
			case 6: // 무릎 보호대
				vo.setEquip_brand_list(Arrays.asList(
					"SBD 파워리프팅 공식 슬리브, 최고급 압박력 제공.",
					"A7 스쿼터들이 선호하는 강한 압박감.",
					"Mark Bell’s Slingshot 두꺼운 재질, 고중량용 무릎 슬리브.",
					"Rehband 유연성과 착용감 우수한 무릎 슬리브.",
					"Stoic 가성비와 기능을 동시에 잡은 실용적인 선택."
				));
				break;
				
			case 7: // 스트랩
				vo.setEquip_brand_list(Arrays.asList(
					"Versa Grips 스트랩 + 헬스장갑 기능을 동시에, 최고의 그립 보조 도구.",
					"WSF 국내에서 인기 있는 가성비 스트랩.",
					"Rogue Fitness 내구성 뛰어나고 다양한 강도 제공.",
					"IronMind 세계 리프터들이 애용하는 고성능 스트랩.",
					"Gunsmith Fitness 디자인과 실용성의 균형이 뛰어난 스트랩 브랜드."
				));
				break;
			
			case 8: // 리프팅 벨트
				vo.setEquip_brand_list(Arrays.asList(
					"Harbinger 입문자부터 상급자까지 두루 쓰이는 대표 헬스벨트.",
					"Schiek 인체공학적 구조로 편안한 착용감 제공.",
					"SDB 국내 파워리프팅 특화, 튼튼한 지지력.",
					"Inzer IPF 공인, 세계 대회에서 자주 보이는 벨트.",
					"A7 고급 디자인과 강력한 지지력의 프리미엄 브랜드."
				));
				break;
			
			case 9: // 리프팅슈즈
				vo.setEquip_brand_list(Arrays.asList(
					"Zerotohero 가성비 좋고 국내 리프터들 사이에서 입소문 난 신흥 브랜드.",
					"Sabosports 전통 있는 러시아 브랜드, 발 고정력과 접지력 우수.",
					"Adidas Powerlift 전 세계적으로 인기 있는 올림픽 리프팅화.",
					"Nike Romaleos 견고함과 디자인 모두 갖춘 프리미엄 리프팅화.",
					"Reebok Legacy Lifter 고강도 리프팅에 적합한 안정감 있는 슈즈."
				));
				break;

			default:
				vo.setEquip_brand_list(Arrays.asList("브랜드 추천리스트 준비중입니다"));
				break;
			}
		}

		model.addAttribute("alist", alist);
		model.addAttribute("uploadDir", uploadDir);
		return "equipList";
	}

	@RequestMapping(value = "/findGym.do")
	public String findGym(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {
		String address;
		if (userNO != null && userNO != 0) {
			address = sixclubService.getUserinfo(userNO).getAddress();
		} else {
			address = "서울 종로구 종로 133";
		}

		model.addAttribute("address", address);
		return "findGym";
	}

	@RequestMapping("/myPage.do")
	public String myPage(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {
		if (userNO == null || userNO == 0) {
			model.addAttribute("msg", "로그인이 필요한 서비스입니다");
			model.addAttribute("url", "login.do");
			return "alert";
		}

		UsersVO user = sixclubService.getUserinfo(userNO);

		if (user == null) {
			model.addAttribute("msg", "사용자 정보를 찾을 수 없습니다");
			model.addAttribute("url", "login.do");
			return "alert";
		}

		user.setSubscribe(sixclubService.isSubscribe(userNO));
		if (user.isSubscribe()) {
			SubscribeVO svo = sixclubService.getSubscribe(userNO);
			svo.setMonthBetween();
			
			model.addAttribute("badge", true);
			model.addAttribute("end_date", sixclubService.getSubscribe(userNO).getEnd_date());
			model.addAttribute("sub", svo);
		}

		// 1. 최근 플랜 번호 조회
		Integer recentPlanNo = sixclubService.getRecentlyPlanNo(userNO);

		
		// 2. 플랜에 해당하는 히스토리 번호들 조회
		List<Integer> historyNos = sixclubService.getRoutineByPlanNo(recentPlanNo);

		// 3. 루틴 리스트 조회 (요일순 2차원 리스트)
		List<List<WorkoutVO>> routineList = sixclubService.getRoutineListByHistoryNos(historyNos);

		// 4. 날짜 리스트 구성 (루틴에서 woDate 꺼냄)
		List<LocalDate> routineDates = new ArrayList<LocalDate>();
		for (List<WorkoutVO> dayRoutine : routineList) {
			if (dayRoutine != null && !dayRoutine.isEmpty() && dayRoutine.get(0).getWoDate() != null) {
				routineDates.add(dayRoutine.get(0).getWoDate().toLocalDate());
			} else {
				routineDates.add(null);
			}
		}

		model.addAttribute("routineList", routineList);
		model.addAttribute("routineDates", routineDates);
		model.addAttribute("name", user.getUser_name());
		return "/mypage/myPage";
	}

	@RequestMapping("/login.do")
	public String HomeGoGo() {
		return "Login";
	}

	@Autowired
	private MaxAgeSetting maxdate;

	@RequestMapping("/gaip.do")
	public String Gaipgogo(Model model) {
		model.addAttribute("mMaxdate", maxdate.getMaxBirthDate());
		System.out.println(maxdate.getMaxBirthDate());
		return "Gaip";
	}

	@RequestMapping("/search_ID.do")
	public String FindIDgogo() {
		return "Search_ID";
	}

	@RequestMapping("/search_PW.do")
	public String FindPWgogo() {
		return "Search_PW";
	}

	@RequestMapping("/NewPasswd.do")
	public String FindNewPWgogo(@RequestParam("mid") String userId, Model model) {
		String back = "NO";
		model.addAttribute("back", back);
		model.addAttribute("userId", userId);
		return "NewPasswd";
	}

	@RequestMapping("/NewPasswdMy.do")
	public String FindNewPWMyPage(@RequestParam("mid") String userId, Model model) {
		String back = "back";
		model.addAttribute("userId", userId);
		model.addAttribute("back", back);
		return "NewPasswd";
	}

	@Autowired
	private PwEncoding pe1;

	// 로그인: id가져와서 pw맞는지 비교
	@RequestMapping("/verify.do")
	public String Logingogo(@RequestParam("id") String userId, @RequestParam("pw") String passwd, HttpSession session,
			Model model) {

		UsersVO userVO = sixclubService.getUserinfo_id(userId);
		if (userVO != null) {
			String imsi = pe1.getSecurePassword(passwd, userVO.getSalt());

			System.out.println("userId: " + userId);
			System.out.println("입력 pw: " + passwd);
			System.out.println("DB salt: " + userVO.getSalt());
			System.out.println("DB passwd: " + userVO.getPasswd());

			System.out.println("암호화 pw: " + imsi);

			if (userVO.getPasswd().equals(imsi)) {
				// 로그인 성공
				session.setAttribute("userNO", userVO.getUser_no()); // 세션에 사용자 정보 저장
				session.setAttribute("isLogin", true);
				System.out.println("로그인성공");
				if (userVO.getUser_no() == 99999) {
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

				return "redirect:/main.do"; // 홈 화면으로 이동
			} else {
				// 로그인 실패
				model.addAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
				model.addAttribute("url", "login.do");
				return "alert";
			}
		} else {
			// 로그인 실패
			model.addAttribute("msg", "아이디 또는 비밀번호가 틀렸습니다");
			model.addAttribute("url", "login.do");
			return "alert";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/dupli.do", produces = "text/plain; charset=UTF-8")
	public String checkDuplicate(@RequestParam("id") String id) {
		boolean exists = sixclubService.getUserinfo_id(id) != null;

		if (exists) {
			System.out.println("이미 존재하는 아이디");
		}
		return exists ? "이미 존재하는 아이디입니다." : "사용 가능한 아이디입니다.";
	}

	// user 가입
	@RequestMapping(value = "/Gaip.do")
	public String userInsert(@ModelAttribute("userVO") UsersVO userVO, Model model) {

		userVO.setSalt(pe1.generateSalt());
		userVO.setPasswd(pe1.getSecurePassword(userVO.getPasswd(), userVO.getSalt()));
		System.out.println(userVO.getSalt());
		sixclubService.insertUser(userVO);
		return "redirect:/login.do";

	}

	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/main.do";
	}

	// 회원정보 수정
	@RequestMapping(value = "/updateUser.do", method = RequestMethod.GET)
	// 처음 회원정보 수정으로 진입할 떄, 로그인한 사람의 정보를 불러와 입력칸에 초기치로 넣기 위한 코드
	public String updateUser(@SessionAttribute(value = "userNO", required = false) int userNO, Model model) {
		UsersVO user = sixclubService.getUserinfo(userNO);
		SubscribeVO subVO = sixclubService.getSubscribe(userNO); 
		subVO.setMonthBetween();
		model.addAttribute("user", user);
		model.addAttribute("monthBetween", subVO.getMonthBetween());
		return "/mypage/updateUser";
	}

	@RequestMapping(value = "/updateUserInfo.do", method = RequestMethod.POST)
	// 회원정보 수정후 수정 버튼 클릭 시 db 수정을 위한 코드
	public String updateUser(@SessionAttribute(value = "userNO", required = false) Integer userNO,
			@ModelAttribute("userVO") UsersVO userVO) {
		userVO.setUser_no(userNO);
		sixclubService.updateUser(userVO);
		return "redirect:/myPage.do";
	}

	@RequestMapping(value = "/deleteUser.do")
	public String deleteUser(@SessionAttribute(value = "userNO", required = false) Integer userNO,
			HttpSession session) {
		sixclubService.deleteUser(userNO);
		session.invalidate();
		return "redirect:/main.do";

	}

	@RequestMapping(value = "/bmi.do", method = RequestMethod.POST)
	public String bmi(@ModelAttribute("userVO") UsersVO userVO, Model model) {

		int height = userVO.getHeight();
		int weight = userVO.getWeight();
		
		if (height == 0 || weight == 0) {
			model.addAttribute("msg", "키와 몸무게를 정확히 입력해주세요.");
			return "/mypage/bmi";
		}

		String health;
		double bmi = weight / Math.pow((double) height / 100, 2);

		if (bmi < 18.5) {
			health = "저체중";
		} else if (bmi <= 22.9) {
			health = "정상";
		} else if (bmi <= 24.9) {
			health = "과체중";
		} else {
			health = "비만";
		}
		
		
		model.addAttribute("user", userVO);
		model.addAttribute("bmi", String.format("%.2f", bmi));
		model.addAttribute("health", health);
		return "/mypage/bmi";

	}

	@RequestMapping(value = "/goBmi.do", method = RequestMethod.GET)
	public String bmifrom(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {

		UsersVO uvo = sixclubService.getUserinfo(userNO);
		
		model.addAttribute("user", uvo);
		return "/mypage/bmi";

	}
	
	@RequestMapping(value = "/goBmr.do", method = RequestMethod.GET)
	public String bmrfrom(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {

		UsersVO uvo = sixclubService.getUserinfo(userNO);
		
		model.addAttribute("user", uvo);
		model.addAttribute("age", ageCalc(uvo));
		return "/mypage/bmr";

	}
	
	@RequestMapping(value = "/bmr.do", method = RequestMethod.POST)
	public String bmr(@ModelAttribute("userVO") UsersVO userVO, @RequestParam(defaultValue = "0") int age,
			Model model) {

		if (userVO.getHeight() == 0 || userVO.getWeight() == 0 || age == 0) {
			model.addAttribute("msg", "키와 몸무게를 정확히 입력해주세요.");
			return "redirect:/goBmr.do";
		}
		int bmr = 0;
		if(userVO.getGender() == 0) 
			bmr = (int) (66.47 +(13.75*userVO.getWeight()) + (5*userVO.getHeight())- (6.76*age));
		else
			bmr = (int) (544.1+(9.56*userVO.getWeight()) + (1.85*userVO.getHeight())-(4.68*age));
		

		model.addAttribute("user", userVO);
		model.addAttribute("age", age);
		model.addAttribute("bmr", bmr);
		return "/mypage/bmr";

	}
	
	@RequestMapping(value = "/goHeartbeat.do", method = RequestMethod.GET)
	public String heartbeatfrom(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {

		model.addAttribute("age", ageCalc(sixclubService.getUserinfo(userNO)));
		return "/mypage/heartbeat";

	}
	
	@RequestMapping(value = "/heartbeat.do", method = RequestMethod.POST)
	public String heartbeat(@RequestParam(defaultValue = "0") int age, @RequestParam int beat, @RequestParam int power,
			Model model) {
		double powerper = 0;
		switch (power) {
		case 0:
			powerper = 0.5;
			break;
		case 1:
			powerper = 0.6;
			break;
		case 2:
			powerper = 0.7;
			break;
		case 3:
			powerper = 0.8;
			break;
		case 4:
			powerper = 0.9;
			break;

		}
		
		int maxBeat = (int) Math.round(((220-age)*powerper + beat));
		int goalBeat = (int) Math.round(((220-age-beat)*powerper + beat));

		

		model.addAttribute("age", age);
		model.addAttribute("beat", beat);
		model.addAttribute("power", power);
		model.addAttribute("maxBeat", maxBeat);
		model.addAttribute("goalBeat", goalBeat);
		
		return "/mypage/heartbeat";

	}
	



	// 아이디 찾기
	@RequestMapping(value = "/searchId.do", method = RequestMethod.POST)
	public String searchId(@RequestParam("user_name") String name, @RequestParam("phone") String phone,
			@ModelAttribute("userVO") UsersVO userVO, Model model) {
		userVO = sixclubService.getUserinfo_name(name, phone);

		if (userVO != null) {
			String searchedID = userVO.getUser_id();
			model.addAttribute("msearchedID", "회원님의 아이디는 " + searchedID + " 입니다");
		} else {
			System.out.println("존재하지 않는 회원정보");
			model.addAttribute("msearchedID", "회원 정보가 존재하지 않습니다.");
		}
		return "Search_ID";

	}

	@Autowired
	private Email_injeung email_injeung;

	// 비밀번호 찾기중 회원 정보 확인 및 인증번호 전송
	@RequestMapping(value = "/searchPw.do", method = RequestMethod.POST)
	public String searchPw(@RequestParam("user_id") String id, @RequestParam("user_name") String name,
			@RequestParam("phone") String phone, @RequestParam("email") String email,
			@ModelAttribute("userVO") UsersVO userVO, Model model, HttpSession session) throws MessagingException {
		userVO = sixclubService.getUserinfo_id(id);
		// 입력값 복원용
		model.addAttribute("input_id", id);
		session.setAttribute("mid", id);
		model.addAttribute("input_name", name);
		model.addAttribute("input_phone", phone);
		model.addAttribute("input_email", email);

		if (userVO != null) {
			// 입력값이 일치하는지 확인용
			boolean check_name = name.equals(userVO.getUser_name());
			boolean check_phone = phone.equals(userVO.getPhone());
			boolean check_email = email.equals(userVO.getEmail());

			if (check_name && check_phone && check_email) {
				System.out.println("Email_Injeung 메서드 호출 직전");
				int injeung_num = email_injeung.Email_Injeung(userVO, model);
				System.out.println("Email_Injeung 메서드 호출 후, 인증번호: " + injeung_num);

				model.addAttribute("minjeung_num", injeung_num);
			} else if (!check_name) {
				model.addAttribute("mcheck", "이름이 일치하지 않습니다.");
			} else if (!check_phone) {
				model.addAttribute("mcheck", "전화번호가 일치하지 않습니다.");
			} else if (!check_email) {
				model.addAttribute("mcheck", "e-mail 주소가 일치하지 않습니다.");
			} else
				model.addAttribute("mcheck", "에러가 발생하여 초기화면으로 이동합니다.");
		} else {
			model.addAttribute("mcheck", "아이디 정보가 없습니다.");
			System.out.println("VO객체 null");
		}
		return "Search_PW";
	}

	// 새 비밀번호 만들기 전 인증번호 확인
	@RequestMapping(value = "/newPasswd.do", method = RequestMethod.POST)
	public String checkInjeungNum(@RequestParam("injeung_num") Integer userInput,
			@RequestParam("real_injeung_num") Integer realNum, @RequestParam("id") String id, Model model) {
		if (userInput.equals(realNum)) {
			return "redirect:/NewPasswd.do?mid=" + id;
		} else if (!userInput.equals(realNum)) {
			System.out.println("인증번호 일치하지 않음");
			model.addAttribute("mcheck", "인증번호가 틀렸습니다.");
		} else if (userInput == null || realNum == null) {
			System.out.println("인증번호 입력 X || 인증번호 발급X ");
			model.addAttribute("mcheck", "인증번호 인증작업을 수행하세요");
		} else {
			System.out.println("error");
			model.addAttribute("mcheck", "에러가 발생하여 초기화면으로 이동합니다.");
		}
		return "Search_PW";
	}

	// 새 비밀번호 insert - 암호화 메소드 추가 필요!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	@RequestMapping(value = "/update_pw.do")
	public String updateUserinfo(@RequestParam("user_id") String id, @RequestParam("passwd") String pw,
			@RequestParam("pw_re") String pw_re, @RequestParam(value = "back", required = false) String back,
			@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model, HttpSession session) {
		String salt = "";
		String encoding_pw = "";
		System.out.println(id);
		if (pw.equals(pw_re)) {
			salt = pe1.generateSalt();
			encoding_pw = pe1.getSecurePassword(pw, salt);
			sixclubService.updateUser_pwinfo(encoding_pw, salt, id);
			model.addAttribute("msg", "비밀번호가 변경되었습니다.");
		} else {
			model.addAttribute("msg", "비밀번호가 일치하지 않습니다.");
			return "NewPasswd";
		}
		if (back.equals("back")) {
			session.removeAttribute("user_id");
			UsersVO user = sixclubService.getUserinfo(userNO);
			model.addAttribute("user", user);
			return "/mypage/updateUser";

		} else {
			session.removeAttribute("user_id");
			return "Login";
		}
	}
	
	int ageCalc(UsersVO uvo) {
		Date birthday = uvo.getBirthday();
	      LocalDate birthDate = birthday instanceof java.sql.Date ? birthday.toLocalDate()
	            : birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	      LocalDate today = LocalDate.now();
	      int age = today.getYear() - birthDate.getYear();
	      if (birthDate.plusYears(age).isAfter(today))
	         age--;
	      
		return age;
	}

}
