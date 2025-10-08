package com.mycom.sixclub;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.mycom.sixclub.service.SixclubService;
import com.mycom.sixclub.service.vo.SubscribeVO;
import com.mycom.sixclub.service.vo.UsersVO;

@Controller
public class SubscribeController {
	@Resource(name = "sixclubService")
	private SixclubService sixclubService;

	// subScribe에 처음 들어올 떄
	@RequestMapping(value = "/subScribe.do")
	public String subscribeform(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {
		UsersVO user = sixclubService.getUserinfo(userNO);
		SubscribeVO subscribe = sixclubService.getSubscribe(userNO);
		List<SubscribeVO> subscribeHis = sixclubService.getSubscribeHistory(userNO);

		model.addAttribute("user", user);
		model.addAttribute("subscribe", subscribe);
		model.addAttribute("subHis", subscribeHis);

		return "/mypage/subScribe";
	}

	// 구독 성공했을 때 구독 날짜를 db에 갱신하기 위해
	@RequestMapping(value = "/SubScribes.do", method = RequestMethod.POST)
	@ResponseBody
	public String subScribe(@RequestBody SubscribeVO vo) {

		sixclubService.subSave(vo);

		return "redirect:/subScribe.do";
	}
	
	@RequestMapping(value="/subscribeHistory.do")
	public String subscribeHistory(@SessionAttribute(value = "userNO", required = false) Integer userNO, Model model) {
		List <SubscribeVO> subscribeHis = sixclubService.getSubscribeHistory(userNO);
		
		
		//change
		for (SubscribeVO vo : subscribeHis) {
		    vo.setMonthBetween();  // 구독 개월 수 계산 setter 직접 호출
		}
		
		model.addAttribute("user_name", sixclubService.getUserinfo(userNO).getUser_name());
		model.addAttribute("subHis", subscribeHis);
		

		return "/mypage/subscribeHistory";
	}
}
