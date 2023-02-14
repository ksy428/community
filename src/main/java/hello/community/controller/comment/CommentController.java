package hello.community.controller.comment;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.RestController;

import hello.community.dto.comment.CommentEditDto;
import hello.community.dto.comment.CommentPagingDto;
import hello.community.dto.comment.CommentWriteDto;
import hello.community.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
	
	private final CommentService commentService;

	@GetMapping("/comment/{postId}")
	public ResponseEntity<CommentPagingDto> commentList(Pageable pageable, @PathVariable Long postId,
			@RequestParam(required = false, defaultValue = "1", value = "cp") int commentPage) {
		
		CommentPagingDto commentPagingDto = commentService.searchCommentList(pageable, postId, commentPage);
			
		return new ResponseEntity<>(commentPagingDto, HttpStatus.OK);
	}
	
	@PostMapping(value = {"/comment/{postId}","/comment/{postId}/{commentId}"})
	public ResponseEntity<Long> write(@Valid @RequestBody CommentWriteDto writeDto, BindingResult result
			,@PathVariable Long postId ,@PathVariable(required = false) Optional<Long> commentId) {
		
		if(result.hasErrors()){
			return null;
		}		 		

		return new ResponseEntity<>(commentService.write(postId, commentId, writeDto), HttpStatus.OK);
	}
	
	@PutMapping("/comment/{commentId}")
	public ResponseEntity<Long> edit(@Valid @RequestBody CommentEditDto editDto, BindingResult result
			,@PathVariable Long commentId) {
		
		if(result.hasErrors()){
			return null;
		}
		
		return new ResponseEntity<>(commentService.edit(commentId, editDto), HttpStatus.OK);

	}
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<Long> delete(@PathVariable Long commentId) {
				
		return new ResponseEntity<>(commentService.delete(commentId), HttpStatus.OK);
	}

}
