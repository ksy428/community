package hello.community.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class HomeController {

	
	@GetMapping("/")
	public String home(HttpSession session
			//, @AuthenticationPrincipal(expression = "#this =='anonymousUser' ? null : member") Member member
			){
		return "home";
	}
	
	@GetMapping("/loginForm")
	public String login(HttpServletRequest request, Model model) {
		
		
		String uri = request.getHeader("Referer");

		if (uri != null && !uri.contains("/login")) {
			request.getSession().setAttribute("prevPage", uri);
		}

		return "login/loginForm";
	}
	


}
