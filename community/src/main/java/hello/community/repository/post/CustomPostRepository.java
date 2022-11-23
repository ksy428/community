package hello.community.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import hello.community.domain.post.Post;

public interface CustomPostRepository {

	Page<Post> search(Pageable pageable, PostSearch postSearch);
}
