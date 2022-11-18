package hello.community.repository.media;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.community.domain.media.Media;


public interface MediaRepository extends JpaRepository<Media, Long>{


	@Query("select i from Media i where i.post.id = :id")
	List<Media> findAllByPostId(@Param("id") Long id);
		
	/*return em.createQuery("select m from Member as m",Member.class)
				.getResultList();*/

}
