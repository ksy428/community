package hello.community.service.recommend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import hello.community.domain.recommend.Recommend;
import hello.community.exception.post.PostException;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.repository.recommend.RecommendRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class RecommendServiceImplTest {
	
	
	@Autowired
	private RecommendService recommendService;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private MockMvc mockMvc;
	

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 추천() {
			
		//given		
		//when
		Long recommendId = recommendService.write(1L);
		
		//then		
		Optional<Recommend> result = recommendRepository.findById(recommendId);
		
		assertThat(recommendId).isEqualTo(result.get().getId());
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	//@Rollback(false)
	void 중복추천() {
			
		//given		
		//when
		Long recommendId = recommendService.write(1L);
		
		//then
		assertThrows(PostException.class, ()-> recommendService.write(1L));
	}
}
