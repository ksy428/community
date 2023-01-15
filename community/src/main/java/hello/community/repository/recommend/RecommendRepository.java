package hello.community.repository.recommend;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.domain.recommend.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Long>{

	//@EntityGraph(attributePaths = {"writer","post"})
	Optional<Recommend> findByWriterAndPost(Member writer,Post post);
}
