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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setFile() {
        when(this.file.getId()).thenReturn(UUID.randomUUID());
        when(this.file.getName()).thenReturn("test.txt");
        when(this.file.getSize()).thenReturn(12313L);
        when(this.file.getStorageName()).thenReturn("1234567890ABCDEF");
        when(this.file.getType()).thenReturn("text/plain");
    }

    @Test
    public void testList_WithoutFolderId() {
        List<File> files = new ArrayList<>();
        files.add(this.file);

        when(this.fileDao.list()).thenReturn(new ArrayList<File>(files));

        List<File> resultFiles = this.fileService.list();
        Resources<FileResource> resources = this.fileService.getFileResources(resultFiles);

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
        files.add(this.file);

        when(this.fileDao.list(any(UUID.class))).thenReturn(new ArrayList<File>(files));

        List<File> resultFiles = this.fileService.list(UUID.randomUUID());
        Resources<FileResource> resources = this.fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertTrue(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertTrue(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithoutFolderId_WithNoFilesFound() {
        when(this.fileDao.list()).thenReturn(new ArrayList<File>());

        List<File> resultFiles = this.fileService.list();
        Resources<FileResource> resources = this.fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithFolderId_WithNoFilesFound() {
        when(this.fileDao.list(any(UUID.class))).thenReturn(new ArrayList<File>());

        List<File> resultFiles = this.fileService.list(UUID.randomUUID());
        Resources<FileResource> resources = this.fileService.getFileResources(resultFiles);

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }

    @Test
    public void testMove_FromRootFolder() {
        this.fileService.move(UUID.randomUUID(), mock(FilePojo.class));

        verify(this.fileDao, times(1)).move(any(UUID.class),any(FilePojo.class));
    }

    @Test
    public void testMove() {
        this.fileService.move(UUID.randomUUID(), UUID.randomUUID(), mock(FilePojo.class));

        verify(this.fileDao, times(1)).move(
                any(UUID.class), any(UUID.class), any(FilePojo.class));
    }

    @Test
    public void testDelete() {
        doNothing().when(this.fileStorageService).deleteFile(any(String.class));
        when(this.fileService.findById(any(UUID.class))).thenReturn(this.file);
        this.fileService.delete(UUID.randomUUID());

        verify(this.fileDao, times(1)).delete(
                any(UUID.class));
    }

    @Test
    public void testExists_WhenExists_ReturnTrue() {
        when(this.fileDao.exists(any(UUID.class))).thenReturn(true);

        boolean result = this.fileService.exists(UUID.randomUUID());

        Assert.assertTrue(result);
    }

    @Test
    public void testExists_WhenNotExists_ReturnFalse() {
        when(this.fileDao.exists(any(UUID.class))).thenReturn(false);

        boolean result = this.fileService.exists(UUID.randomUUID());

        Assert.assertFalse(result);
    }

    @Test
    public void testExists_WhenFileExistsInsideFolder_ReturnTrue() {
        when(this.fileDao.exists(any(UUID.class), any(UUID.class))).thenReturn(true);

        boolean result = this.fileService.exists(UUID.randomUUID(), UUID.randomUUID());

        Assert.assertTrue(result);
    }

    @Test
    public void testExists_WhenFileNotExistsInsideFolder_ReturnFalse() {
        when(this.fileDao.exists(any(UUID.class), any(UUID.class))).thenReturn(false);

        boolean result = this.fileService.exists(UUID.randomUUID(), UUID.randomUUID());

        Assert.assertFalse(result);
    }
}
