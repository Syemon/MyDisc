package com.mydisc.MyDisc.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
@AutoConfigureMockMvc()
public class FolderControllerTests {

    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> body = new HashMap<>();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreate() throws Exception {
        this.body.put("name", "folder");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(post("/api/folders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("folder.name").value("folder"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.parent").exists())
                .andExpect(jsonPath( "_links.files").exists());
    }

    @Test
    @Transactional
    public void testDelete() throws Exception {
        Folder folder = getFolder("folder");

        this.mockMvc.perform(delete("/api/folders/{folderId}", folder.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void testDeleteParent() throws Exception {
        Folder parent = getFolder("folder");
        Folder children = getFolder("folder");

        this.mockMvc.perform(delete("/api/folders/{folderId}", parent.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void testDeleteChildren() throws Exception {
        Folder parent = getFolder("folder");
        Folder children = getFolder("folder");

        this.mockMvc.perform(delete("/api/folders/{folderId}", children.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void testRename() throws Exception {
        Folder folder = getFolder("folder");
        UUID folderId = folder.getId();

        String newName = "new Folder";
        this.body.put("name", newName);
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(patch("/api/folders/{folderId}", folderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("folder.name").value(newName))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.parent").exists())
                .andExpect(jsonPath( "_links.files").exists());

        Session session = entityManager.unwrap(Session.class);
        Folder renamedFolder = session.get(Folder.class, folderId);

        Assert.assertEquals(newName, renamedFolder.getName());
    }

    @Test
    @Transactional
    public void testList() throws Exception {
        String parentName = "parent";
        String childrenOneName = "children1";
        String childrenTwoName = "children2";

        Folder parent = this.getFolder(parentName);
        Folder children1 = this.getFolder(childrenOneName);
        children1.setParent(parent);
        Folder children2 = this.getFolder(childrenTwoName);
        children2.setParent(parent);
        entityManager.unwrap(Session.class);
        entityManager.persist(children1);
        entityManager.persist(children2);
        entityManager.flush();


        this.mockMvc.perform(get("/api/folders/{folderId}/children", parent.getId()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().json("{}"));
    }

    @Test
    @Transactional
    public void testRootList() throws Exception {
        String parentName = "parent";
        String childrenOneName = "children1";
        String childrenTwoName = "children2";

        Folder parent = this.getFolder(parentName);
        Folder children1 = this.getFolder(childrenOneName);
        children1.setParent(parent);
        Folder children2 = this.getFolder(childrenTwoName);
        children2.setParent(parent);
        entityManager.unwrap(Session.class);
        entityManager.persist(children1);
        entityManager.persist(children2);
        entityManager.flush();


        this.mockMvc.perform(get("/api/folders/root/children"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().json("{}"));
    }

    @Test
    @Transactional
    public void testGet() throws Exception {
        String name = "folder";

        Folder folder = this.getFolder(name);
        Folder children = this.getFolder("children");
        children.setParent(folder);
        entityManager.unwrap(Session.class);
        entityManager.persist(children);
        entityManager.flush();


        this.mockMvc.perform(get("/api/folders/{folderId}", folder.getId()))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("folder.id").value(folder.getId().toString()))
                .andExpect(jsonPath("folder.name").value(name))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.parent").exists())
                .andExpect(jsonPath( "_links.files").exists());
    }

    @Test
    public void testGetRoot() throws Exception {
        String name = "root";

        this.mockMvc.perform(get("/api/folders/{folderId}", "root"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("folder.id").value(name))
                .andExpect(jsonPath("folder.name").value(name))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.parent").doesNotExist())
                .andExpect(jsonPath( "_links.files").exists());
    }

    @Transactional
    private Folder getFolder(String name) {
        Folder folder = new Folder(name);
        entityManager.unwrap(Session.class);
        entityManager.persist(folder);
        entityManager.flush();

        return folder;
    }
}
