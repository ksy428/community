package hello.community.dto.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import hello.community.domain.member.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberEditDto {

	private String loginId;
	
	@NotBlank(message = "닉네임을 입력하세요")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
			message="2~10자 한글,영문,숫자를 입력하세요")
	private String nickname;
	
	@NotBlank(message = "이메일을 입력하세요")
	@Email(message = "이메일 형식이 아닙니다")
	private String email;
	
	@Builder
	public MemberEditDto(String loginId, String nickname, String email) {
		this.loginId = loginId;
		this.nickname = nickname;
		this.email = email;
	}
	
	public MemberEditDto(Member member) {
		this.loginId = member.getLoginId();
		this.nickname = member.getNickname();
		this.email = member.getEmail();
	}
}
