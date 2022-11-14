package hello.community.global.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil{

	public static String getLoginMemberId() throws MemberException {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		if(principal.equals("anonymousUser")) {
			throw new MemberException(MemberExceptionType.NOT_LOGIN);
		}
		
		UserDetails loginMember = (UserDetails) principal;
		
		return loginMember.getUsername();
	}
	
}
