package hello.community.service.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.dto.post.PostEditDto;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.exception.file.FileException;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.security.SecurityUtil;
import hello.community.repository.media.MediaRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class PostServiceImplTest {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MediaRepository mediaRepository;
	@Autowired
	private PostRepository postRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private PostService postService;

	@Autowired
	private EntityManager em;

	private final String USERNAME = "test";
	private final String PASSWORD = "1234";

	private final String fileName1 = "test_file1.jpg";
	private final String filePath1 = "src/main/resources/test_file1.jpg";
	private final String fileName2 = "test_file2.jpg";
	private final String filePath2 = "src/main/resources/test_file2.jpg";

	@BeforeEach
	public void init() throws Exception {
		/*memberRepository.save(Member.builder().loginId(USERNAME).password(passwordEncoder.encode(PASSWORD))
				.nickname("test").email("test@naver.com").build());*/

		/*	memberRepository.save(Member.builder().loginId("22").password(passwordEncoder.encode(PASSWORD)).nickname("22")
					.email("22@naver.com").build());
		
			memberRepository.save(Member.builder().loginId("33").password(passwordEncoder.encode(PASSWORD)).nickname("33")
					.email("22@naver.com").build());
		
			memberRepository.save(Member.builder().loginId("44").password(passwordEncoder.encode(PASSWORD)).nickname("44")
					.email("22@naver.com").build());
		
			memberRepository.save(Member.builder().loginId("55").password(passwordEncoder.encode(PASSWORD)).nickname("55")
					.email("22@naver.com").build());*/
		/*
				setPost("11", "11");
				setPost("22", "22");
				setPost("33", "33");
				setPost("44", "44");
				setPost("55", "55");
				setPost("66", "66");
				setPost("77", "77");
				setPost("88", "88");
				setPost("99", "99");*/
		em.clear();
	}

	//MockMultiaprtFile 생성
	MultipartFile getMockMultiaprtFile(String fileName, String filePath) throws Exception {
		return new MockMultipartFile(fileName, fileName, null, new FileInputStream(new File(filePath)));
	}

	// 이미지 있는 Post 데이터 입력
	Long setMediaPost(String title, String content) throws Exception {

		MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
		MultipartFile mock2 = getMockMultiaprtFile(fileName2, filePath2);

		List<MultipartFile> mediaList = new ArrayList<>();
		mediaList.add(mock1);
		mediaList.add(mock2);

		PostWriteDto writeDto = PostWriteDto.builder().title(title).content(content).uploadFile(mediaList).build();

		return postService.write(writeDto);
	}

	// 이미지 없는 Post 데이터 입력
	Long setPost(String title, String content) throws Exception {

		PostWriteDto writeDto = PostWriteDto.builder().title(title).content(content).build();

		return postService.write(writeDto);
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글등록성공() throws Exception {
		// given
		MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
		MultipartFile mock2 = getMockMultiaprtFile(fileName2, filePath2);

		List<MultipartFile> mediaList = new ArrayList<>();
		mediaList.add(mock1);
		mediaList.add(mock2);

		PostWriteDto dto = PostWriteDto.builder().title("제목!!!").content("내용ㅋㅋㅋ").uploadFile(mediaList).build();

		// when
		Long postId =postService.write(dto);

		// then
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));

		assertThat(post.getTitle().equals("제목!!!"));
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글수정이미지x() throws Exception {
		// given
		Long postId = setPost("제목11", "내용11");
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		PostEditDto editDto = new PostEditDto(post);
		editDto.setTitle("제목수정완료");
		editDto.setContent("내용수정완료");

		// when
		postService.edit(postId, editDto);

		// then

		Post editPost = postRepository.findById(postId)
			.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		
		assertThat(editPost.getTitle().equals("제목수정완료"));
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글수정이미지o() throws Exception {

		// given
		Long postId = setMediaPost("제목11", "내용11");
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		
		
	
		PostEditDto editDto = new PostEditDto(post);
		MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
		List<MultipartFile> mediaList = new ArrayList<>();
		
		mediaList.add(mock1);	
		editDto.setTitle("제목수정완료");
		editDto.setContent("내용수정완료");
		editDto.setNewUpdoaldFile(mediaList);

		// when
		postService.edit(postId, editDto);

		// then
		Post editPost = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		
		assertThat(editPost.getTitle().equals("제목수정완료"));
		assertThat(mediaRepository.findAll().size()).isEqualTo(1);
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글수정_찾기실패() throws Exception {
		// given
		Long postId = setPost("제목1", "내용1");

		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		PostEditDto editDto = new PostEditDto(post);
		editDto.setTitle("제목수정완료");
		editDto.setContent("내용수정완료");

		// when
		// then
		assertThrows(PostException.class, () -> postService.edit(100L, editDto));
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	// @Rollback(false)
	void 게시글삭제() throws Exception {
		// given
		Long postId = setPost("11", "11");
		setPost("22", "22");
		setPost("33", "33");

		// when		
		postService.delete(postId);
		
		// then		
		Member member = memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		
		assertThat(member.getPostList().size()).isEqualTo(2);
	}

	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글조회() throws Exception {

		Long postId = setPost("제목1", "내용1");

		PostInfoDto infoDto = postService.view(postId);

		assertThat(infoDto.getContent().equals("11"));
		assertThat(infoDto.getMemberInfoDto().getLoginId().equals("test"));
		assertThat(infoDto.getMemberInfoDto().getEmail().equals("test.naver.com"));
	}

	@Test
	void ㅋㅋ() throws Exception {

		log.info("여기부터");
		
		postRepository.findAll();
	}

}
