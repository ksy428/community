package hello.community.service.post;

import hello.community.dto.post.PostInfoDto;
import hello.community.dto.post.PostWriteDto;
import hello.community.exception.file.FileException;
import hello.community.exception.member.MemberException;
import hello.community.exception.post.PostException;

public interface PostService {

	void write(PostWriteDto writeDto) throws FileException, MemberException;
	
	void edit(Long id, PostWriteDto writeDto) throws FileException, PostException;
	
	void delete(Long id) throws FileException, PostException;
	
	PostInfoDto view (Long id) throws PostException;
}
