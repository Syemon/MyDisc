package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.entity.Folder;
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
    public File findById(UUID fileId) {
        Session session = entityManager.unwrap(Session.class);

        return session.get(File.class, fileId);
    }

    @Override
    public File findById(UUID folderId, UUID fileId) {
        Session session = entityManager.unwrap(Session.class);

        Query<File> query =
                session.createQuery(
                        "SELECT fi FROM File AS fi \n" +
                                "INNER JOIN fi.folder AS fo \n" +
                                "WHERE fo.id = :folderId AND fi.id = :fileId", File.class);

        query.setParameter("folderId", folderId);
        query.setParameter("fileId", fileId);

        return query.getSingleResult();
    }

    @Override
    public List<File> list() {
        Session session = entityManager.unwrap(Session.class);

        Query<File> query =
                session.createQuery(
                        "SELECT f FROM File AS f \n" +
                                "WHERE f.folder IS NULL", File.class);

        return query.getResultList();
    }

    @Override
    public List<File> list(UUID folderId) {
        Session session = entityManager.unwrap(Session.class);

        Query<File> query =
                session.createQuery(
                        "SELECT fi FROM File AS fi \n" +
                                "INNER JOIN fi.folder AS fo \n" +
                                "WHERE fo.id = :folderId", File.class);

        query.setParameter("folderId", folderId);

        return query.getResultList();
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

    @Override
    public void move(UUID fileId, FilePojo filePojo) {

    }

    @Override
    public void move(UUID folderId, UUID fileId, FilePojo filePojo) {

    }
}
