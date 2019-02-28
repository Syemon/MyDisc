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
import java.util.UUID;

@Repository
public class FileDaoImpl implements FileDao {

    private EntityManager entityManager;

    @Autowired
    public FileDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public File save(UUID folderId, MultipartFile rawFile, String fileName) {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = session.get(Folder.class, folderId);
        File file = new File();
        file.setFolder(folder);
        file.setName(fileName);
        file.setType(rawFile.getContentType());

        file.setSize(rawFile.getSize());

        session.save(file);
        session.flush();
        session.refresh(folder);

        session.save(folder);
        return file;
    }
}
