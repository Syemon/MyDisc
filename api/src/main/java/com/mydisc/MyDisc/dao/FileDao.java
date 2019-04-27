package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FileDao {
    File findById(UUID fileId);

    File findById(UUID folderId, UUID fileId);

    void delete(UUID fileId);

    List<File> list();

    List<File> list(UUID folderId);

    File save(MultipartFile rawFile, Map<String, String> fileNames);

    File save(UUID folderId, MultipartFile rawFile, Map<String, String> fileNames);

    void move(UUID fileId, FilePojo filePojo);

    void move(UUID folderId, UUID fileId, FilePojo filePojo);

    boolean exists(UUID fileId);

    boolean exists(UUID folderId, UUID fileId);
}
