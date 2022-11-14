package hello.community.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


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
	public String login(){		
		return "login/loginForm";
	}

}
