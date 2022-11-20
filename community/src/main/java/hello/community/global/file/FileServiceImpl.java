package hello.community.global.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.exception.file.FileException;
import hello.community.exception.file.FileExceptionType;
import hello.community.exception.member.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	@Value("${file.dir}")
	private String fileDir;
	
	@Value("${file.tmpdir}")
	private String tmpFileDir;

	public String getFullPath(String filename) {
		return fileDir + filename;
	}
	
	public String getTmpFullPath(String filename) {
		return tmpFileDir + filename;
	}

	@Override
	public List<Media> storeFiles(List<MultipartFile> multipartFileList) throws FileException {
		List<Media> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFileList) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}
	@Override
	/***
	 *  임시폴더에 파일 저장
	 */
	public Media storeFile(MultipartFile multipartFile) throws FileException {
		if (multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);

		try {
			multipartFile.transferTo(new File(getTmpFullPath(storeFileName)));
		} catch (IOException e) {
			throw new FileException(FileExceptionType.FAIL_SAVE_FILE);
		}
		return Media.builder().originName(originalFilename).storeName(storeFileName).build();
	}
	/***
	 * 임시폴더에 있던 파일 복사 후 삭제
	 */
	@Override
	public void copyFiles(List<Media> mediaList) throws FileException{
		for (Media media : mediaList) {		
			File tmpFile = new File(getTmpFullPath(media.getStoreName()));			
	
			if(tmpFile.exists()) {
				File copyFile = new File(getFullPath(media.getStoreName()));
				try {
					Files.copy(tmpFile.toPath(), copyFile.toPath(),StandardCopyOption.REPLACE_EXISTING);
					deleteTmpFile(media.getStoreName());
				} catch (IOException e) {
					throw new FileException(FileExceptionType.FAIL_SAVE_FILE);
				}
			}
		}
	}
	/***
	 * 파일삭제
	 */
	@Override
	public void deleteFile(String storeName) throws FileException {
		
		File deleteFile = new File(getFullPath(storeName));
		if(deleteFile.exists()) {
			if(!deleteFile.delete()) {
				throw new FileException(FileExceptionType.FAIL_DELETE_FILE);
			}
		}
	}
	
	/***
	 * 임시파일삭제
	 */
	@Override
	public void deleteTmpFile(String storeName) throws FileException {
		
		File deleteFile = new File(getTmpFullPath(storeName));
		if(deleteFile.exists()) {
			if(!deleteFile.delete()) {
				throw new FileException(FileExceptionType.FAIL_DELETE_FILE);
			}
		}
	}

	public String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}
	public String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}
}
