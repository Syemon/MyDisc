package com.mydisc.MyDisc.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath( "_links.parent").doesNotExist());
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
    public void testListWithNoCreatedFolders() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/folders");

        this.mockMvc.perform(get("/api/folders"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().json("{}"));
    }

    @Test
    public void testList() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/folders");

        this.mockMvc.perform(get("/api/folders"))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(content().json("{}"));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "Lorem", 0 },
                { 123, 1 },
                { "b40f2063-2921-4599-abdd-6469199bc548", 2 }
        });
    }

    @Parameterized.Parameter
    public int input;

    @Parameterized.Parameter(1)
    public int expected;

    @Test
    public void testGetValidation() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}", input))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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
