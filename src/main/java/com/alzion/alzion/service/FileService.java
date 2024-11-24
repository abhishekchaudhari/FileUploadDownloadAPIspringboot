package com.alzion.alzion.service;

import com.alzion.alzion.model.FileMetadata;
import com.alzion.alzion.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {
    @Value("${storage.path}")
    private String storagePath;

    private final FileMetadataRepository repository;
    private final EncryptionService encryptionService;

    public FileService(FileMetadataRepository repository, EncryptionService encryptionService) {
        this.repository = repository;
        this.encryptionService = encryptionService;
    }

    public String uploadFile(byte[] data, String passcode, String fileName) throws Exception {
        String id = UUID.randomUUID().toString();
        String filePath = storagePath + "/" + id;
        
        byte[] encryptedData = encryptionService.encrypt(data, passcode);
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(encryptedData);
        }

        FileMetadata metadata = new FileMetadata();
        metadata.setId(id);
        metadata.setFileName(fileName);
        metadata.setFilePath(filePath);
        metadata.setUploadTime(LocalDateTime.now());

        repository.save(metadata);
        return id;
    }

    public byte[] downloadFile(String id, String passcode) throws Exception {
        Optional<FileMetadata> metadataOpt = repository.findById(id);
        if (metadataOpt.isEmpty()) {
            throw new Exception("File not found");
        }

        FileMetadata metadata = metadataOpt.get();
        byte[] encryptedData = Files.readAllBytes(new File(metadata.getFilePath()).toPath());
        return encryptionService.decrypt(encryptedData, passcode);
    }

    public void deleteExpiredFiles() {
        LocalDateTime threshold = LocalDateTime.now().minusHours(48);
        repository.findAllByUploadTimeBefore(threshold).forEach(metadata -> {
            File file = new File(metadata.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            repository.delete(metadata);
        });
    }
}
