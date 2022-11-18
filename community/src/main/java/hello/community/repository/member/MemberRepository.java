package hello.community.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hello.community.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	Optional<Member> findByLoginId(String loginId);
	
	Optional<Member> findByNickname(String nickname);
	
	Optional<Member> findByEmail(String email);
}
