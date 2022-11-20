package hello.community.service.post;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

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
import hello.community.global.file.FileService;
import hello.community.global.security.SecurityUtil;
import hello.community.repository.media.MediaRepository;
import hello.community.repository.member.MemberRepository;
import hello.community.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	private final MediaRepository mediaRepository;

	private final FileService fileService;

	@Override
	public Long write(PostWriteDto writeDto) throws FileException, MemberException {

		Post post = writeDto.toEntity();

		post.setWriter(memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

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
	public PostInfoDto view(Long postId) throws PostException {
		return new PostInfoDto(findOne(postId));
	}

	@Override
	public PostEditDto getEditInfo(Long postId) throws PostException {
		return new PostEditDto(findOne(postId));
	}

	@Override
	public Post findOne(Long postId) throws PostException {
		return postRepository.findById(postId).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));
	}
}
