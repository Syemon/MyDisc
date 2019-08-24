package com.mydisc.MyDisc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.resource.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Transactional
@Service
public class FileService {
    private FileDao fileDao;
    private FileStorageService fileStorageService;

    @Autowired
    public FileService(FileDao fileDao, FileStorageService fileStorageService) {
        this.fileDao = fileDao;
        this.fileStorageService = fileStorageService;
    }

    public ResponseEntity<Resource> download(UUID fileId) {
        File file = findById(fileId);

        Resource fileResource =  fileStorageService.loadFileAsResource(
                file.getStorageName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
                .body(fileResource);
    }

    public File findById(UUID fileId) {
        return fileDao.findById(fileId);
    }

    public File findById(UUID folderId, UUID fileId) {
        return fileDao.findById(folderId, fileId);
    }

    public void delete(UUID fileId) {
        String storageName = fileDao.findById(fileId).getStorageName();
        fileDao.delete(fileId);
        fileStorageService.deleteFile(storageName);
    }

    public void deleteAllInFolder(UUID folderId) {
        List<File> files = list(folderId);
        for (File file: files) {
            delete(file.getId());
        }
    }

    public boolean exists(UUID fileId) {
        return fileDao.exists(fileId);
    }

    public boolean exists(UUID folderId, UUID fileId) {
        return fileDao.exists(folderId, fileId);
    }

    public List<File> list() {
        return fileDao.list();
    }

    public List<File> list(UUID folderId) {
        return fileDao.list(folderId);
    }

    public Resources<FileResource> getFileResources(List<File> files) {
        List<FileResource> fileResources = new ArrayList<>();

        for (File file : files) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = new HashMap<>();
            body.put("name", file.getName());
            body.put("type", file.getType());

            fileResources.add(new FileResource(file, body));
        }

        return new Resources<>(fileResources);
    }

    public File upload(MultipartFile rawFile) {
        Map<String, String> fileNames = fileStorageService.storeFile(rawFile);

        return fileDao.save(rawFile, fileNames);
    }

    public File upload(UUID folderId, MultipartFile rawFile) {
        Map<String, String> fileNames = fileStorageService.storeFile(rawFile);

        return fileDao.save(folderId, rawFile, fileNames);
    }

    public void move(UUID fileId, FilePojo filePojo) {
        fileDao.move(fileId, filePojo);
    }

    public void move(UUID folderId, UUID fileId, FilePojo filePojo) {
        fileDao.move(folderId, fileId, filePojo);
    }

    public boolean isEnoughSpace(MultipartFile file) {
        return fileDao.isEnoughSpace(file);
    }
}
