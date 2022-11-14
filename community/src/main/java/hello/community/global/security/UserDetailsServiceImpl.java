package hello.community.global.security;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hello.community.domain.member.Member;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService{

	private final MemberRepository memberRepository;
	private final HttpSession session;
	
	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
		
		Member member = memberRepository.findByLoginId(loginId)
				.orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
			
		session.setAttribute("member", member);
		
		return new UserDetailsImpl(member);
			
	}

}
