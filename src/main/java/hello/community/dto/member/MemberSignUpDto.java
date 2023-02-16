package hello.community.dto.member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import hello.community.domain.member.Member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberSignUpDto {

	@NotBlank(message = "아이디는 필수 입니다")
	@Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z\\d]{4,12}$",
			message = "4~12자에 최소 하나의 영문자를 포함해야 합니다")
	private String loginId;
	
	@NotBlank(message = "비밀번호는 필수 입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d`~!@#$%^&*()-_=+]{8,20}$",
			message = "8~20자에 최소 하나의 영문자 및 숫자를 포함해야 합니다")
	private String password;
	
	@NotBlank(message = "비밀번호는 필수 입니다.")
	private String passwordConfirm;
	
	@NotBlank(message = "닉네임은 필수 입니다")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
			message="닉네임은2-10자,한글,영문,숫자만 가능합니다")
	private String nickname;
	
	@NotBlank(message = "이메일은 필수 입니다")
	@Email(message = "이메일 형식이 아닙니다")
	private String email;
		
	@Builder
	public MemberSignUpDto(String loginId, String password, String nickname, String email) {
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
	}
	
	public Member toEntity() {	
		return Member.builder()			
				.loginId(this.loginId)
				.password(this.password)
				.nickname(this.nickname)
				.email(this.email)
				.build();
	}
	
}
