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

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileDao fileDao;

    @Override
    @Transactional
    public ResponseEntity<Resource> download(UUID fileId) {
        File file = this.findById(fileId);

        Resource fileResource =  this.fileStorageService.loadFileAsResource(
                file.getStorageName());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
                .body(fileResource);
    }

    @Override
    @Transactional
    public File findById(UUID fileId) {
        return fileDao.findById(fileId);
    }

    @Override
    @Transactional
    public File findById(UUID folderId, UUID fileId) {
        return fileDao.findById(folderId, fileId);
    }

    @Override
    @Transactional
    public void delete(UUID fileId) {
        String storageName = fileDao.findById(fileId).getStorageName();
        fileDao.delete(fileId);
        fileStorageService.deleteFile(storageName);
    }

    @Override
    @Transactional
    public List<File> list() {
        return fileDao.list();
    }

    @Override
    @Transactional
    public List<File> list(UUID folderId) {
        return fileDao.list(folderId);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public File upload(MultipartFile rawFile) {
        Map<String, String> fileNames = fileStorageService.storeFile(rawFile);

        return fileDao.save(rawFile, fileNames);
    }

    @Override
    @Transactional
    public File upload(UUID folderId, MultipartFile rawFile) {
        Map<String, String> fileNames = fileStorageService.storeFile(rawFile);

        return fileDao.save(folderId, rawFile, fileNames);
    }

    @Override
    @Transactional
    public void move(UUID fileId, FilePojo filePojo) {
        fileDao.move(fileId, filePojo);
    }

    @Override
    @Transactional
    public void move(UUID folderId, UUID fileId, FilePojo filePojo) {
        fileDao.move(folderId, fileId, filePojo);
    }
}
