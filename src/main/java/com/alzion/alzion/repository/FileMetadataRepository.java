package com.alzion.alzion.repository;


import com.alzion.alzion.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, String> {
    List<FileMetadata> findAllByUploadTimeBefore(LocalDateTime time);
}