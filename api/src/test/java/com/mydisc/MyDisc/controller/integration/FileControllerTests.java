package com.mydisc.MyDisc.controller.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.MyDiscApplication;
import com.mydisc.MyDisc.entity.Folder;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyDiscApplication.class)
@AutoConfigureMockMvc()
public class FileControllerTests {
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, String> body = new HashMap<>();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    @Transactional
    public void testCreate() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        Folder folder = this.getFolder("folder");
        String jsonBody = mapper.writeValueAsString(body);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/folders/{folderId}/files", folder.getId())
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("file.id").exists())
                .andExpect(jsonPath("file.name").exists())
                .andExpect(jsonPath("file.type").exists())
                .andExpect(jsonPath("file.size").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.folder").exists())
                .andExpect(jsonPath( "_links.folder.href").exists());
    }

    @Test
    @Transactional
    public void testCreate_InRootFolder() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        String jsonBody = mapper.writeValueAsString(body);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/folders/root/files")
                .file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(
                        content().contentTypeCompatibleWith("application/hal+json"))
                .andExpect(jsonPath("file.id").exists())
                .andExpect(jsonPath("file.name").exists())
                .andExpect(jsonPath("file.type").exists())
                .andExpect(jsonPath("file.size").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath( "_links.self.href").exists())
                .andExpect(jsonPath( "_links.folder").exists())
                .andExpect(jsonPath( "_links.folder.href").exists());
    }

    @Transactional
    private Folder getFolder(String name) {
        Folder folder = new Folder(name);
        this.entityManager.unwrap(Session.class);
        this.entityManager.persist(folder);
        this.entityManager.flush();

        return folder;
    }
}
