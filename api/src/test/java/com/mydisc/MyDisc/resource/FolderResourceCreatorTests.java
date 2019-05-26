package com.mydisc.MyDisc.resource;

import com.mydisc.MyDisc.entity.Folder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.Resources;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FolderResourceCreator.class)
public class FolderResourceCreatorTests {
    @MockBean
    private Folder folder;

    private List<Folder> folders;

    private UUID folderId;

    @Before
    public void setUp() {
        folders = new ArrayList<>();
        folderId = UUID.randomUUID();

        when(folder.getId()).thenReturn(folderId);
        when(folder.getName()).thenReturn("Lorem ipsum");
        folders.add(folder);
    }

    @Test
    public void testGetResource_WhenNoParameter_ReturnRootFolderResource() {
        FolderResource resource = FolderResourceCreator.getResource();

        Assert.assertSame("root", resource.getBody().getName());
        Assert.assertSame("root", resource.getBody().getId());
    }

    @Test
    public void testGetResource_WhenFolderParameter_ReturnFolderResource() {
        FolderResource resource = FolderResourceCreator.getResource(folder);

        Assert.assertSame("Lorem ipsum", resource.getBody().getName());
        Assert.assertEquals(folderId.toString(), resource.getBody().getId());
    }

    @Test
    public void testGetResources_WhenFoldersParameter_ReturnFolderResources() {
        Resources<FolderResource> resources = FolderResourceCreator.getResources(folders);

        Assert.assertFalse(resources.getContent().isEmpty());
    }
}
