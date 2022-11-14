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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.image.Image;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.exception.file.FileException;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.repository.image.ImageRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
class PostServiceImplTest {

	@Autowired private MemberRepository memberRepository;
	@Autowired private ImageRepository imageRepository;
	@Autowired private PostRepository postRepository;
	
	@Autowired PasswordEncoder passwordEncoder;

	@Autowired private PostService postService;
	
	@Autowired private EntityManager em;
	
	private final String USERNAME = "test";
	private final String PASSWORD = "1234";
	
	private final String fileName1 = "test_file1.jpg"; 
    private final String filePath1 = "src/main/resources/test_file1.jpg";   
    private final String fileName2 = "test_file2.jpg";	 
    private final String filePath2 = "src/main/resources/test_file2.jpg";

	@BeforeEach
	public void init() {
		memberRepository.save(Member.builder().loginId(USERNAME).password(passwordEncoder.encode(PASSWORD))
						.nickname("test").email("test@naver.com").build());
		
		memberRepository.save(Member.builder().loginId("22").password(passwordEncoder.encode(PASSWORD))
				.nickname("22").email("22@naver.com").build());	
	}

	MultipartFile getMockMultiaprtFile(String fileName,String filePath) throws Exception{	
		return new MockMultipartFile(fileName, fileName, "image/jpg", new FileInputStream(new File(filePath)));
	}

	void setImagePost() throws Exception {
		
		MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
    	MultipartFile mock2 = getMockMultiaprtFile(fileName2, filePath2);
   	
    	List<MultipartFile> imageList = new ArrayList<>(); 
    	imageList.add(mock1);
    	imageList.add(mock2);
    	
		PostWriteDto dto = PostWriteDto.builder().title("제목!!!").content("내용ㅋㅋㅋ").updoaldFile(imageList).build();
		
		postService.write(dto);
	}
	
	void setPost(String title, String content) throws Exception {
		
		PostWriteDto dto = PostWriteDto.builder().title(title).content(content).build();
		
		postService.write(dto);
	}
	
	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글등록성공() throws Exception{
    	//given
    	MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
    	MultipartFile mock2 = getMockMultiaprtFile(fileName2, filePath2);
   	
    	List<MultipartFile> imageList = new ArrayList<>(); 
    	imageList.add(mock1);
    	imageList.add(mock2);
    	
    	PostWriteDto dto = PostWriteDto.builder().title("제목!!!").content("내용ㅋㅋㅋ").updoaldFile(imageList).build();
    	
		Member member = memberRepository.findByLoginId(USERNAME)
				.orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
    	//when
		postService.write(dto);
		
		//then
		Post post = postRepository.findByWriter(member).orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST));

		assertThat(post.getTitle().equals("제목!!!"));
	}
	
	@Test
	void 게시글수정이미지x() throws Exception{
		//given
		postRepository.save(Post.builder().title("제목수정 전").content("내용수정 전").build());		
		PostWriteDto dto = PostWriteDto.builder().title("제목수정완료").content("내용수정완료").build();
		
		Member member = memberRepository.findByLoginId(USERNAME)
				.orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		//when
		postService.edit(member.getId(), dto);
		
		//then
		assertThat(postRepository.findAll().get(0).getTitle().equals("제목수정완료"));
	}
	
	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글수정이미지o() throws Exception{
	
		//given
		setPost("제목1","내용1");
		
		MultipartFile mock1 = getMockMultiaprtFile(fileName1, filePath1);
		
		List<MultipartFile> imageList = new ArrayList<>();
		imageList.add(mock1);
				
		PostWriteDto dto = PostWriteDto.builder()
				   .title("제목수정완료")
			  	   .content("내용수정완료")
			  	   .updoaldFile(imageList)
				   .build();
		
		Member member = memberRepository.findByLoginId(USERNAME)
				.orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		
		//when
		postService.edit(member.getId(), dto);
		
		//then
		assertThat(postRepository.findAll().get(0).getTitle().equals("제목수정완료"));
		assertThat(imageRepository.findAll().size()).isEqualTo(1);
	}
	
	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글수정_찾기실패() throws Exception{
		//given	    
		setPost("제목1","내용1");
					
		PostWriteDto dto = PostWriteDto.builder()
				   .title("제목수정완료")
			  	   .content("내용수정완료")
				   .build();
		
		//when
		//then
		assertThrows(PostException.class, ()-> postService.edit(100L,dto));	
	}
		
	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	//@Rollback(false)
	void 게시글삭제() throws Exception {	
		//given
		setPost("11", "11");
		setPost("22", "22");
		setPost("33", "33");
		
		Member member = memberRepository.findByLoginId(USERNAME)
				.orElseThrow(()->new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
	
		//when
		Post post = postRepository.findByTitle("11").orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST));
		postService.delete(post.getId());
		//then
		assertThat(member.getPostList().size()).isEqualTo(2);
	}
	
	@Test
	@WithMockUser(username = "test", roles = "MEMBER")
	void 게시글조회() throws Exception {
		
		setPost("11", "11");
		setPost("22", "22");
		setPost("33", "33");
				
		Post post = postRepository.findByTitle("11").orElseThrow(()-> new PostException(PostExceptionType.NOT_FOUND_POST));

		PostInfoDto infoDto = postService.view(post.getId());
		
		assertThat(infoDto.getContent().equals("11"));
		assertThat(infoDto.getMemberInfoDto().getLoginId().equals("test"));
		assertThat(infoDto.getMemberInfoDto().getEmail().equals("test.naver.com"));
	}

}
