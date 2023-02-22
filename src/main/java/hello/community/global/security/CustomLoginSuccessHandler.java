package hello.community.global.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	
	private final RequestCache requestCache = new HttpSessionRequestCache();
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
			clearSession(request);
			
			SavedRequest savedRequest = requestCache.getRequest(request, response);
			
			String prevPage = (String) request.getSession().getAttribute("prevPage");
	        if (prevPage != null) {
	            request.getSession().removeAttribute("prevPage");
	        }

	        // 기본 URI
	        String uri = "/";

	        /**
	         * savedRequest 존재하는 경우 = 인증 권한이 없는 페이지 접근
	         * Security Filter가 인터셉트하여 savedRequest에 세션 저장
	         */
	        if (savedRequest != null) {
	            uri = savedRequest.getRedirectUrl();
	        }
			//직접 로그인 버튼을 누른경우
	        else if (prevPage != null && !prevPage.equals("")) {
	            // 회원가입 - 로그인으로 넘어온 경우 "/"로 redirect
	            if (prevPage.contains("/member/signup")) {
	                uri = "/";
	            } else {
	                uri = prevPage;
	            }
	        }

	        redirectStrategy.sendRedirect(request, response, uri);									
	}
	
	// 에러 세션 제거
    protected void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
