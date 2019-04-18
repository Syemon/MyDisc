package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;

import java.util.List;
import java.util.UUID;

public interface FolderService {
    public List<Folder> findAll();

    public Folder findById(UUID id);

    public Folder save(FolderPojo folderPojo);

    public Folder rename(FolderPojo folderPojo);

    public void delete(UUID id);

    public List<Folder> findChildren();

    public List<Folder> findChildren(UUID folderId);

    void move(UUID folderId);

    void move(UUID folderId, UUID targetFolderId);

    boolean exists(UUID folderId);
}
