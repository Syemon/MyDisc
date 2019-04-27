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
        this.folders = new ArrayList<>();
        this.folderId = UUID.randomUUID();

        when(this.folder.getId()).thenReturn(this.folderId);
        when(this.folder.getName()).thenReturn("Lorem ipsum");
        this.folders.add(this.folder);
    }

    @Test
    public void testGetResource_WhenNoParameter_ReturnRootFolderResource() {
        FolderResource resource = FolderResourceCreator.getResource();

        Assert.assertSame("root", resource.getBody().get("name"));
        Assert.assertSame("root", resource.getBody().get("id"));
    }

    @Test
    public void testGetResource_WhenFolderParameter_ReturnFolderResource() {
        FolderResource resource = FolderResourceCreator.getResource(this.folder);

        Assert.assertSame("Lorem ipsum", resource.getBody().get("name"));
        Assert.assertEquals(this.folderId.toString(), resource.getBody().get("id"));
    }

    @Test
    public void testGetResources_WhenFoldersParameter_ReturnFolderResources() {
        Resources<FolderResource> resources = FolderResourceCreator.getResources(this.folders);

        Assert.assertFalse(resources.getContent().isEmpty());
    }
}
