package com.mydisc.MyDisc.service;

import com.mydisc.MyDisc.entity.Folder;

import java.util.List;
import java.util.UUID;

public interface FolderService {
    public List<Folder> findAll();

    public Folder findById(UUID id);

    public void save(Folder folder);

    public void delete(UUID id);
}
