package com.mydisc.MyDisc.dao;


import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import org.springframework.http.HttpEntity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FolderDao {

    public List<Folder> findAll();

    public Folder findById(UUID id);

    public Folder save(FolderPojo folderPojo);

    public void deleteById(UUID id);
}
