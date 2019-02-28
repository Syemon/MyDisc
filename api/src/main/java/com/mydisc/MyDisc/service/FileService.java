package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface FileService {
    public File upload(UUID folderId, MultipartFile file);
}
