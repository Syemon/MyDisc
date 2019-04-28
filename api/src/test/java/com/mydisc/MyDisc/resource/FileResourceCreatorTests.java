package com.mydisc.MyDisc.resource;

import com.mydisc.MyDisc.entity.File;
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

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(FileResourceCreator.class)
public class FileResourceCreatorTests {
    @MockBean
    private File file;

    private List<File> files;

    private UUID fileId;

    @Before
    public void setUp() {
        this.files = new ArrayList<>();
        this.fileId = UUID.randomUUID();

        when(this.file.getId()).thenReturn(this.fileId);
        when(this.file.getName()).thenReturn("Test");
        when(this.file.getType()).thenReturn("text/plain");
        when(this.file.getSize()).thenReturn(123L);

        this.files.add(this.file);
    }

    @Test
    public void testGetResource_ReturnFileResource() {
        FileResource resource = FileResourceCreator.getResource(this.file);

        Assert.assertEquals(this.fileId.toString(), resource.getBody().get("id"));
        Assert.assertSame("Test", resource.getBody().get("name"));
        Assert.assertSame("text/plain", resource.getBody().get("type"));
        Assert.assertEquals(String.valueOf(123L), resource.getBody().get("size"));
    }

    @Test
    public void testGetResources_ReturnFolderResources() {
        Resources<FileResource> resources = FileResourceCreator.getResources(this.files);

        Assert.assertFalse(resources.getContent().isEmpty());
        for (FileResource resource: resources) {
            Assert.assertFalse(resource.getBody().containsKey("id"));
            Assert.assertFalse(resource.getBody().containsKey("size"));
            Assert.assertSame("Test", resource.getBody().get("name"));
            Assert.assertSame("text/plain", resource.getBody().get("type"));
        }
    }
}
