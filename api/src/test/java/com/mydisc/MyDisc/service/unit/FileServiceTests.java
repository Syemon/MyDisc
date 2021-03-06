package com.mydisc.MyDisc.service.unit;

import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.resource.FileResource;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FileStorageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resources;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FileService.class)
public class FileServiceTests {
    @MockBean
    private FileDao fileDao;

    @Autowired
    private FileService fileService;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private Folder folder;

    @MockBean
    private File file;

    @Before
    public void setFile() {
        when(file.getId()).thenReturn(UUID.randomUUID());
        when(file.getName()).thenReturn("test.txt");
        when(file.getSize()).thenReturn(12313L);
        when(file.getStorageName()).thenReturn("1234567890ABCDEF");
        when(file.getType()).thenReturn("text/plain");
    }

    @Test
    public void testList_WithoutFolderId() {
        List<File> files = new ArrayList<>();
        files.add(file);

        when(fileDao.list()).thenReturn(new ArrayList<File>(files));

        List<File> resultFiles = fileService.list();
        Resources<FileResource> resources = fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertTrue(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertTrue(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithFolderId() {
        List<File> files = new ArrayList<>();
        files.add(file);

        when(fileDao.list(any(UUID.class))).thenReturn(new ArrayList<File>(files));

        List<File> resultFiles = fileService.list(UUID.randomUUID());
        Resources<FileResource> resources = fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertTrue(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertTrue(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithoutFolderId_WithNoFilesFound() {
        when(fileDao.list()).thenReturn(new ArrayList<File>());

        List<File> resultFiles = fileService.list();
        Resources<FileResource> resources = fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithFolderId_WithNoFilesFound() {
        when(fileDao.list(any(UUID.class))).thenReturn(new ArrayList<File>());

        List<File> resultFiles = fileService.list(UUID.randomUUID());
        Resources<FileResource> resources = fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }

    @Test
    public void testMove_FromRootFolder() {
        fileService.move(UUID.randomUUID(), mock(FilePojo.class));

        verify(fileDao, times(1)).move(any(UUID.class),any(FilePojo.class));
    }

    @Test
    public void testMove() {
        fileService.move(UUID.randomUUID(), UUID.randomUUID(), mock(FilePojo.class));

        verify(fileDao, times(1)).move(
                any(UUID.class), any(UUID.class), any(FilePojo.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(fileStorageService).deleteFile(any(String.class));
        when(fileService.findById(any(UUID.class))).thenReturn(file);
        fileService.delete(UUID.randomUUID());

        verify(fileDao, times(1)).delete(
                any(UUID.class));
    }

    @Test
    public void testExists_WhenExists_ReturnTrue() {
        when(fileDao.exists(any(UUID.class))).thenReturn(true);

        boolean result = fileService.exists(UUID.randomUUID());

        Assert.assertTrue(result);
    }

    @Test
    public void testExists_WhenNotExists_ReturnFalse() {
        when(fileDao.exists(any(UUID.class))).thenReturn(false);

        boolean result = fileService.exists(UUID.randomUUID());

        Assert.assertFalse(result);
    }

    @Test
    public void testExists_WhenFileExistsInsideFolder_ReturnTrue() {
        when(fileDao.exists(any(UUID.class), any(UUID.class))).thenReturn(true);

        boolean result = fileService.exists(UUID.randomUUID(), UUID.randomUUID());

        Assert.assertTrue(result);
    }

    @Test
    public void testExists_WhenFileNotExistsInsideFolder_ReturnFalse() {
        when(fileDao.exists(any(UUID.class), any(UUID.class))).thenReturn(false);

        boolean result = fileService.exists(UUID.randomUUID(), UUID.randomUUID());

        Assert.assertFalse(result);
    }

    @Test
    public void testIsEnoughSpace_WhenNot_ReturnFalse() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        when(fileDao.isEnoughSpace(any(MultipartFile.class))).thenReturn(false);

        boolean result = fileService.isEnoughSpace(multipartFile);

        Assert.assertFalse(result);
    }

    @Test
    public void testIsEnoughSpace_WhenEnough_ReturnTrue() {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        when(fileDao.isEnoughSpace(any(MultipartFile.class))).thenReturn(true);

        boolean result = fileService.isEnoughSpace(multipartFile);

        Assert.assertTrue(result);
    }
}
