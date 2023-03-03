package hello.community.global;

import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.assertj.core.api.Assertions.setMaxElementsForPrinting;

import java.lang.reflect.Array;
import java.time.LocalDateTime;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;

import ch.qos.logback.core.boolex.EvaluationException;
import hello.community.global.file.FileService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hello.community.domain.board.Board;
import hello.community.domain.comment.Comment;
import hello.community.domain.media.Media;
import hello.community.domain.member.Member;
import hello.community.domain.post.Post;
import hello.community.dto.comment.CommentBriefInfo;
import hello.community.dto.member.MemberInfoDto;
import hello.community.dto.post.PostBriefInfo;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.exception.comment.CommentException;
import hello.community.exception.comment.CommentExceptionType;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.util.DateUtil;
import hello.community.repository.board.BoardRepository;
import hello.community.repository.comment.CommentRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
//@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000")
@Slf4j
class GlobalTest {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	EntityManager em;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	FileService fileService;

	/*@Test
	void 시간테스트() {
					
		LocalDateTime date2 = LocalDateTime.of(2023, 1, 11, 23, 50);
			
		String msg = DateUtil.calculateDate(date2);
		
		log.info("날짜: {}", msg);
	}
	
	@Test
	void 문자열추출테스트() {
		
		Post post = postRepository.findById(67L).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		
		List<Media> mediaList = post.getMediaList();
		
		String thumbnailPath = null;
		
		for(Media media : mediaList) {
			log.info("이름: {}", media.getOriginName());
			if(thumbnailPath == null) {
				thumbnailPath = media.getStoreName();
			}
			if(post.getContent().indexOf(thumbnailPath) > post.getContent().indexOf(media.getStoreName())) {
				thumbnailPath = media.getStoreName();
			}
			log.info("인덱스:{}",post.getContent().indexOf(media.getStoreName()));
		}
		
		
		log.info("썸네일:{}", thumbnailPath);
		//String msg = "<p><img src=\"/file/1cf175cd-f379-46bf-a9bd-1d4786b42832.png\" style=\"width: 920px;\">ㄷㄷㅏ<img src=\"/file/174ccc62-30fc-42d5-9604-c43515f19ccc.jpg\" style=\"width: 25%;\"></p><p><br></p><p><img src=\"/file/6d2aae75-2773-4c30-993f-1b831a0567c1.png\" style=\"width: 814px;\"></p>";
		
		
		//int index = msg.indexOf("<img src=\"zz");
		//log.info("인덱스: {}", index);
	}
		
	@Test
	void 패치조인테스트() {
		System.out.println("***********************************************************************************************");
		em.flush();
		em.clear();
		
		 //Member member =  memberRepository.findById(1L).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
		//List<Comment> commentList = commentRepository.findAll();

		*//*	Comment comment = commentRepository.findById(133L).orElseThrow(()-> new CommentException(CommentExceptionType.NOT_FOUND_COMMENT));
			comment.getParent().getId();*//*
		
		
		 //member.getPostList().size();
		 //Post post = postRepository.findById(1L).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		 //post.getWriter();
		 //post.getMediaList().size();
		//List<Post> postList = postRepository.findAll();
		//memberRepository.findAll();
		//postRepository.findAll();
			*//*for(Post post: postList) {
				System.out.println(post.getWriter());
			}*//*
		// member.getPostList().size();
		// List<Member> memberList = memberRepository.findAll();
			*//*for(Member member: memberList) {
			 System.out.println(member.getPostList().size());*//*
	
	
		// Post post = postRepository.findById(1L).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
		*//* for(Post post : postList) {
			 System.out.println(post.getWriter().getNickname());
		 }*//*
	
	}
	
	@Test
	void 멤버() {
		Member member = memberRepository.findByNickname("짱짱")
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

		List<Post> postList = member.getPostList();
		List<Comment> commentList = member.getCommentList();

		int size = 5;

		List<PostBriefInfo> plist = postList.stream().sorted(Comparator.comparing(Post::getCreatedDate).reversed()).limit(5)
				.map(PostBriefInfo::new).collect(Collectors.toList());
		
		List<CommentBriefInfo> clist = commentList.stream().sorted(Comparator.comparing(Comment::getCreatedDate).reversed()).limit(5)
				.map(CommentBriefInfo::new).collect(Collectors.toList());

		for(CommentBriefInfo i : clist) {
			System.out.println("제목:"+ i.getContent());
		}
	}*/

	@Test
	void 테스트() {
		String my_string ="aAb1B2cC34oOp";

		//String[] b = my_string.split("^[1-9][0-9]*$");
		String[] b = my_string.split("^[1-9]$");

		int[] emergency={3, 76, 24};
		char g = 'a';
		int i= 1;
		int j= 10;
		int k = 1;

	}
}
