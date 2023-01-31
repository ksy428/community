package hello.community.service.member;

import java.util.List;

import hello.community.domain.member.Member;
import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.member.MemberEditDto;
import hello.community.dto.member.MemberSignUpDto;
import hello.community.dto.member.PasswordEditDto;
import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.exception.member.MemberException;

public interface MemberService {

	void signUp(MemberSignUpDto signUpDto);
	
	void editInfo(MemberEditDto editInfoDto);
	
	void editPassword(PasswordEditDto editPWDto);
	
	MemberInfoDto getInfo(String nickName);
	
	MemberEditDto getEditInfo();
	
	boolean isExistLoginId(String loginId);
	
	boolean isExistNickname(String newNickname);
	
	boolean isExistEmail(String newEmail);
	
	Member findOne(String loginId);
	
	List<SubscribeInfoDto> getSubcribeList();
	
}
