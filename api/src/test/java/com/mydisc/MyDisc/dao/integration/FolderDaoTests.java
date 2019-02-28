package com.mydisc.MyDisc.dao.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
public class FolderDaoTests {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> body = new HashMap<>();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FolderDao folderDao;

    @Test
    @Transactional
    public void testFindChildrenOfRoot() throws Exception {
        List<Folder> expectedFolders = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Folder parent = this.getFolder("parent");
        Folder children1 = this.getFolder("children1");
        Folder children2 = this.getFolder("children2");
        children1.setParent(parent);
        children2.setParent(parent);
        session.persist(children1);
        session.persist(children2);
        session.flush();

        expectedFolders.add(parent);

        List<Folder> folders = this.folderDao.findChildren();

        Assert.assertEquals(expectedFolders, folders);
    }

    @Test
    @Transactional
    public void testFindChildren() throws Exception {
        List<Folder> expectedFolders = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Folder parent = this.getFolder("parent");
        Folder children1 = this.getFolder("children1");
        Folder children2 = this.getFolder("children2");
        children1.setParent(parent);
        children2.setParent(parent);
        session.persist(children1);
        session.persist(children2);
        session.flush();

        expectedFolders.add(children1);
        expectedFolders.add(children2);

        List<Folder> folders = this.folderDao.findChildren(parent.getId());

        Assert.assertEquals(expectedFolders, folders);
    }

    @Transactional
    private Folder getFolder(String name) {
        Folder folder = new Folder(name);
        entityManager.unwrap(Session.class);
        entityManager.persist(folder);
        entityManager.flush();

        return folder;
    }
}
