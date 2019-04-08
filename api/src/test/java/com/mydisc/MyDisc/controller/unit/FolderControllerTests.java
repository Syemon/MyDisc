package com.mydisc.MyDisc.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
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
import static org.mockito.Mockito.*;
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

    @Test
    public void testMove_ToRoot_ValidateWithNotExistFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(null);

        this.mockMvc.perform(patch(
                "/api/folders/{folderId}/move/root",
                "dad3cfda-5124-4389-b5c2-2433a380cc49"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - dad3cfda-5124-4389-b5c2-2433a380cc49"));
    }

    @Test
    public void testMove_ToAnotherFolder_ValidateWithNotExistFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(null);

        this.mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                "dad3cfda-5124-4389-b5c2-2433a380cc49", "333e1f7f-a06e-413f-a137-35d2b0c7d4c8"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - dad3cfda-5124-4389-b5c2-2433a380cc49"));
    }

    @Test
    public void testMove_ToAnotherFolder_ValidateWithNotExistTargetFolder() throws Exception {
        UUID folderId = UUID.fromString("333e1f7f-a06e-413f-a137-35d2b0c7d4c8");
        UUID targetFolderId = UUID.fromString("dad3cfda-5124-4389-b5c2-2433a380cc49");

        when(this.folderService.findById(folderId)).thenReturn(this.folder);
        when(this.folderService.findById(targetFolderId)).thenReturn(null);

        this.mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                folderId.toString(), targetFolderId.toString()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Not found - " + targetFolderId.toString()));
    }

    @Test
    public void testMove_ToAnotherFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);

        this.mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(this.folderService, times(1)).move(
                any(UUID.class), any(UUID.class));
    }

    @Test
    public void testMove_ToRootFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);

        this.mockMvc.perform(patch(
                "/api/folders/{folderId}/move/root",
                UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(this.folderService, times(1)).move(any(UUID.class));
    }

    @Test
    public void testCreate_InAnotherFolder_ValidateWithNotExistFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(null);

        this.body = new HashMap<>();
        this.body.put("name", "Lorem ipsum");
        this.body.put("parentId", "dad3cfda-5124-4389-b5c2-2433a380cc49");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(post(
                "/api/folders")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Not found - dad3cfda-5124-4389-b5c2-2433a380cc49"));
    }

    @Test
    public void testCreate_InAnotherFolder_ValidateWithNoBody() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);

        this.mockMvc.perform(post(
                "/api/folders"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testCreate_InRoot() throws Exception {
        when(this.folder.getName()).thenReturn("folder");
        when(this.folder.getId()).thenReturn(UUID.randomUUID());
        when(this.folderService.save(any(FolderPojo.class))).thenReturn(this.folder);

        this.body = new HashMap<>();
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
    public void testCreate_InAnotherFolder() throws Exception {
        Folder folder2 = mock(Folder.class);
        when(this.folder.getName()).thenReturn("folder");
        when(this.folder.getId()).thenReturn(UUID.randomUUID());
        when(this.folder.getParent()).thenReturn(folder2);
        when(this.folderService.findById(any(UUID.class))).thenReturn(this.folder);
        when(this.folderService.save(any(FolderPojo.class))).thenReturn(this.folder);

        this.body = new HashMap<>();
        this.body.put("name", "folder");
        this.body.put("parentId", "dad3cfda-5124-4389-b5c2-2433a380cc49");
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
}
