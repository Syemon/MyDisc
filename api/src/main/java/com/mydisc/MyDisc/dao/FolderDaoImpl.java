package com.mydisc.MyDisc.dao;

import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class FolderDaoImpl implements FolderDao {

    private EntityManager entityManager;

    @Autowired
    public FolderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<Folder> findAll() {
        Session currentSession = entityManager.unwrap(Session.class);

        Query<Folder> query =
                currentSession.createQuery("from Folder", Folder.class);

        return query.getResultList();
    }
}
