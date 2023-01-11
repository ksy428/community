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

	void signUp(MemberSignUpDto signUpDto) throws MemberException;
	
	void editInfo(MemberEditDto editInfoDto) throws MemberException;
	
	void editPassword(PasswordEditDto editPWDto) throws MemberException;
	
	MemberInfoDto getInfo() throws MemberException;
	
	MemberEditDto getEditInfo() throws MemberException;
	
	boolean isExistLoginId(String loginId);
	
	boolean isExistNickname(String newNickname);
	
	boolean isExistEmail(String newEmail);
	
	Member findOne(String loginId) throws MemberException;
	
	List<SubscribeInfoDto> getSubcribeList();
	
}
