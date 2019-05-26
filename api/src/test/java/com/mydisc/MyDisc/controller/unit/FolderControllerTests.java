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

    private Map<String, String> body;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGetValidation_WithStringParameter() throws Exception {
        mockMvc.perform(get("/api/folders/{serviceId}", "Lorem"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter \"folderId\" is incorrect"));
    }

    @Test
    public void testGetValidation_WithIntegerParameter() throws Exception {
        mockMvc.perform(get("/api/folders/{serviceId}", 123))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Parameter \"folderId\" is incorrect"));
    }

    @Test
    public void testGetValidation_WhenIdNotExists() throws Exception {
        mockMvc.perform(get("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testDeleteValidation_WhenIdNotExists() throws Exception {
        mockMvc.perform(delete("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testListChildrenValidation_WhenIdNotExists() throws Exception {
        mockMvc.perform(get("/api/folders/{serviceId}/children", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testRenameValidation_WhenIdNotExists() throws Exception {
        body = new HashMap<>();
        body.put("name", "New Name");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testRenameValidation_WhenHasEmptyBody() throws Exception {
        when(folder.getId()).thenReturn(UUID.randomUUID());
        when(folderService.findById(any(UUID.class))).thenReturn(folder);

        mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testRenameValidation_WhenHasNoNameParameter() throws Exception {
        body = new HashMap<>();
        body.put("blank", "blank");
        String jsonBody = mapper.writeValueAsString(body);

        when(folder.getId()).thenReturn(UUID.randomUUID());
        when(folderService.exists(any(UUID.class))).thenReturn(true);

        mockMvc.perform(patch("/api/folders/{serviceId}", "4555c2c6-5024-40cf-b15b-24548973cc14")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Name is required"));
    }

    @Test
    public void testMove_ToRoot_ValidateWithNotExistFolder() throws Exception {
        when(folderService.findById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(patch(
                "/api/folders/{folderId}/move/root",
                "dad3cfda-5124-4389-b5c2-2433a380cc49"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testMove_ToAnotherFolder_ValidateWithNotExistFolder() throws Exception {
        when(folderService.findById(any(UUID.class))).thenReturn(null);

        mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                "dad3cfda-5124-4389-b5c2-2433a380cc49", "333e1f7f-a06e-413f-a137-35d2b0c7d4c8"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testMove_ToAnotherFolder_ValidateWithNotExistTargetFolder() throws Exception {
        UUID folderId = UUID.fromString("333e1f7f-a06e-413f-a137-35d2b0c7d4c8");
        UUID targetFolderId = UUID.fromString("dad3cfda-5124-4389-b5c2-2433a380cc49");

        when(folderService.exists(folderId)).thenReturn(true);
        when(folderService.exists(targetFolderId)).thenReturn(false);

        mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                folderId.toString(), targetFolderId.toString()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testMove_ToAnotherFolder() throws Exception {
        when(folderService.exists(any(UUID.class))).thenReturn(true);

        mockMvc.perform(patch(
                "/api/folders/{folderId}/move/{targetFolderId}",
                UUID.randomUUID().toString(), UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(folderService, times(1)).move(
                any(UUID.class), any(UUID.class));
    }

    @Test
    public void testMove_ToRootFolder() throws Exception {
        when(folderService.exists(any(UUID.class))).thenReturn(true);

        mockMvc.perform(patch(
                "/api/folders/{folderId}/move/root",
                UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(folderService, times(1)).move(any(UUID.class));
    }

    @Test
    public void testCreate_InAnotherFolder_ValidateWithNotExistFolder() throws Exception {
        when(folderService.findById(any(UUID.class))).thenReturn(null);

        body = new HashMap<>();
        body.put("name", "Lorem ipsum");
        body.put("parentId", "dad3cfda-5124-4389-b5c2-2433a380cc49");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post(
                "/api/folders")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message")
                        .value("Folder was not found"));
    }

    @Test
    public void testCreate_InAnotherFolder_ValidateWithNoBody() throws Exception {
        when(folderService.findById(any(UUID.class))).thenReturn(folder);

        mockMvc.perform(post(
                "/api/folders"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testCreate_InRoot() throws Exception {
        when(folder.getName()).thenReturn("folder");
        when(folder.getId()).thenReturn(UUID.randomUUID());
        when(folderService.save(any(FolderPojo.class))).thenReturn(folder);

        body = new HashMap<>();
        body.put("name", "folder");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post("/api/folders")
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
        when(folder.getName()).thenReturn("folder");
        when(folder.getId()).thenReturn(UUID.randomUUID());
        when(folder.getParent()).thenReturn(folder2);
        when(folderService.exists(any(UUID.class))).thenReturn(true);
        when(folderService.save(any(FolderPojo.class))).thenReturn(folder);

        body = new HashMap<>();
        body.put("name", "folder");
        body.put("parentId", "dad3cfda-5124-4389-b5c2-2433a380cc49");
        String jsonBody = mapper.writeValueAsString(body);

        mockMvc.perform(post("/api/folders")
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
