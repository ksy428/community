package hello.community.controller.post;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hibernate.transform.ToListResultTransformer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.dto.post.PostEditDto;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final PostService postService;
	
	@GetMapping("/board")
	public String postList() {
		
		return "post/listPost";
	}
	
	@GetMapping("/board/write")
	public String writeForm(@ModelAttribute PostWriteDto writeDto) {	
		return "post/writePostForm";
	}
	
	@PostMapping("/board/write")
	@ResponseBody
	public String write(@Valid @ModelAttribute PostWriteDto writeDto, BindingResult result,
			@RequestParam(value = "originNameList[]", required = false) List<String> originNameList,
			@RequestParam(value = "storeNameList[]", required = false) List<String> storeNameList){

		if(result.hasErrors()){
			return "error";
		}
		
		writeDto.initDto(originNameList, storeNameList);

		Long postId = postService.write(writeDto);
		
		return postId.toString();
	}
	
	@GetMapping("/board/view/{postId}")
	public String view(@PathVariable Long postId, Model model) {
		
		PostInfoDto infoDto = postService.view(postId);
		
		model.addAttribute("postInfoDto", infoDto);
		
		return "post/viewPost";
	}
	
	@GetMapping("/board/view/{postId}/edit")
	public String editForm(@PathVariable Long postId, Model model) {
		
		PostEditDto editDto = postService.getEditInfo(postId);
		
		model.addAttribute("postEditDto",editDto);
		
		return "post/editPostForm";
	}
	
	@PutMapping("/board/view/{postId}/edit")
	@ResponseBody
	public String edit(@PathVariable Long postId, @Valid @ModelAttribute PostEditDto editDto, BindingResult result,
			@RequestParam(value = "originNameList[]", required = false) List<String> originNameList,
			@RequestParam(value = "storeNameList[]", required = false) List<String> storeNameList) {
			
		if(result.hasErrors()) {
			return "post/editPostForm";
		}
		
		editDto.setMediaList(postService.findOne(postId).getMediaList());
		
		editDto.inItDto(originNameList, storeNameList);
		
		postService.edit(postId, editDto);
		
		//return "redirect:/board/view/"+ postId;
		return postId.toString();
	}
	
	@DeleteMapping("/board/view/{postId}")
	public String delete(@PathVariable Long postId){
	
		postService.delete(postId);
		
		return "redirect:/board";
	}
}
