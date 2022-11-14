package hello.community;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import hello.community.domain.member.Member;
import hello.community.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDummyData {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@PostConstruct
    public void init() {
		 memberRepository.save(Member.builder()			
				.loginId("tpdud0428")
				.password(passwordEncoder.encode("1234"))
				.nickname("세영짱짱")
				.email("tpdud0428@naver.com")
				.build());
    }

}



