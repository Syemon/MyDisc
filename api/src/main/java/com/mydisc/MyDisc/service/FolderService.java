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
        return this.folderDao.exists(folderId);
    }

    @Transactional
    public List<Folder> findAll() {
        return this.folderDao.findAll();
    }

    @Transactional
    public Folder findById(UUID id) {
        return this.folderDao.findById(id);
    }

    public List<Folder> findChildren() {
        return this.folderDao.findChildren();
    }

    public List<Folder> findChildren(UUID folderId) {
        return this.folderDao.findChildren(folderId);
    }

    @Transactional
    public Folder save(FolderPojo folderPojo) {
        return this.folderDao.save(folderPojo);
    }

    @Transactional
    public Folder rename(FolderPojo folderPojo) {
        return this.folderDao.update(folderPojo);
    }

    @Transactional
    public void delete(UUID id) {
        this.folderDao.deleteById(id);
    }

    @Transactional
    public void move(UUID folderId) {
        this.folderDao.move(folderId);
    }

    @Transactional
    public void move(UUID folderId, UUID targetFolderId) {
        this.folderDao.move(folderId, targetFolderId);
    }
}
