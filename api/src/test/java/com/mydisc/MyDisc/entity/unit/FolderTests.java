package com.mydisc.MyDisc.entity.unit;

import com.mydisc.MyDisc.entity.Folder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FolderTests {

    private Folder folder;

    @Autowired
    private EntityManager entityManager;

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
}