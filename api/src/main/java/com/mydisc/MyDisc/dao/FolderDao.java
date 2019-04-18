package com.mydisc.MyDisc.dao;


import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;

import java.util.List;
import java.util.UUID;

public interface FolderDao {

    boolean exists(UUID folderId);

    public List<Folder> findAll();

    public Folder findById(UUID id);

    public Folder save(FolderPojo folderPojo);

    public Folder update(FolderPojo folderPojo);

    public void deleteById(UUID id);

    List<Folder> findChildren();

    List<Folder> findChildren(UUID folderId);

    void move(UUID folderId);

    void move(UUID folderId, UUID targetFolderId);
}
