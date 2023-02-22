package hello.community.global.security;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler{

	//private final RequestCache requestCache = new HttpSessionRequestCache();
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String errorMessage;
		if (exception instanceof BadCredentialsException || exception instanceof InternalAuthenticationServiceException){
			errorMessage="아이디 또는 비밀번호가 맞지 않습니다";
		}
		else if (exception instanceof UsernameNotFoundException){
			errorMessage="존재하지 않는 아이디 입니다";
		}
		else{
			errorMessage="로그인에 실패했습니다";
		}

		clearSession(request);

		errorMessage= URLEncoder.encode(errorMessage,"UTF-8");

		redirectStrategy.sendRedirect(request, response, "/loginForm?error=true&exception=" + errorMessage);
	}
	
	// 에러 세션 제거
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
