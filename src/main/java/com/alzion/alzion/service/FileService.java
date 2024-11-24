package com.alzion.alzion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.UUID;
import java.nio.file.*;
import java.time.*;
import java.util.stream.Stream;

@Service
public class FileService {
    @Value("${storage.path}")
    private String storagePath;

//    private final FileMetadataRepository repository;
    private final EncryptionService encryptionService;

    public FileService(EncryptionService encryptionService) {
//        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    public String uploadFile(byte[] data, String passcode, String fileName) throws Exception {
        String id = UUID.randomUUID().toString();
        String filePath = storagePath + "/" + id;
        
        byte[] encryptedData = encryptionService.encrypt(data, passcode);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(encryptedData);
        }

        // Code is added just to show DB connectivity part and is of no use
//        FileMetadata metadata = new FileMetadata();
//        metadata.setId(id);
//        metadata.setFileName(fileName);
//        metadata.setFilePath(filePath);
//        metadata.setUploadTime(LocalDateTime.now());
//        repository.save(metadata);

        return id;
    }

    public byte[] downloadFile(String id, String passcode) throws Exception {
        String filePath = storagePath + "/" + id; // Construct the file path from the storage directory and file name

        // Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            throw new Exception("File not found");
        }

        // Read the file contents
        byte[] encryptedData = Files.readAllBytes(file.toPath());
        return encryptionService.decrypt(encryptedData, passcode);
    }

    public void deleteExpiredFiles() {
        // For local Testing purpose
//        LocalDateTime threshold = LocalDateTime.now().minusMinutes(1);


        LocalDateTime threshold = LocalDateTime.now().minusHours(48);

        try (Stream<Path> files = Files.list(Paths.get(storagePath))) {
            files.forEach(filePath -> {
                try {
                    // Get file's creation time
                    BasicFileAttributes attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
                    LocalDateTime creationTime = LocalDateTime.ofInstant(attributes.creationTime().toInstant(), ZoneId.systemDefault());

                    // Check if the file is older than the threshold
                    if (creationTime.isBefore(threshold)) {
                        // Delete the file
                        Files.delete(filePath);
                        System.out.println("Deleted expired file: " + filePath.getFileName());
                    }
                } catch (Exception e) {
                    System.err.println("Failed to process file: " + filePath + ", Error: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error listing files in directory: " + storagePath + ", Error: " + e.getMessage());
        }
    }
}
