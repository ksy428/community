package hello.community.controller.post;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import hello.community.dto.comment.CommentPagingDto;
import hello.community.dto.post.PostBriefInfo;
import hello.community.dto.post.PostEditDto;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostPagingDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.repository.post.PostSearch;
import hello.community.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

	private final PostService postService;
	
	@GetMapping("/board/{boardType}")
	public String postListForm(@ModelAttribute PostSearch postSearch, Model model, Pageable pageable,
			@PathVariable String boardType,
			@RequestParam(required = false, defaultValue = "1", value = "p") int page) {

		PostPagingDto postPagingDto = postService.searchPostList(pageable, boardType, postSearch, page);
			
		model.addAttribute("postPagingDto",postPagingDto);
		
		return "post/listPost";
	}
	
	
	@GetMapping("/board/{boardType}/write")
	public String writeForm(@ModelAttribute PostWriteDto writeDto) {	
		return "post/writePostForm";
	}
	
	@PostMapping("/board/{boardType}/write")
	@ResponseBody
	public ResponseEntity<Long> write(@Valid @RequestBody PostWriteDto writeDto, BindingResult result, @PathVariable String boardType){

		if(result.hasErrors()){
			//return "error";
		}
		
		writeDto.initDto(boardType);

		Long postId = postService.write(writeDto);
		
		return new ResponseEntity<>(postId, HttpStatus.OK);
	}
	
	@GetMapping("/board/{boardType}/{postId}")
	public String view(@PathVariable Long postId, Model model) {
		
		PostInfoDto infoDto = postService.view(postId);
		
		model.addAttribute("postInfoDto", infoDto);
		
		return "post/viewPost";
	}
	
	@GetMapping("/board/{boardType}/{postId}/edit")
	public String editForm(@PathVariable Long postId, Model model) {
		
		PostEditDto editDto = postService.getEditInfo(postId);
		
		model.addAttribute("postEditDto",editDto);
		
		return "post/editPostForm";
	}
	
	@ResponseBody
	@PutMapping("/board/{boardType}/{postId}/edit")
	public ResponseEntity<Long> edit(@PathVariable Long postId, @Valid @RequestBody PostEditDto editDto, BindingResult result) {
			
		if(result.hasErrors()) {
			//return "post/editPostForm";
		}
		
		editDto.setMediaList(postService.findOne(postId).getMediaList());
		
		editDto.inItDto();
		
		postService.edit(postId, editDto);
		
		return new ResponseEntity<>(postId, HttpStatus.OK);
	}
	
	@DeleteMapping("/board/{boardType}/{postId}")
	public String delete(@PathVariable String boardType, @PathVariable Long postId){
	
		postService.delete(postId);
		
		return "redirect:/board/"+ boardType;
	}
	
	@ResponseBody
	@GetMapping("/board/{boardType}/list")
	public ResponseEntity<PostPagingDto> postList(@ModelAttribute PostSearch postSearch, Pageable pageable,
			@PathVariable String boardType,
			@RequestParam(required = false, defaultValue = "1", value = "p") int page) {

		PostPagingDto postPagingDto = postService.searchPostList(pageable, boardType, postSearch, page);				
		
		return new ResponseEntity<>(postPagingDto, HttpStatus.OK);
	}	
}
