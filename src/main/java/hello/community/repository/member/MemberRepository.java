package hello.community.repository.member;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

	/*@Cacheable(value ="loginMember", key ="#p0")
	@EntityGraph(attributePaths = {"subscribeList"})*/
	Optional<Member> findByLoginId(String loginId);
	
	Optional<Member> findByNickname(String nickname);
	
	Optional<Member> findByEmail(String email);
}
