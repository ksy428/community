package hello.community.service.post;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

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
import hello.community.global.file.FileService;
import hello.community.global.security.SecurityUtil;
import hello.community.repository.image.ImageRepository;
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
	private final ImageRepository imageRepository;

	private final FileService fileService;

	@Override
	public void write(PostWriteDto writeDto) throws FileException, MemberException {

		Post post = writeDto.toEntity();

		post.setWriter(memberRepository.findByLoginId(SecurityUtil.getLoginMemberId())
				.orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

		// 서버에 이미지 저장
		if (!CollectionUtils.isEmpty(writeDto.getUpdoaldFile())) {
			List<Image> imageList = fileService.storeFiles(writeDto.getUpdoaldFile());
			for (Image image : imageList) {
				image.setPost(post);
			}
		}
		postRepository.save(post);
	}

	@Override
	public void edit(Long id, PostWriteDto writeDto) throws FileException, PostException {

		Post post = postRepository.findById(id).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));

		// 기존이미지 삭제
		List<Image> deleteImageList = imageRepository.findAllByPostId(id);

		if (!CollectionUtils.isEmpty(deleteImageList)) {
			for (Image image : deleteImageList) {
				fileService.deleteFiles(image.getStoreImageName());
				imageRepository.delete(image);
			}
		}
		post.initImageList();
		post.editTitle(writeDto.getTitle());
		post.editContent(writeDto.getContent());

		// 새로운 이미지 서버에 저장
		if (!CollectionUtils.isEmpty(writeDto.getUpdoaldFile())) {
			List<Image> saveImageList = fileService.storeFiles(writeDto.getUpdoaldFile());
			for (Image image : saveImageList) {
				image.setPost(post);
			}
		}
	}

	@Override
	public void delete(Long id) throws FileException, PostException{

		Post post = postRepository.findById(id).orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST));

		// 게시글 속 이미지 삭제
		List<Image> deleteImageList = imageRepository.findAllByPostId(id);

		if (!CollectionUtils.isEmpty(deleteImageList)) {
			for (Image image : deleteImageList) {
				fileService.deleteFiles(image.getStoreImageName());
				imageRepository.delete(image);
			}
		}	
		postRepository.delete(post);
		post.getWriter().getPostList().remove(post);
		log.info("삭제로직:{}",post.getWriter().getPostList().size());
	}

	@Override
	public PostInfoDto view(Long id) throws PostException{
		return new PostInfoDto(postRepository.findById(id)
				.orElseThrow(() -> new PostException(PostExceptionType.NOT_FOUND_POST)));
	}
	
}
