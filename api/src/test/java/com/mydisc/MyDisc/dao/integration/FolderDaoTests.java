package com.mydisc.MyDisc.dao.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.dao.FolderDao;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Autowired
    private FileDao fileDao;

    @Test
    @Transactional
    public void testFindChildrenOfRoot() {
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
    public void testFindChildren() {
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

    @Test
    @Transactional
    public void testMove_FromAnotherFolderToRoot() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        Folder parentFolder = this.getFolder("parentFolder");
        folder.setParent(parentFolder);
        session.persist(folder);
        session.persist(parentFolder);
        session.flush();

        Assert.assertEquals(folder.getParent(), parentFolder);
        Assert.assertTrue(parentFolder.getChildren().contains(folder));

        this.folderDao.move(folder.getId());

        session.persist(folder);
        session.persist(parentFolder);
        session.flush();

        session.refresh(folder);
        session.refresh(parentFolder);

        Assert.assertNull(folder.getParent());
        Assert.assertFalse(parentFolder.getChildren().contains(folder));
    }

    @Test
    @Transactional
    public void testMove_FromAnotherFolderToAnotherFolder() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        Folder parentFolder = this.getFolder("parentFolder");
        Folder targetFolder = this.getFolder("targetFolder");

        folder.setParent(parentFolder);
        session.persist(folder);
        session.persist(parentFolder);
        session.flush();

        Assert.assertEquals(folder.getParent(), parentFolder);
        Assert.assertTrue(parentFolder.getChildren().contains(folder));

        this.folderDao.move(folder.getId(), targetFolder.getId());

        session.persist(folder);
        session.persist(parentFolder);
        session.persist(targetFolder);
        session.flush();

        session.refresh(folder);
        session.refresh(parentFolder);
        session.refresh(targetFolder);

        Assert.assertEquals(folder.getParent(), targetFolder);
        Assert.assertFalse(parentFolder.getChildren().contains(folder));
        Assert.assertTrue(targetFolder.getChildren().contains(folder));
    }

    @Test
    @Transactional
    public void testDelete_WhenInsideRoot() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        UUID id = folder.getId();

        this.folderDao.deleteById(folder.getId());

        Assert.assertFalse(this.folderDao.exists(id));
    }

    @Test
    @Transactional
    public void testDelete_WhenInsideParentFolder_FolderIsDeletedAndParentFolderExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        Folder parent = this.getFolder("parent");
        folder.setParent(parent);
        UUID id = folder.getId();

        this.folderDao.deleteById(folder.getId());
        session.refresh(parent);

        Assert.assertFalse(this.folderDao.exists(id));
        Assert.assertFalse(parent.hasChildren());
    }

    @Test
    @Transactional
    public void testDelete_WhenInsideParentFolderWithAnotherFolder_FolderIsDeletedAndParentFolderExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = this.getFolder("folder");
        Folder child = this.getFolder("folder");
        Folder anotherChild = this.getFolder("folder");
        child.setParent(parent);
        anotherChild.setParent(parent);

        this.folderDao.deleteById(child.getId());
        session.refresh(parent);

        Assert.assertFalse(this.folderDao.exists(child.getId()));
        Assert.assertTrue(this.folderDao.exists(parent.getId()));
        Assert.assertTrue(this.folderDao.exists(anotherChild.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedParent_DeleteChild() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = this.getFolder("folder");
        Folder child = this.getFolder("folder");
        child.setParent(parent);

        this.folderDao.deleteById(parent.getId());

        Assert.assertFalse(this.folderDao.exists(parent.getId()));
        Assert.assertFalse(this.folderDao.exists(child.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedFolderInRoot_AnotherFolderInRootStillExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = this.getFolder("folder");
        Folder folderInRoot = this.getFolder("folder");

        this.folderDao.deleteById(folder.getId());

        Assert.assertFalse(this.folderDao.exists(folder.getId()));
        Assert.assertTrue(this.folderDao.exists(folderInRoot.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedParent_NestedChildrenAreDeleted() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = this.getFolder("folder");
        Folder child = this.getFolder("folder");
        Folder childOfchild = this.getFolder("folder");

        child.setParent(parent);
        childOfchild.setParent(child);
        this.folderDao.deleteById(parent.getId());

        session.flush();

        Assert.assertFalse(this.folderDao.exists(parent.getId()));
        Assert.assertFalse(this.folderDao.exists(child.getId()));
        Assert.assertFalse(this.folderDao.exists(childOfchild.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedFolderWithFile_FileAndFolderIsDeleted() throws IOException {
        Session session = entityManager.unwrap(Session.class);

        File file = this.getFileWithFolderRelation();
        Folder folder = file.getFolder();

        this.folderDao.deleteById(folder.getId());

        session.flush();

        Assert.assertFalse(this.folderDao.exists(folder.getId()));
        Assert.assertFalse(this.fileDao.exists(file.getId()));
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        Folder folder = this.getFolder("Test");

        boolean result = this.folderDao.exists(folder.getId());

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() {
        boolean result = this.folderDao.exists(UUID.randomUUID());

        Assert.assertFalse(result);
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
    private File getFile() throws IOException {
        String storageName = UUID.randomUUID().toString();
        Files.createFile(Paths.get(String.format("test_uploads/%s", storageName)));
        File file = new File();

        file.setName("file");
        file.setStorageName(storageName);
        file.setType("text/plain");
        file.setSize(123L);
        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.refresh(file);

        return file;
    }

    @Transactional
    private File getFileWithFolderRelation() throws IOException {
        File file = this.getFile();
        file.setFolder(this.getFolder("Lorem"));

        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.refresh(file);

        return file;
    }
}
