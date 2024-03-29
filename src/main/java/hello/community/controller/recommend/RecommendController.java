package hello.community.controller.recommend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.community.service.recommend.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

	private final RecommendService recommendService;

	@PostMapping("/recommend/{postId}")
	public ResponseEntity<Long> postRecommend(@PathVariable Long postId){

		Long recommendCount = recommendService.write(postId);

		return new ResponseEntity<>(recommendCount, HttpStatus.OK);
	}
}
