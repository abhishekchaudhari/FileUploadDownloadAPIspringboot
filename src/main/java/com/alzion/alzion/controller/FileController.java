package com.alzion.alzion.controller;

import com.alzion.alzion.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("passcode") String passcode) {
        try {
            String id = fileService.uploadFile(file.getBytes(), passcode, file.getOriginalFilename());
            return ResponseEntity.ok("http://localhost:8080/api/files/download/" + id);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id,
                                               @RequestParam("passcode") String passcode) {
        try {
            byte[] data = fileService.downloadFile(id, passcode);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
