package hello.community.repository.post;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.community.domain.post.Post;

public interface PostRepository extends JpaRepository<Post,Long>, CustomPostRepository{

	Optional<Post> findById(Long id);
	
}
