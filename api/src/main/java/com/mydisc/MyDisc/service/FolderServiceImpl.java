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
public class FolderServiceImpl implements FolderService{

    private FolderDao folderDao;

    @Autowired
    public FolderServiceImpl(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @Override
    @Transactional
    public boolean exists(UUID folderId) {
        return this.folderDao.exists(folderId);
    }

    @Override
    @Transactional
    public List<Folder> findAll() {
        return this.folderDao.findAll();
    }

    @Override
    @Transactional
    public Folder findById(UUID id) {
        return this.folderDao.findById(id);
    }

    @Override
    public List<Folder> findChildren() {
        return this.folderDao.findChildren();
    }

    @Override
    public List<Folder> findChildren(UUID folderId) {
        return this.folderDao.findChildren(folderId);
    }

    @Override
    @Transactional
    public Folder save(FolderPojo folderPojo) {
        return this.folderDao.save(folderPojo);
    }

    @Override
    @Transactional
    public Folder rename(FolderPojo folderPojo) {
        return this.folderDao.update(folderPojo);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.folderDao.deleteById(id);
    }

    @Override
    @Transactional
    public void move(UUID folderId) {
        this.folderDao.move(folderId);
    }

    @Override
    @Transactional
    public void move(UUID folderId, UUID targetFolderId) {
        this.folderDao.move(folderId, targetFolderId);
    }
}
