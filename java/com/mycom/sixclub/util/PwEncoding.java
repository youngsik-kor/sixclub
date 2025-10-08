package com.mycom.sixclub.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class PwEncoding {

	// SHA-256 암호화 메서드
	public static String sha256(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hash = md.digest(input.getBytes("UTF-8"));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// salt 생성 메서드
	public static String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt); // 문자열로 변환해서 저장하기 편하게
	}

	// pw+salt SHA-256처리
	public static String getSecurePassword(String password, String salt) {
		String saltedPassword = password + salt;
		// 기존 SHA-256 해싱 함수에 saltedPassword 넣어서 해싱
		return sha256(saltedPassword); // sha256은 앞서 만든 해싱 메서드
	}
}
