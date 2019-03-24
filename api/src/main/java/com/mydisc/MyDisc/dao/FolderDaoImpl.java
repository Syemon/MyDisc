package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

@Repository
public class FolderDaoImpl implements FolderDao {

    private EntityManager entityManager;

    @Autowired
    public FolderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Folder> findAll() {
        Session session = entityManager.unwrap(Session.class);

        Query<Folder> query =
                session.createQuery("from Folder", Folder.class);

        return query.getResultList();
    }

    @Override
    public Folder findById(UUID id) {
        Session session = entityManager.unwrap(Session.class);

        return session.get(Folder.class, id);
    }

    @Override
    public List<Folder> findChildren() {
        Session session = entityManager.unwrap(Session.class);

        Query<Folder> query =
                session.createQuery(
                        "SELECT f FROM Folder AS f \n" +
                        "LEFT JOIN Folder AS p \n" +
                        "ON f.parent = p \n" +
                        "WHERE p IS NULL", Folder.class);

        return query.getResultList();
    }

    @Override
    public List<Folder> findChildren(UUID folderId) {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = session.get(Folder.class, folderId);

        return folder.getChildren();
    }

    @Override
    public Folder save(FolderPojo folderPojo) {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = new Folder();
        folder.setName(folderPojo.getName());
        session.save(folder);
        session.flush();
        session.refresh(folder);

        try {
            UUID parentId = UUID.fromString(folderPojo.getParentId());
            Folder parent = session.get(Folder.class, parentId);
            folder.setParent(parent);
        } catch (Exception exc) {

        }

        session.save(folder);
        return folder;
    }

    @Override
    public Folder update(FolderPojo folderPojo) {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = session.get(Folder.class, folderPojo.getId());
        folder.setName(folderPojo.getName());
        session.save(folder);

        return folder;
    }

    @Override
    public void deleteById(UUID id) {
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery(
                "delete from Folder where id=:folderId");

        query.setParameter("folderId", id)
                .executeUpdate();
    }

    @Override
    public void move(UUID folderId) {

    }

    @Override
    public void move(UUID folderId, UUID targetFolderId) {

    }
}
