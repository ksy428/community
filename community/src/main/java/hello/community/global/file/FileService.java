package hello.community.global.file;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.image.Image;
import hello.community.exception.file.FileException;

public interface FileService {

	List<Image> storeFiles(List<MultipartFile> multipartFiles) throws FileException;
	
	Image storeFile(MultipartFile multipartFile) throws FileException;
	
	String createStoreFileName(String originalFilename);
	
	String extractExt(String originalFilename);
	
	void deleteFiles(String fileName) throws FileException;

}
