package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
public class FolderService {
    private FolderDao folderDao;

    @Autowired
    public FolderService(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    public boolean exists(UUID folderId) {
        return folderDao.exists(folderId);
    }

    public Folder findById(UUID id) {
        return folderDao.findById(id);
    }

    public List<Folder> findChildren() {
        return folderDao.findChildren();
    }

    public List<Folder> findChildren(UUID folderId) {
        return folderDao.findChildren(folderId);
    }

    public Folder save(FolderPojo folderPojo) {
        return folderDao.save(folderPojo);
    }

    public Folder rename(FolderPojo folderPojo) {
        return folderDao.update(folderPojo);
    }

    public void delete(UUID id) {
        folderDao.deleteById(id);
    }

    public void move(UUID folderId) {
        folderDao.move(folderId);
    }

    public void move(UUID folderId, UUID targetFolderId) {
        folderDao.move(folderId, targetFolderId);
    }
}
