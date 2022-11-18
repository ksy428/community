package hello.community.global.file;

import java.io.File;
import java.io.IOException;
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
	public List<Media> storeFiles(List<MultipartFile> multipartFiles) throws FileException {
		List<Media> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}

	@Override
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
		
		return Media.builder().originMediaName(originalFilename).storeMediaName(storeFileName).build();
	}

	/*// tmp -> db에 저장 + 파일옮기기
	public Media storeFileCopy(List<String> tmpStoreFileName) {
		
		return Media.builder().originMediaName(originalFilename).storeMediaName(storeFileName).build();
	}
	*/
	public String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	public String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}

	@Override
	public void deleteFiles(String filePath) throws FileException {
		
		File deleteFile = new File(getFullPath(filePath));
		
		if(deleteFile.exists()) {
			
			if(!deleteFile.delete()) {
				throw new FileException(FileExceptionType.FAIL_DELETE_FILE);
			}
		}
	}
}
