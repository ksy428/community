package hello.community.service.recommend;

import java.util.Optional;

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

	private final RecommendRepository recommendRepository;
	
	private final MemberRepository memberRepository;
	
	private final PostRepository postRepository;

	@Override
	@Transactional
	public Long write(Long postId){
		
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
			.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
	
		Optional<Recommend> result = recommendRepository.findByMemberAndPost(member, post);
		
		// 처음 추천했을 경우 
		if(result.isEmpty()) {
			Recommend recommend = Recommend.builder()
									.member(member)
									.post(post)
									.build();			
			post.addRecommend();			
			recommendRepository.save(recommend);
			
			return post.getRecommend();
		}
		// 중복 추천했을 경우
		else {
			throw new PostException(PostExceptionType.NOT_RECOMMAND_POST);
		}
	}
}
