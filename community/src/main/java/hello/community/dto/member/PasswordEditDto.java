package hello.community.dto.member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordEditDto {

	@NotBlank(message = "비밀번호를 입력하세요")
	private String originPassword;
	
	@NotBlank(message = "새 비밀번호를 입력하세요")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",message = "")
	private String newPassword;
	
	@NotBlank(message = "비밀번호 확인을 입력하세요")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$",message = "")
	private String newPasswordCheck;

	@Builder
	public PasswordEditDto(String originPassword,String newPassword,String newPasswordCheck) {
		this.originPassword = originPassword;
		this.newPassword = newPassword;
		this.newPasswordCheck = newPasswordCheck;
	}
}
