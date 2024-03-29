package hello.community.controller.subscribe;


import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hello.community.service.subscribe.SubscribeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SubscribeController {

	private final SubscribeService subscribeService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/board/{boardType}/subscribe")
	public String subscribe(@PathVariable String boardType, HttpServletRequest request){

		subscribeService.write(boardType);
		
		return "redirect:" + request.getHeader("Referer");
	}
	
	@DeleteMapping("/board/{boardType}/subscribe")
	public String unsubscribe(@PathVariable String boardType, HttpServletRequest request){

		subscribeService.delete(boardType);

		return "redirect:" + request.getHeader("Referer");
	}
}
