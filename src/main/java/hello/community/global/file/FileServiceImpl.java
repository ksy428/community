package hello.community.global.file;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.exception.file.FileException;
import hello.community.exception.file.FileExceptionType;
import hello.community.exception.member.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

	public static final int DELETE_SEC = 60 * 60 * 24; // 24시간
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
				File thumbnail = new File(getFullPath("t_"+ extractFileName(media.getStoreName())+ ".png"));

				/**
				 * ImageIo에서 몇 몇 gif를 읽기 실패함. 이미지자체에서 오류 같은데 나중에 해결하기
				 */
				try {
					Thumbnails.of(tmpFile).crop(Positions.CENTER).size(70, 50).toFile(thumbnail);
				} catch (IOException e) {
					log.info("썸네일만들기실패");
				}
				try {
					Files.copy(tmpFile.toPath(), copyFile.toPath(),StandardCopyOption.REPLACE_EXISTING);

					deleteTmpFile(media.getStoreName());
					/////////////////////////////////////
					/*int tWidth =  70;// 생성할 썸네일이미지의 너비
					int tHeight = 50; // 생성할 썸네일이미지의 높이
					BufferedImage oImage = ImageIO.read(copyFile);
					int index = media.getStoreName().lastIndexOf(".");
					String ext = media.getStoreName().substring(index + 1);

					BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
					Graphics2D graphic = tImage.createGraphics();
					Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
					graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
					graphic.dispose(); // 리소스를 모두 해제

					ImageIO.write(tImage, ext ,thumbnail);*/
					//////////////////////////////
				} catch (IOException e) {
					//e.printStackTrace();
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
		File thumbnailFile = new File(getFullPath("t_"+ extractFileName(storeName)+ ".png"));

		if(deleteFile.exists()) {
			if(!deleteFile.delete()) {
				throw new FileException(FileExceptionType.FAIL_DELETE_FILE);
			}
		}
		if(thumbnailFile.exists()){
			if(!thumbnailFile.delete()) {
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


	/***
	 * 임시파일스케줄러삭제 ,
	 * 스케줄러로 임시파일폴더의 파일들을 가져온 후 그 파일의
	 * 초로 환산한 수정 날짜가 DELETE_SEC보다 크면 삭제한다
	 */
	@Override
	public void deleteTmpFolder() throws FileException{
		File folder = new File(tmpFileDir);

		if (!folder.isDirectory()) return;

		for(File file : folder.listFiles()){
			try {
				long fModify = getSecondsFromModification(file);
				if (fModify > DELETE_SEC) {
					file.delete();
				}
			}
			catch (IOException e) {
				throw new FileException(FileExceptionType.FAIL_DELETE_FILE);
			}
		}
	}

	// 파일의 수정한 날짜를 현재 시간 대비 경과 시간을 초로 환산 하여 리턴
	private static long getSecondsFromModification(File file) throws IOException {

		Path attribPath = file.toPath();

		BasicFileAttributes basicAttribs = Files.readAttributes(attribPath, BasicFileAttributes.class);

		return (System.currentTimeMillis() - basicAttribs.lastModifiedTime().to(TimeUnit.MILLISECONDS)) / 1000;
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

	public String extractFileName(String fileName){
		int pos = fileName.lastIndexOf(".");
		return fileName.substring(0,pos);
	}
}
