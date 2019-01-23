package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.Folder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FolderController {
    private FolderDao folderDao;

    @Autowired
    public FolderController(FolderDao folderDao) {
        this.folderDao = folderDao;
    }

    @GetMapping("/folders")
    public List<Folder> list() {
        return folderDao.findAll();
    }
}
