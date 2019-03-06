package com.mydisc.MyDisc.service.unit;

import com.mydisc.MyDisc.dao.FileDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.resource.FileResource;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FileStorageService;
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
import static org.mockito.Mockito.when;

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

        Resources<FileResource> resources = this.fileService.list();

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

        when(this.fileDao.list()).thenReturn(new ArrayList<File>(files));

        Resources<FileResource> resources = this.fileService.list();

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

        Resources<FileResource> resources = this.fileService.list();

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }

    @Test
    public void testList_WithFolderId_WithNoFilesFound() {
        when(this.fileDao.list()).thenReturn(new ArrayList<File>());

        Resources<FileResource> resources = this.fileService.list();

        for (Object fileResource: resources) {
            assertFalse(fileResource instanceof FileResource);
        }

        for (FileResource fileResource: resources) {
            assertFalse(fileResource.hasLinks());
        }
    }
}
