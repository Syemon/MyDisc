package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.Folder;
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
        return this.findById(id);
    }

    @Override
    @Transactional
    public void save(Folder folder) {
        this.folderDao.save(folder);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        this.delete(id);
    }
}
