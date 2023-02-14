package hello.community.controller.media;

import java.net.MalformedURLException;
import java.nio.charset.MalformedInputException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import hello.community.domain.media.Media;
import hello.community.global.file.FileService;
import hello.community.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MediaController {

	private final FileService fileService;
	
	@ResponseBody
	@PostMapping("/media")
	public Media mediaFileUpload(@RequestParam(value="file") MultipartFile file){
		
		Media media = fileService.storeFile(file);

		return media;
	}

}
