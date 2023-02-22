package hello.community.service.post;

import hello.community.domain.board.Board;
import hello.community.domain.member.Member;
import hello.community.dto.board.BoardInfoDto;
import hello.community.dto.subscribe.SubscribeInfoDto;
import hello.community.global.redis.RedisService;
import hello.community.service.message.MessageService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import hello.community.domain.media.Media;
import hello.community.domain.post.Post;
import hello.community.dto.post.PostEditDto;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostPagingDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.exception.board.BoardException;
import hello.community.exception.board.BoardExceptionType;
import hello.community.exception.file.FileException;
import hello.community.exception.member.MemberException;
import hello.community.exception.member.MemberExceptionType;
import hello.community.exception.post.PostException;
import hello.community.exception.post.PostExceptionType;
import hello.community.global.file.FileService;
import hello.community.global.util.SecurityUtil;
import hello.community.repository.board.BoardRepository;
import hello.community.repository.media.MediaRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import hello.community.repository.post.PostSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {

	public static final int POST_PAGING_SIZE = 20;
	private final PostRepository postRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;
	private final MediaRepository mediaRepository;
	private final FileService fileService;
	private final RedisService redisService;

	@Override
	@Transactional
	public Long write(PostWriteDto writeDto) throws FileException, MemberException {

		Post post = writeDto.toEntity();

		post.setWriter(memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

		post.setBoard(boardRepository.findByBoardType(writeDto.getBoardType())
				.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD)));
		
		
		List<Media> mediaList = writeDto.getMediaList();

		if (!CollectionUtils.isEmpty(mediaList)) {
			fileService.copyFiles(mediaList);
			for (Media media : mediaList) {
				media.setPost(post);
			}
		}
		postRepository.save(post);
		
		return post.getId();
	}

	@Override
	@Transactional
	public void edit(Long postId, PostEditDto editDto) throws FileException, PostException {

		Post post = findOne(postId);
		
		if(!post.getWriter().getLoginId().equals(SecurityUtil.getLoginMemberId())) {
			throw new PostException(PostExceptionType.NOT_AUTHORIZATION_POST);
		}
		
		List<Media> newMediaList = editDto.getNewMediaList();
		List<Media> deleteMediaList = editDto.getDeleteMediaList();

		//서버에서 파일삭제
		if (!CollectionUtils.isEmpty(deleteMediaList)) {
			for (Media media : deleteMediaList) {
				fileService.deleteFile(media.getStoreName());
				post.getMediaList().remove(media);
			}
		}
		//서버에 파일저장
		if (!CollectionUtils.isEmpty(newMediaList)) {
			fileService.copyFiles(newMediaList);
			for (Media media : newMediaList) {
				media.setPost(post);
			}
		}
		post.editTitle(editDto.getTitle());
		post.editContent(editDto.getContent());
	}

	@Override
	@Transactional
	public void delete(Long postId) throws FileException, PostException {

		Post post = findOne(postId);
		
		if(!post.getWriter().getLoginId().equals(SecurityUtil.getLoginMemberId())) {
			throw new PostException(PostExceptionType.NOT_AUTHORIZATION_POST);
		}

		// 게시글 속 이미지 삭제
		List<Media> deleteMediaList = mediaRepository.findAllByPostId(postId);

		if (!CollectionUtils.isEmpty(deleteMediaList)) {
			for (Media media : deleteMediaList) {
				fileService.deleteFile(media.getStoreName());
			}
		}
		postRepository.delete(post);
		post.getWriter().getPostList().remove(post);
	}

	@Override
	@Transactional
	public PostInfoDto view(Long postId) throws PostException {

		Post post = findOne(postId);

		post.addHit();

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// 로그인했으면 최근방문기록 redis에 저장
		if(!principal.equals("anonymousUser")) {
			UserDetails loginMember = (UserDetails) principal;

			Instant instant = Instant.now();
			long timeStampMillis = instant.toEpochMilli();

			BoardInfoDto boardInfoDto = new BoardInfoDto(post.getBoard());

			redisService.setRecentBoard(loginMember.getUsername(), boardInfoDto, timeStampMillis);
		}

		return new PostInfoDto(post);
	}

	@Override
	public PostEditDto getEditInfo(Long postId) throws PostException {
		return new PostEditDto(findOne(postId));
	}

	@Override
	public Post findOne(Long postId) throws PostException {
		return postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
	}

	@Override
	public PostPagingDto searchPostList(Pageable pageable, String boardType, PostSearch postSearch, int page) {
		
		if(boardType != "main") {
			Board board = boardRepository.findByBoardType(boardType)
					.orElseThrow(() -> new BoardException(BoardExceptionType.NOT_FOUND_BOARD));

			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			// 로그인했으면 최근방문기록 redis에 저장
			if(!principal.equals("anonymousUser")) {
				UserDetails loginMember = (UserDetails) principal;

				Instant instant = Instant.now();
				long timeStampMillis = instant.toEpochMilli();

				BoardInfoDto boardInfoDto = new BoardInfoDto(board);

				redisService.setRecentBoard(loginMember.getUsername(), boardInfoDto, timeStampMillis);
			}
		}
		
		//mode가 best가 아닌 다른값이오면 디폴트로 게시글 번호로 정렬
		if(!StringUtils.hasText(postSearch.getMode())) {

			pageable = PageRequest.of( page > 0 ? (page - 1) : 0 ,  POST_PAGING_SIZE, Sort.by("id").descending());
		}
		//인기글은 인기글로 올라갔을때 시간기준으로 내림차순
		else { 
			if(postSearch.getMode().equals("best")){
				pageable = PageRequest.of( page > 0 ? (page - 1) : 0 ,  POST_PAGING_SIZE, Sort.by("best").descending());
			}
			else {
				pageable = PageRequest.of( page > 0 ? (page - 1) : 0 ,  POST_PAGING_SIZE, Sort.by("id").descending());
			}
		}

		return new PostPagingDto(postRepository.search(pageable, boardType, postSearch));
	}

}
