package hello.community.repository.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hello.community.domain.image.Image;


public interface ImageRepository extends JpaRepository<Image, Long>{


	@Query("select i from Image i where i.post.id = :id")
	List<Image> findAllByPostId(@Param("id") Long id);
		
	/*return em.createQuery("select m from Member as m",Member.class)
				.getResultList();*/

}
