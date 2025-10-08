package com.mycom.sixclub.util;

import java.time.LocalDate;

import org.springframework.stereotype.Component;
@Component
public class MaxAgeSetting {
	
	public String getMaxBirthDate() {
        LocalDate  today = LocalDate.now();
        LocalDate maxDate = today.minusYears(14);
        return maxDate.toString(); // "YYYY-MM-DD" 형식
    }

}
