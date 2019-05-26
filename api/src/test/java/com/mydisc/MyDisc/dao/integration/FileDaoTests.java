package com.mydisc.MyDisc.dao.integration;

import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        Folder folder = getFolder("folder");

        File file = getFile();
        file.setFolder(folder);

        session.persist(file);
        session.flush();

        File resultFile = fileDao.findById(folder.getId(), file.getId());

        Assert.assertEquals(file, resultFile);
    }

    @Test
    @Transactional
    public void testList_LocatedInTheRootFolder() {
        List<File> expectedFiles = new ArrayList<>();

        File file1 = getFile();
        File file2 = getFile();

        expectedFiles.add(file1);
        expectedFiles.add(file2);

        List<File> resultFiles = fileDao.list();

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInTheRootFolder_WhenFilesAreOnlyInADifferentFolder() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = getFolder("folder");

        Session session = entityManager.unwrap(Session.class);

        File file1 = getFile();
        File file2 = getFile();
        file1.setFolder(folder);
        file2.setFolder(folder);
        session.persist(file1);
        session.persist(file2);
        session.flush();

        List<File> resultFiles = fileDao.list();

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInSomeFolder() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = getFolder("folder");

        Session session = entityManager.unwrap(Session.class);

        File file1 = getFile();
        File file2 = getFile();
        file1.setFolder(folder);
        file2.setFolder(folder);

        session.persist(file1);
        session.persist(file2);
        session.flush();

        expectedFiles.add(file1);
        expectedFiles.add(file2);

        List<File> resultFiles = fileDao.list(folder.getId());

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testList_LocatedInSomeFolder_WhenFilesAreOnlyInTheRoot() {
        List<File> expectedFiles = new ArrayList<>();
        Folder folder = getFolder("folder");

        File file1 = getFile();
        File file2 = getFile();

        List<File> resultFiles = fileDao.list(folder.getId());

        Assert.assertEquals(expectedFiles, resultFiles);
    }

    @Test
    @Transactional
    public void testMove_FromRootFolder() {
        FilePojo filePojo = new FilePojo();

        Session session = entityManager.unwrap(Session.class);

        Folder targetFolder = getFolder("targetFolder");
        File file = getFile();

        filePojo.setFolderId(targetFolder.getId());

        fileDao.move(file.getId(), filePojo);

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

        Folder folder = getFolder("folder");
        Folder targetFolder = getFolder("targetFolder");
        File file = getFile();

        file.setFolder(folder);
        session.persist(folder);
        session.flush();

        filePojo.setFolderId(targetFolder.getId());

        fileDao.move(folder.getId(), file.getId(), filePojo);

        session.persist(file);
        session.flush();

        session.refresh(file);

        Assert.assertEquals(targetFolder, file.getFolder());
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        File file = getFile();

        boolean result = fileDao.exists(file.getId());

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() {
        boolean result = fileDao.exists(UUID.randomUUID());

        Assert.assertFalse(result);
    }

    @Test
    @Transactional
    public void testExists_WhenExistsInsideFolder_ReturnTrue() {
        File file = getFileWithFolderRelation();

        boolean result = fileDao.exists(
                file.getFolder().getId(),
                file.getId()
        );

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testExists_WhenNotExistsInsideFolder_ReturnFalse() {
        File file = getFile();

        boolean result = fileDao.exists(UUID.randomUUID(), file.getId());

        Assert.assertFalse(result);
    }

    @Test
    @Transactional
    public void testIsEnoughSpace_WhenIs_ReturnTrue() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", new byte[1024 * 1024 * 30]);

        getFileWithGivenSize(5000000L);
        getFileWithGivenSize(9000000L);

        boolean result = fileDao.isEnoughSpace(multipartFile);

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testIsEnoughSpace_WhenIsAndEmptyDisc_ReturnTrue() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", new byte[1024 * 1024 * 30]);

        boolean result = fileDao.isEnoughSpace(multipartFile);

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void testIsEnoughSpace_WhenNotAndEmptyDisc_ReturnFalse() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", new byte[1024 * 1024 * 51]);

        boolean result = fileDao.isEnoughSpace(multipartFile);

        Assert.assertFalse(result);
    }

    @Test
    @Transactional
    public void testIsEnoughSpace_WhenNot_ReturnFalse() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", new byte[1024 * 1024 * 51]);

        getFileWithGivenSize(24000000L);
        getFileWithGivenSize(24000000L);

        boolean result = fileDao.isEnoughSpace(multipartFile);

        Assert.assertFalse(result);
    }


    @Test
    @Transactional
    public void testDelete_WhenInRoot() throws IOException {
        Session session = entityManager.unwrap(Session.class);

        File file = getFileWithStorage();

        fileDao.delete(file.getId());
        session.flush();

        Assert.assertFalse(fileDao.exists(file.getId()));
    }

    @Test
    @Transactional
    public void testDelete_WhenInFolder() throws IOException {
        Session session = entityManager.unwrap(Session.class);

        File file = getFileWithFolderRelation();

        fileDao.delete(file.getId());
        session.flush();

        Assert.assertFalse(fileDao.exists(file.getId()));
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

    @Transactional
    private File getFileWithStorage() throws IOException {
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
    private File getFileWithGivenSize(Long size) {
        File file = new File();
        file.setName("file");
        file.setStorageName("qweqweqweqwe123");
        file.setType("text/plain");
        file.setSize(size);
        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.refresh(file);

        return file;
    }

    @Transactional
    private File getFileWithFolderRelation() {
        File file = getFile();
        file.setFolder(getFolder("Lorem"));

        entityManager.unwrap(Session.class);
        entityManager.persist(file);
        entityManager.flush();
        entityManager.refresh(file);

        return file;
    }
}
