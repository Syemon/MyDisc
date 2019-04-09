package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.resource.FileResource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    public ResponseEntity download(UUID fileId);
    public File findById(UUID fileId);
    public File findById(UUID folderId, UUID fileId);
    public List<File> list();
    public List<File> list(UUID folderId);
    public Resources<FileResource> getFileResources(List<File> files);
    public File upload(MultipartFile file);
    public File upload(UUID folderId, MultipartFile file);
    public void move(UUID fileId, FilePojo filePojo);
    public void move(UUID folderId, UUID fileId, FilePojo filePojo);
}
