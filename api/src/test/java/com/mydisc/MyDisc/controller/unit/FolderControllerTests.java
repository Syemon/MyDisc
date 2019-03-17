package com.mydisc.MyDisc.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FolderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FolderController.class)
public class FolderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileService fileService;

    @MockBean
    private FolderService folderService;

    @MockBean
    private Folder folder;

    @MockBean
    private File file;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private Map<String, String> body;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetValidation_WithStringParameter() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}", "Lorem"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter \"folderId\" is incorrect"));
    }

    @Test
    public void testGetValidation_WithIntegerParameter() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}", 123))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter \"folderId\" is incorrect"));
    }

    @Test
    public void testGetValidation_WhenIdNotExists() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - 4555c2c6-5024-40cf-b15b-24548973cc14"));
    }

    @Test
    public void testDeleteValidation_WhenIdNotExists() throws Exception {
        this.mockMvc.perform(delete("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - 4555c2c6-5024-40cf-b15b-24548973cc14"));
    }

    @Test
    public void testListChildrenValidation_WhenIdNotExists() throws Exception {
        this.mockMvc.perform(get("/api/folders/{serviceId}/children", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - 4555c2c6-5024-40cf-b15b-24548973cc14"));
    }

    @Test
    public void testRenameValidation_WhenIdNotExists() throws Exception {
        this.body = new HashMap<>();
        this.body.put("name", "New Name");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - 4555c2c6-5024-40cf-b15b-24548973cc14"));
    }

    @Test
    public void testRenameValidation_WhenHasEmptyBody() throws Exception {
        when(this.folder.getId()).thenReturn(UUID.randomUUID());
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);

        this.mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testRenameValidation_WhenHasNoNameParameter() throws Exception {
        this.body = new HashMap<>();
        this.body.put("blank", "blank");
        String jsonBody = mapper.writeValueAsString(body);

        when(this.folder.getId()).thenReturn(UUID.randomUUID());
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);

        this.mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name is required"));
    }
}
