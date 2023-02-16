package hello.community.dto.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import hello.community.domain.member.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class MemberEditDto {

	private String loginId;

	@NotBlank(message = "닉네임은 필수 입니다")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
			message="닉네임은2-10자,한글,영문,숫자만 가능합니다")
	private String nickname;
	
	@NotBlank(message = "이메일을 입력하세요")
	@Email(message = "이메일 형식이 아닙니다")
	private String email;

	public MemberEditDto(Member member) {
		this.loginId = member.getLoginId();
		this.nickname = member.getNickname();
		this.email = member.getEmail();
	}
}
