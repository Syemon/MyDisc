package com.mydisc.MyDisc.dao.integration;

import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
public class FileDaoTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    @Test
    @Transactional
    public void testFindById() throws Exception {
        Session session = entityManager.unwrap(Session.class);
        Folder folder = this.getFolder("folder");

        File file = this.getFile();
        file.setFolder(folder);

        session.persist(file);
        session.flush();

        File resultFile = this.fileDao.findById(folder.getId(), file.getId());

        Assert.assertEquals(file, resultFile);
    }

    @Transactional
    private Folder getFolder(String name) {
        Folder folder = new Folder(name);
        entityManager.unwrap(Session.class);
        entityManager.persist(folder);
        entityManager.flush();

        return folder;
    }

    @Transactional
    private File getFile() {
        File file = new File();
        file.setName("file");
        file.setStorageName("qweqweqweqwe123");
        file.setType("text/plain");
        file.setSize(123L);
        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();

        return file;
    }
}
