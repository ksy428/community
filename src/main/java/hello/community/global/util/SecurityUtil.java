package hello.community.global.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityUtil{

	/*@Autowired
	AuthenticationManager authenticationManager;*/

	public static String getLoginMemberId() throws MemberException {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
		if(principal.equals("anonymousUser")) {
			throw new MemberException(MemberExceptionType.NOT_LOGIN);
		}
		
		UserDetails loginMember = (UserDetails) principal;
		
		return loginMember.getUsername();
	}
}
