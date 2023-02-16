package hello.community.service.member;

import java.util.List;

import hello.community.domain.member.Member;
import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.member.MemberEditDto;
import hello.community.dto.member.MemberSignUpDto;
import hello.community.dto.member.PasswordEditDto;
import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.exception.member.MemberException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface MemberService {

	void signUp(MemberSignUpDto signUpDto);

	void editInfo(MemberEditDto editInfoDto);

	void editPassword(PasswordEditDto editPWDto);

	void withdraw(String passwordCheck);
	
	MemberInfoDto getInfo(String nickName);
	
	MemberEditDto getEditInfo();
	
	boolean isExistLoginId(String loginId);
	
	boolean isExistNickname(String nickname);
	
	boolean isExistEmail(String email);

	boolean isEditNickname(String nickname);

	boolean isEditEmail(String email);

	boolean matchPassword(String password);

	List<SubscribeInfoDto> getSubscribeList();
	
}
