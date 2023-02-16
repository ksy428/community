package hello.community.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordEditDto {
	private String originPassword;

	@NotBlank(message = "비밀번호는 필수 입니다.")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d`~!@#$%^&*()-_=+]{8,20}$",
			message = "8~20자에 최소 하나의 영문자 및 숫자를 포함해야 합니다")
	private String newPassword;

	private String newPasswordConfirm;

	@Builder
	public PasswordEditDto(String originPassword,String newPassword,String newPasswordConfirm) {
		this.originPassword = originPassword;
		this.newPassword = newPassword;
		this.newPasswordConfirm = newPasswordConfirm;
	}
}
