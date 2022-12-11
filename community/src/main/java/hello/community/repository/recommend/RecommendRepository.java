package hello.community.repository.recommend;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.domain.recommend.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Long>{

	Optional<Recommend> findByMemberAndPost(Member member,Post post);
}
