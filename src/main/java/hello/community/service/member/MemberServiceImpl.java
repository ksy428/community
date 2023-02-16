package hello.community.service.member;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.comment.Comment;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.member.MemberSignUpDto;
import hello.community.dto.member.PasswordEditDto;
import hello.community.dto.post.PostBriefInfo;
import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.dto.comment.CommentBriefInfo;
import hello.community.dto.member.MemberEditDto;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.global.util.SecurityUtil;
import hello.community.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService{

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;


	@Override
	public void signUp(MemberSignUpDto signUpDto) {

		signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

		Member member = signUpDto.toEntity();

		memberRepository.save(member);
	}

	@Override
	@Transactional
	public void editInfo(MemberEditDto editInfoDto){

		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		member.editNickname(editInfoDto.getNickname());
		member.editEmail(editInfoDto.getEmail());
		
		// 회원정보 변경 후 세션 갱신
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials()));
		SecurityContextHolder.getContext().setAuthentication(newAuth);

	}

	@Override
	@Transactional
	public void editPassword(PasswordEditDto editPWDto){

		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		/*// 현재 비밀번호 틀림
		if (!passwordEncoder.matches(editPWDto.getOriginPassword(), member.getPassword())) {
			throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
		}*/

		member.editPassword(passwordEncoder.encode(editPWDto.getNewPassword()));

		// 비밀번호 변경 후 세션 갱신
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication newAuth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getPrincipal(), editPWDto.getNewPassword()));
		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	@Override
	@Transactional
	public void withdraw(String passwordCheck){

		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		if (!passwordEncoder.matches(passwordCheck, member.getPassword())) {
			throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
		}

		//회원탈퇴후 세션 클리어
		memberRepository.delete(member);
		SecurityContextHolder.clearContext();
	}

	@Override
	public MemberInfoDto getInfo(String nickName){

		Member member = memberRepository.findByNickname(nickName)
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
				
		return new MemberInfoDto(member);
	}

	@Override
	public MemberEditDto getEditInfo(){

		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		return new MemberEditDto(member);
	}

	@Override
	public boolean isExistLoginId(String loginId) {
		return memberRepository.findByLoginId(loginId).isPresent();
	}

	@Override
	public boolean isExistNickname(String nickname) {
		return memberRepository.findByNickname(nickname).isPresent();
	}

	@Override
	public boolean isExistEmail(String email) {
		return memberRepository.findByEmail(email).isPresent();
	}
	@Override
	public boolean isEditNickname(String nickname){
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		return !member.getNickname().equals(nickname);
	}
	@Override
	public boolean isEditEmail(String email){
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		return !member.getEmail().equals(email);
	}

	@Override
	public boolean matchPassword(String password) {

		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		if (passwordEncoder.matches(password, member.getPassword())) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public List<SubscribeInfoDto> getSubscribeList() {
			
		List<SubscribeInfoDto> subscribeList = new ArrayList<>();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!principal.equals("anonymousUser")) {
			UserDetails loginMember = (UserDetails) principal;
			
			Member member = memberRepository.findByLoginId(loginMember.getUsername())
							.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
				
			subscribeList = member.getSubscribeList().stream().map( subscribe -> new SubscribeInfoDto(subscribe.getBoard())).collect(Collectors.toList());
		}
		return subscribeList;
	}
}
