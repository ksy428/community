package hello.community.dto.member;

import java.time.LocalDateTime;

import hello.community.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {

	private String loginId;
	private String nickname;
	private String email;
	private LocalDateTime createdDate;
	
	@Builder
	public MemberInfoDto(String loginId, String nickname, String email, Long exp, LocalDateTime createDate) {
		this.loginId = loginId;
		this.nickname = nickname;
		this.email = email;
		this.createdDate = createDate;
	}
	
	public MemberInfoDto(Member member) {
		this.loginId = member.getLoginId();
		this.nickname = member.getNickname();
		this.email = member.getEmail();
		this.createdDate = member.getCreatedDate();
	}
}
