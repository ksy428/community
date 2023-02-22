package hello.community.global.scheduler;

import hello.community.global.file.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileScheduler {
    private final FileService fileService;

    //12시간마다
    @Scheduled(cron = "0 0 0/12 * * *")
    public void deleteTmpFolder(){
        fileService.deleteTmpFolder();
    }
}
