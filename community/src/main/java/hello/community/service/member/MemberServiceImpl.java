package hello.community.service.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.member.Member;
import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.member.MemberSignUpDto;
import hello.community.dto.member.PasswordEditDto;
import hello.community.dto.subscribe.SubscribeInfoDto;
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

	@Override
	public void signUp(MemberSignUpDto signUpDto) {

		signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

		Member member = signUpDto.toEntity();

		memberRepository.save(member);
	}

	@Override
	@Transactional
	public void editInfo(MemberEditDto editInfoDto) throws MemberException {

		Member member = findOne(SecurityUtil.getLoginMemberId());

		member.editNickname(editInfoDto.getNickname());
		member.editEmail(editInfoDto.getEmail());
	}

	@Override
	@Transactional
	public void editPassword(PasswordEditDto editPWDto) throws MemberException {

		Member member = findOne(SecurityUtil.getLoginMemberId());

		// 현재 비밀번호 틀림
		if (!passwordEncoder.matches(editPWDto.getOriginPassword(), member.getPassword())) {
			throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
		}

		member.editPassword(passwordEncoder.encode(editPWDto.getNewPassword()));
	}

	@Override
	public MemberInfoDto getInfo() throws MemberException {

		Member member = findOne(SecurityUtil.getLoginMemberId());

		return new MemberInfoDto(member);
	}

	@Override
	public MemberEditDto getEditInfo() throws MemberException {

		Member member = findOne(SecurityUtil.getLoginMemberId());

		return new MemberEditDto(member);
	}

	@Override
	public boolean isExistLoginId(String loginId) {
		return memberRepository.findByLoginId(loginId).isPresent();
	}

	@Override
	public boolean isExistNickname(String newNickname) {
		Optional<Member> result = memberRepository.findByNickname(newNickname);
		if (result.isEmpty()) {
			return false;
		}
		String originNickname = result.get().getNickname();
		if (originNickname.equals(newNickname)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isExistEmail(String newEmail) {
		Optional<Member> result = memberRepository.findByEmail(newEmail);
		if (result.isEmpty()) {
			return false;
		}
		String originEmail = result.get().getEmail();

		if (originEmail.equals(newEmail)) {
			return false;
		}
		return true;
	}

	@Override
	public Member findOne(String loginId) throws MemberException {
		return memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
	}

	@Override
	public List<SubscribeInfoDto> getSubcribeList() {
			
		List<SubscribeInfoDto> subscribeList = new ArrayList<>();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(!principal.equals("anonymousUser")) {
			UserDetails loginMember = (UserDetails) principal;
			
			Member member = memberRepository.findByLoginId(loginMember.getUsername())
							.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
				
			subscribeList = member.getSubscribeList().stream().map( subscribe -> new SubscribeInfoDto(subscribe.getBoard())).toList();
		}
		
		
		return subscribeList;
	}
}
