package com.mydisc.MyDisc.entity.unit;

import com.mydisc.MyDisc.entity.Folder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FolderTests {
    private Folder folder;

    @Before
    public void setUp() {
        this.folder  = new Folder();
    }

    @Test
    public void testName() {
        String name = "Lorem";
        this.folder.setName(name);
        Assert.assertEquals(name, this.folder.getName());
    }

    @Test
    @Transactional
    public void testSetParent() {
        Folder parent = new Folder("parent");
        Folder children1 = new Folder("children1");
        Folder children2 = new Folder("children2");

        children1.setParent(parent);
        children2.setParent(parent);

        Assert.assertEquals(parent, children1.getParent());
        Assert.assertEquals(parent, children2.getParent());

        int numberOfChildren = parent.getChildren().size();
        Assert.assertEquals(2, numberOfChildren);
    }

    @Test
    public void testRemoveChild() {
        Folder parent = new Folder("parent");
        Folder children1 = new Folder("children1");
        Folder children2 = new Folder("children2");

        children1.setParent(parent);
        children2.setParent(parent);

        parent.removeChild(children1);
        parent.removeChild(children2);

        int numberOfChildren = parent.getChildren().size();
        Assert.assertEquals(0, numberOfChildren);
    }

    @Test
    public void testHasChildren_WhenHasNotAny_ReturnFalse() {
        Folder folder = new Folder("folder");

        Assert.assertFalse(folder.hasChildren());
    }

    @Test
    public void testHasChildren_WhenHasEmptyList_ReturnFalse() {
        Folder folder = new Folder("folder");
        Folder child = new Folder("child");

        folder.addChild(child);
        folder.removeChild(child);

        Assert.assertFalse(folder.hasChildren());
    }

    @Test
    public void testHasChildren_WhenHasEmptyList_ReturnTrue() {
        Folder folder = new Folder("folder");
        Folder child = new Folder("child");

        folder.addChild(child);

        Assert.assertTrue(folder.hasChildren());
    }

    @Test
    public void testHasParent_WhenNot_ReturnFalse() {
        Folder folder = new Folder("folder");

        Assert.assertFalse(folder.hasParent());
    }

    @Test
    public void testHasParent_WhenHas_ReturnTrue() {
        Folder folder = new Folder("folder");
        folder.setParent(this.folder);

        Assert.assertTrue(folder.hasParent());
    }
}
