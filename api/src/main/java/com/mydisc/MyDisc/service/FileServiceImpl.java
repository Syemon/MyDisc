package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileDao fileDao;

    @Override
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
}
