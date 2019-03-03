package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class FileDaoImpl implements FileDao {

    private EntityManager entityManager;

    @Autowired
    private FolderDao folderDao;

    @Autowired
    public FileDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public File save(MultipartFile rawFile, Map<String, String> fileNames) {
        Session session = entityManager.unwrap(Session.class);

        File file = new File();
        file.setName(fileNames.get("fileName"));
        file.setStorageName(fileNames.get("storageFileName"));
        file.setType(rawFile.getContentType());
        file.setSize(rawFile.getSize());

        session.save(file);
        session.flush();

        return file;
    }

    @Override
    public File save(UUID folderId, MultipartFile rawFile, Map<String, String> fileNames) {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = folderDao.findById(folderId);
        File file = new File();
        file.setFolder(folder);
        file.setName(fileNames.get("fileName"));
        file.setStorageName(fileNames.get("storageFileName"));
        file.setType(rawFile.getContentType());
        file.setSize(rawFile.getSize());

        session.save(file);
        session.flush();

        return file;
    }
}
