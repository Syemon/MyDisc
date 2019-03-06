package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.resource.FileResource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    public ResponseEntity download(UUID fileId);
    public File findById(UUID fileId);
    public File findById(UUID folderId, UUID fileId);
    public Resources<FileResource> list();
    public Resources<FileResource> list(UUID folderId);
    public File upload(MultipartFile file);
    public File upload(UUID folderId, MultipartFile file);
}
