package com.mydisc.MyDisc.entity.unit;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FileTests {
    private File file;

    @Before
    public void setUp() {
        file  = new File();
    }

    @Test
    public void testName() {
        String name = "Lorem";
        file.setName(name);

        Assert.assertEquals(name, file.getName());
    }

    @Test
    public void testFolderRelation() {
        Folder folder = new Folder("folder");
        file.setFolder(folder);

        Assert.assertEquals(folder, file.getFolder());
    }

    @Test
    public void testType() {
        String type = "image/jpeg";
        file.setType(type);

        Assert.assertEquals(type, file.getType());
    }

    @Test
    public void testSize() {
        long size = 4212;
        file.setSize(size);

        Assert.assertEquals(size, file.getSize());
    }

    @Test
    public void testDeleted() {
        boolean isDeleted = true;
        file.setDeleted(isDeleted);

        Assert.assertEquals(isDeleted, file.isDeleted());
    }

    @Test
    public void testHasFolder_WhenNot_ReturnFalse() {
        Assert.assertFalse(file.hasFolder());
    }

    @Test
    public void testHasFolder_WhenHas_ReturnTrue() {
        Folder folder = new Folder("folder");

        file.setFolder(folder);

        Assert.assertTrue(file.hasFolder());
    }
}
