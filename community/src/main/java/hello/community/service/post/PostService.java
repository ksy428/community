package hello.community.service.post;

import hello.community.domain.post.Post;
import hello.community.dto.post.PostEditDto;
import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.exception.file.FileException;
import hello.community.exception.member.MemberException;
import hello.community.exception.post.PostException;

public interface PostService {

	//Long write(PostWriteDto writeDto) throws FileException, MemberException;
	
	Long write(PostWriteDto writeDto) throws FileException, MemberException;
	
	void edit(Long postId, PostEditDto editDto) throws FileException, PostException;
	
	void delete(Long postId) throws FileException, PostException;
	
	PostInfoDto view (Long postId) throws PostException;
	
	PostEditDto getEditInfo(Long postId) throws PostException;
	
	Post findOne(Long postId) throws PostException;
}
