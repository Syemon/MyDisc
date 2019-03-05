package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    public ResponseEntity download(UUID fileId);
    public File findById(UUID fileId);
    public File findById(UUID folderId, UUID fileId);
    public File upload(MultipartFile file);
    public File upload(UUID folderId, MultipartFile file);
}
