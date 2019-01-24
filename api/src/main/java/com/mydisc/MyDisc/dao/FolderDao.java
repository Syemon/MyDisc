package com.mydisc.MyDisc.dao;


import com.mydisc.MyDisc.entity.Folder;

import java.util.List;
import java.util.UUID;

public interface FolderDao {

    public List<Folder> findAll();

    public Folder findById(UUID id);

    public void save(Folder folder);

    public void deleteById(UUID id);
}
