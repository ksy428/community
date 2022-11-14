package hello.community.global.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.image.Image;
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

	public String getFullPath(String filename) {
		return fileDir + filename;
	}

	@Override
	public List<Image> storeFiles(List<MultipartFile> multipartFiles) throws FileException {
		List<Image> storeFileResult = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			if (!multipartFile.isEmpty()) {
				storeFileResult.add(storeFile(multipartFile));
			}
		}
		return storeFileResult;
	}

	@Override
	public Image storeFile(MultipartFile multipartFile) throws FileException {
		if (multipartFile.isEmpty()) {
			return null;
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String storeFileName = createStoreFileName(originalFilename);

		try {
			multipartFile.transferTo(new File(getFullPath(storeFileName)));
		} catch (IOException e) {
			throw new FileException(FileExceptionType.FAIL_SAVE_FILE);
		}
		
		return Image.builder().originImageName(originalFilename).storeImageName(storeFileName).build();
	}

	@Override
	public String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	@Override
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
