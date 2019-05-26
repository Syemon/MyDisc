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
        Folder parent = getFolder("parent");
        Folder children1 = getFolder("children1");
        Folder children2 = getFolder("children2");
        children1.setParent(parent);
        children2.setParent(parent);
        session.persist(children1);
        session.persist(children2);
        session.flush();

        expectedFolders.add(parent);

        List<Folder> folders = folderDao.findChildren();

        Assert.assertEquals(expectedFolders, folders);
    }

    @Test
    @Transactional
    public void testFindChildren() {
        List<Folder> expectedFolders = new ArrayList<>();
        Session session = entityManager.unwrap(Session.class);
        Folder parent = getFolder("parent");
        Folder children1 = getFolder("children1");
        Folder children2 = getFolder("children2");
        children1.setParent(parent);
        children2.setParent(parent);
        session.persist(children1);
        session.persist(children2);
        session.flush();

        expectedFolders.add(children1);
        expectedFolders.add(children2);

        List<Folder> folders = folderDao.findChildren(parent.getId());

        Assert.assertEquals(expectedFolders, folders);
    }

    @Test
    @Transactional
    public void testMove_FromAnotherFolderToRoot() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = getFolder("folder");
        Folder parentFolder = getFolder("parentFolder");
        folder.setParent(parentFolder);
        session.persist(folder);
        session.persist(parentFolder);
        session.flush();

        Assert.assertEquals(folder.getParent(), parentFolder);
        Assert.assertTrue(parentFolder.getChildren().contains(folder));

        folderDao.move(folder.getId());

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

        Folder folder = getFolder("folder");
        Folder parentFolder = getFolder("parentFolder");
        Folder targetFolder = getFolder("targetFolder");

        folder.setParent(parentFolder);
        session.persist(folder);
        session.persist(parentFolder);
        session.flush();

        Assert.assertEquals(folder.getParent(), parentFolder);
        Assert.assertTrue(parentFolder.getChildren().contains(folder));

        folderDao.move(folder.getId(), targetFolder.getId());

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

        Folder folder = getFolder("folder");
        UUID id = folder.getId();

        folderDao.deleteById(folder.getId());

        Assert.assertFalse(folderDao.exists(id));
    }

    @Test
    @Transactional
    public void testDelete_WhenInsideParentFolder_FolderIsDeletedAndParentFolderExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = getFolder("folder");
        Folder parent = getFolder("parent");
        folder.setParent(parent);
        UUID id = folder.getId();

        folderDao.deleteById(folder.getId());
        session.refresh(parent);

        Assert.assertFalse(folderDao.exists(id));
        Assert.assertFalse(parent.hasChildren());
    }

    @Test
    @Transactional
    public void testDelete_WhenInsideParentFolderWithAnotherFolder_FolderIsDeletedAndParentFolderExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = getFolder("folder");
        Folder child = getFolder("folder");
        Folder anotherChild = getFolder("folder");
        child.setParent(parent);
        anotherChild.setParent(parent);

        folderDao.deleteById(child.getId());
        session.refresh(parent);

        Assert.assertFalse(folderDao.exists(child.getId()));
        Assert.assertTrue(folderDao.exists(parent.getId()));
        Assert.assertTrue(folderDao.exists(anotherChild.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedParent_DeleteChild() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = getFolder("folder");
        Folder child = getFolder("folder");
        child.setParent(parent);

        folderDao.deleteById(parent.getId());

        Assert.assertFalse(folderDao.exists(parent.getId()));
        Assert.assertFalse(folderDao.exists(child.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedFolderInRoot_AnotherFolderInRootStillExists() {
        Session session = entityManager.unwrap(Session.class);

        Folder folder = getFolder("folder");
        Folder folderInRoot = getFolder("folder");

        folderDao.deleteById(folder.getId());

        Assert.assertFalse(folderDao.exists(folder.getId()));
        Assert.assertTrue(folderDao.exists(folderInRoot.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedParent_NestedChildrenAreDeleted() {
        Session session = entityManager.unwrap(Session.class);

        Folder parent = getFolder("folder");
        Folder child = getFolder("folder");
        Folder childOfchild = getFolder("folder");

        child.setParent(parent);
        childOfchild.setParent(child);
        folderDao.deleteById(parent.getId());

        session.flush();

        Assert.assertFalse(folderDao.exists(parent.getId()));
        Assert.assertFalse(folderDao.exists(child.getId()));
        Assert.assertFalse(folderDao.exists(childOfchild.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenDeletedFolderWithFile_FileAndFolderIsDeleted() throws IOException {
        Session session = entityManager.unwrap(Session.class);

        File file = getFileWithFolderRelation();
        Folder folder = file.getFolder();

        folderDao.deleteById(folder.getId());

        session.flush();

        Assert.assertFalse(folderDao.exists(folder.getId()));
        Assert.assertFalse(fileDao.exists(file.getId()));
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        Folder folder = getFolder("Test");

        boolean result = folderDao.exists(folder.getId());

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() {
        boolean result = folderDao.exists(UUID.randomUUID());

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
        File file = getFile();
        file.setFolder(getFolder("Lorem"));

        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.refresh(file);

        return file;
    }
}
