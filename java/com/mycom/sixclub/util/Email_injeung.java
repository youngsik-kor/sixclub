package com.mycom.sixclub.util;

import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.mycom.sixclub.service.vo.UsersVO;

@Component
public class Email_injeung {

	@Autowired
	public JavaMailSender mailSender;

	public int Email_Injeung(UsersVO userVO, Model model) throws MessagingException {
		Random r = new Random();
		int num = r.nextInt(999999); // 랜덤난수설정

		String setfrom = "six_club@naver.com"; // naver
		String tomail = userVO.getEmail(); // 받는사람
		String title = userVO.getUser_name() + " 님 비밀번호변경 인증 이메일 입니다";
		String content = "\r\n" + "안녕하세요 회원님" + "\r\n" + "비밀번호찾기 인증번호는 " + num + " 입니다."; // 메일 내용

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

		messageHelper.setFrom(setfrom);
		messageHelper.setTo(tomail);
		messageHelper.setSubject(title);
		messageHelper.setText(content);

		try {
			System.out.println("메일 전송 시작");
			mailSender.send(message);
			System.out.println("메일 전송 완료");
		} catch (MailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute("minjeung_num", num);

		return num;
	}

}
