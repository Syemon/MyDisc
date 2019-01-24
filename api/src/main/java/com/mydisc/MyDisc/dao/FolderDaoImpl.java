package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public void save(Folder folder) {
        Session session = entityManager.unwrap(Session.class);

        session.saveOrUpdate(folder);
    }

    @Override
    public void deleteById(UUID id) {
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery(
                "delete from Folder where id=:folderId");

        query.setParameter("folderId", id)
                .executeUpdate();
    }
}
