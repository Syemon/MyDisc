package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FolderService {
    private FolderDao folderDao;

    @Autowired
    public FolderService(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Transactional
    public boolean exists(UUID folderId) {
        return folderDao.exists(folderId);
    }

    @Transactional
    public Folder findById(UUID id) {
        return folderDao.findById(id);
    }

    @Transactional
    public List<Folder> findChildren() {
        return folderDao.findChildren();
    }

    @Transactional
    public List<Folder> findChildren(UUID folderId) {
        return folderDao.findChildren(folderId);
    }

    @Transactional
    public Folder save(FolderPojo folderPojo) {
        return folderDao.save(folderPojo);
    }

    @Transactional
    public Folder rename(FolderPojo folderPojo) {
        return folderDao.update(folderPojo);
    }

    @Transactional
    public void delete(UUID id) {
        folderDao.deleteById(id);
    }

    @Transactional
    public void move(UUID folderId) {
        folderDao.move(folderId);
    }

    @Transactional
    public void move(UUID folderId, UUID targetFolderId) {
        folderDao.move(folderId, targetFolderId);
    }
}
