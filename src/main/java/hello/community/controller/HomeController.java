package hello.community.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hello.community.dto.post.PostPagingDto;
import hello.community.repository.post.PostSearch;
import hello.community.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
	
	private final PostService postService;
	
	@GetMapping("/")
	public String home(@ModelAttribute PostSearch postSearch, Model model, Pageable pageable
			,@RequestParam(required = false, defaultValue = "1", value = "p") int page) {
			//, @AuthenticationPrincipal(expression = "#this =='anonymousUser' ? null : member") Member member){
	
		postSearch.setMode("best");
		
		PostPagingDto postPagingDto = postService.searchPostList(pageable, "main", postSearch, page);

		model.addAttribute("postPagingDto",postPagingDto);
		
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
