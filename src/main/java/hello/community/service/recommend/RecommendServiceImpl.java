package hello.community.service.recommend;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.community.dto.post.PostBriefInfo;
import hello.community.exception.json.JsonException;
import hello.community.exception.json.JsonExceptionType;
import hello.community.service.message.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.domain.recommend.Recommend;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.util.SecurityUtil;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.repository.recommend.RecommendRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendServiceImpl implements RecommendService{

	public static final int BEST_RECOMMEND_COUNT = 1;
	private final RecommendRepository recommendRepository;
	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final MessageService messageService;

	private ObjectMapper objectMapper = new ObjectMapper();
	@Override
	@Transactional
	public Long write(Long postId){
		
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
			.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
	
		Optional<Recommend> result = recommendRepository.findByWriterAndPost(member, post);
		
		// 처음 추천했을 경우 
		if(result.isEmpty()) {

			Recommend recommend = new Recommend();

			post.addRecommend();

			bestCheck(post);

			//연관관계메서드
			recommend.setWriter(member);
			recommend.setPost(post);

			recommendRepository.save(recommend);
			
			return post.getRecommend();
		}
		// 중복 추천했을 경우
		else {
			throw new PostException(PostExceptionType.NOT_RECOMMAND_POST);
		}
	}

	private void bestCheck(Post post){
		
		//이미 베스트게시글이면 리턴
		if(post.isBest()) {
			return;
		}
		else {
			//베스트게시글로 승격할 경우
			if (post.getRecommend() >= BEST_RECOMMEND_COUNT) {
				//베스트게시글로 상태값 변경
				post.setBest(LocalDateTime.now());
				try {
					//json 변환
					String json = objectMapper.writeValueAsString(new PostBriefInfo(post));
					//웹소켓으로 메시지알림
					messageService.sendMessage(json);
				}
				catch (JacksonException e){
					throw new JsonException(JsonExceptionType.FAIL_OBJECT_TO_JSON);
				}
			}
		}
	}
}
