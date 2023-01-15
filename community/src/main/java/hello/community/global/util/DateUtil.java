package hello.community.global.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

	public static final int SEC = 60;
	public static final int MIN = 60;
	public static final int HOUR = 24;
	public static final int DAY = 30;
	public static final int MONTH = 12;
	
	public static String calculateDate(LocalDateTime date) {
		
		LocalDateTime now = LocalDateTime.now();
		
		Long diffTime = date.until(now, ChronoUnit.SECONDS);
		
		String msg = null;
		
		if(diffTime < SEC) {
			msg = "방금 전";
		}
		else if((diffTime /= SEC) < MIN){
			msg = diffTime + "분 전";
		}
		else if((diffTime /= MIN) < HOUR) {
		//else if((diffTime /= MIN) < HOUR && now.truncatedTo(ChronoUnit.DAYS).compareTo(date.truncatedTo(ChronoUnit.DAYS)) == 0){
			//msg = diffTime + "시간 전";
			msg = date.format(DateTimeFormatter.ofPattern("HH:mm"));
		}
		else if((diffTime /= HOUR) < DAY){
			//msg = diffTime + "일 전";
			msg = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
		else {
			msg = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}		
		return msg;
	}
}
