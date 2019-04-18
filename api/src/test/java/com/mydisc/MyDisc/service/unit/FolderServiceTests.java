package com.mydisc.MyDisc.service.unit;

import com.mydisc.MyDisc.dao.FolderDao;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.service.FolderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FolderService.class)
public class FolderServiceTests {

    @MockBean
    private FolderDao folderDao;

    @Autowired
    private FolderService folderService;

    @MockBean
    private Folder folder;

    @MockBean
    private File file;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testMove_ToRootFolder() {
        this.folderService.move(UUID.randomUUID());

        verify(this.folderDao, times(1)).move(any(UUID.class));
    }

    @Test
    public void testMove_ToAnotherFolder() {
        this.folderService.move(UUID.randomUUID(), UUID.randomUUID());

        verify(this.folderDao, times(1)).move(
                any(UUID.class), any(UUID.class));
    }

    @Test
    public void testExists_WhenExists_ReturnTrue() {
        when(this.folderDao.exists(any(UUID.class))).thenReturn(true);

        boolean result = this.folderService.exists(UUID.randomUUID());

        Assert.assertTrue(result);
    }

    @Test
    public void testExists_WhenNotExists_ReturnFalse() {
        when(this.folderDao.exists(any(UUID.class))).thenReturn(false);

        boolean result = this.folderService.exists(UUID.randomUUID());

        Assert.assertFalse(result);
    }
}
