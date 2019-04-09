package com.mydisc.MyDisc.dao.integration;

import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
public class FileDaoTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    @Test
    @Transactional
    public void testFindById() {
        Session session = entityManager.unwrap(Session.class);
        Folder folder = this.getFolder("folder");

        File file = this.getFile();
        file.setFolder(folder);

        session.persist(file);
        session.flush();

        File resultFile = this.fileDao.findById(folder.getId(), file.getId());

        Assert.assertEquals(file, resultFile);
    }

    @Test
    @Transactional
    public void testList_LocatedInTheRootFolder() {
        List<File> expectedFiles = new ArrayList<>();

        File file1 = this.getFile();
        File file2 = this.getFile();

        expectedFiles.add(file1);
        expectedFiles.add(file2);

        List<File> resultFiles = this.fileDao.list();

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInTheRootFolder_WhenFilesAreOnlyInADifferentFolder() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = this.getFolder("folder");

        Session session = entityManager.unwrap(Session.class);

        File file1 = this.getFile();
        File file2 = this.getFile();
        file1.setFolder(folder);
        file2.setFolder(folder);
        session.persist(file1);
        session.persist(file2);
        session.flush();

        List<File> resultFiles = this.fileDao.list();

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInSomeFolder() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = this.getFolder("folder");

        Session session = entityManager.unwrap(Session.class);

        File file1 = this.getFile();
        File file2 = this.getFile();
        file1.setFolder(folder);
        file2.setFolder(folder);

        session.persist(file1);
        session.persist(file2);
        session.flush();

        expectedFiles.add(file1);
        expectedFiles.add(file2);

        List<File> resultFiles = this.fileDao.list(folder.getId());

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInSomeFolder_WhenFilesAreOnlyInTheRoot() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = this.getFolder("folder");

        File file1 = this.getFile();
        File file2 = this.getFile();

        List<File> resultFiles = this.fileDao.list(folder.getId());

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testMove_FromRootFolder() {
        FilePojo filePojo = new FilePojo();

        Session session = entityManager.unwrap(Session.class);

        Folder targetFolder = this.getFolder("targetFolder");
        File file = this.getFile();

        filePojo.setFolderId(targetFolder.getId());

        this.fileDao.move(file.getId(), filePojo);

        session.persist(file);
        session.flush();

        session.refresh(file);

        Assert.assertEquals(targetFolder, file.getFolder());
    }

    @Test
    @Transactional
    public void testMove_FromAnotherFolder() {
        FilePojo filePojo = new FilePojo();

        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        Folder targetFolder = this.getFolder("targetFolder");
        File file = this.getFile();

        file.setFolder(folder);
        session.persist(folder);
        session.flush();

        filePojo.setFolderId(targetFolder.getId());

        this.fileDao.move(folder.getId(), file.getId(), filePojo);

        session.persist(file);
        session.flush();

        session.refresh(file);

        Assert.assertEquals(targetFolder, file.getFolder());
    }

    @Transactional
    private Folder getFolder(String name) {
        Folder folder = new Folder(name);
        entityManager.unwrap(Session.class);
        entityManager.persist(folder);
        entityManager.flush();
        entityManager.refresh(folder);

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
        entityManager.refresh(file);

        return file;
    }
}
