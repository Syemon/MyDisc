package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FileDao fileDao;

    @Override
    @Transactional
    public File upload(UUID folderId, MultipartFile rawFile) {
        String fileName = fileStorageService.storeFile(rawFile);

        return fileDao.save(folderId, rawFile, fileName);
    }
}
