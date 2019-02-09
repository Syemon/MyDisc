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
    public List<Folder> findAll() {
        return this.folderDao.findAll();
    }

    @Override
    @Transactional
    public Folder findById(UUID id) {
        return this.folderDao.findById(id);
    }

    @Override
    @Transactional
    public Folder save(FolderPojo folderPojo) {
        return this.folderDao.save(folderPojo);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.folderDao.deleteById(id);
    }
}
