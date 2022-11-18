package hello.community;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDummyData {
	
	private final Init init;
	
	@PostConstruct
	public void init() {
		init.save();
	}
	
	@Component
	@RequiredArgsConstructor
	private static class Init{
		
		private final MemberRepository memberRepository;
		private final PostRepository postRepository;
		private final PasswordEncoder passwordEncoder;
		
		@Transactional
	    public void save() {
			 //initMember("tpdud0428", "1234", "세영짱짱", "tpdud0428@naver.com");
			 initMember("test", "1234", "test짱짱", "test@naver.com");
			 initMember("dummy1", "1234", "dummy짱짱1", "dummy1@naver.com");
			 initMember("dummy2", "1234", "dummy짱짱2", "dummy2@naver.com");
			 initMember("dummy3", "1234", "dummy짱짱3", "dummy3@naver.com");
		
			 initPost("1번글", "내용무", 1L);
			 initPost("2번글", "내용무", 1L);
			 initPost("3번글", "내용무", 1L);
			 initPost("4번글", "내용무", 2L);
			 initPost("5번글", "내용무", 3L);
			 initPost("6번글", "내용무", 4L);		
	    }

		@Transactional
		public void initMember(String loginId, String password, String nickname, String email) {
			 memberRepository.save(Member.builder()			
						.loginId(loginId)
						.password(passwordEncoder.encode(password))
						.nickname(nickname)
						.email(email)
						.build());
		}
		
		@Transactional
		public void initPost(String title, String content,Long memberId) {
			Post post = Post.builder().title(title).content(content).build();
			post.setWriter(memberRepository.findById(memberId).
					orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));
			
			postRepository.save(post);
		}	
	}
}



