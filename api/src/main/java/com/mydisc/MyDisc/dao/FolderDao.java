package com.mydisc.MyDisc.dao;


import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;

import java.util.List;
import java.util.UUID;

public interface FolderDao {
    boolean exists(UUID folderId);

    List<Folder> findAll();

    Folder findById(UUID id);

    Folder save(FolderPojo folderPojo);

    Folder update(FolderPojo folderPojo);

    void deleteById(UUID id);

    List<Folder> findChildren();

    List<Folder> findChildren(UUID folderId);

    void move(UUID folderId);

    void move(UUID folderId, UUID targetFolderId);
}
