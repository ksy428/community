package hello.community.controller.post;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hello.community.service.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostServiceImpl postService;
	
	@GetMapping("/write")
	public String writeForm() {
		
		return "post/writeForm";
	}
	
	@PostMapping("/write")
	public String write() {
		
		return "redirect:/";
	}
}
