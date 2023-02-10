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

	//@CacheEvict(value ="loginMember", key ="#p1")
	void editInfo(MemberEditDto editInfoDto, String loginId);

	//@CacheEvict(value ="loginMember", key ="#p1")
	void editPassword(PasswordEditDto editPWDto, String loginId);
	
	MemberInfoDto getInfo(String nickName);
	
	MemberEditDto getEditInfo();
	
	boolean isExistLoginId(String loginId);
	
	boolean isExistNickname(String newNickname);
	
	boolean isExistEmail(String newEmail);

	List<SubscribeInfoDto> getSubcribeList();
	
}
