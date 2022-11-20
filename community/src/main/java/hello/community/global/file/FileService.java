package hello.community.global.file;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.exception.file.FileException;

public interface FileService {

	List<Media> storeFiles(List<MultipartFile> multipartFiles) throws FileException;
	
	Media storeFile(MultipartFile multipartFile) throws FileException;
		
	void copyFiles(List<Media> mediaList) throws FileException;
	
	void deleteFile(String storeName) throws FileException;
	
	void deleteTmpFile(String fileName) throws FileException;
	
}
