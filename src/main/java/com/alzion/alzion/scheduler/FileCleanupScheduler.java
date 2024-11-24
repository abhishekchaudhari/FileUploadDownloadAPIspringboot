package com.alzion.alzion.scheduler;

import com.alzion.alzion.service.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileCleanupScheduler {
    private final FileService fileService;

    public FileCleanupScheduler(FileService fileService) {
        this.fileService = fileService;
    }

    @Scheduled(cron = "0 * * * * *") // Runs every hour
    public void cleanUpOldFiles() {
        fileService.deleteExpiredFiles();
    }
}