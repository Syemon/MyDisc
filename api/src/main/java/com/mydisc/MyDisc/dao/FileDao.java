package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FileDao {

    public File findById(UUID fileId);
    public File findById(UUID folderId, UUID fileId);
    public List<File> list();
    public List<File> list(UUID folderId);
    public File save(MultipartFile rawFile, Map<String, String> fileNames);
    public File save(UUID folderId, MultipartFile rawFile, Map<String, String> fileNames);
    public void move(UUID fileId, FilePojo filePojo);
    public void move(UUID folderId, UUID fileId, FilePojo filePojo);
}
