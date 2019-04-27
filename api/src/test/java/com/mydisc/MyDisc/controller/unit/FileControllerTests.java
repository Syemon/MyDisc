package com.mydisc.MyDisc.controller.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.controller.FileController;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FolderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileControllerTests {
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

    @Before
    public void setFile() {
        when(this.file.getId()).thenReturn(UUID.randomUUID());
        when(this.file.getName()).thenReturn("test.txt");
        when(this.file.getSize()).thenReturn(12313L);
        when(this.file.getStorageName()).thenReturn("1234567890ABCDEF");
        when(this.file.getType()).thenReturn("text/plain");
    }

    @Before
    public void setFolder() {
        when(this.folder.getId()).thenReturn(UUID.randomUUID());
    }

    @Test
    public void testCreate_WithRootFolder() throws Exception {
        when(this.fileService.upload(any(MultipartFile.class))).thenReturn(this.file);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

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

    @Test
    public void testCreate_WithSomeFolder() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.upload(any(UUID.class), any(MultipartFile.class))).thenReturn(this.file);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.multipart(
                "/api/folders/{folderId}/files", this.folder.getId())
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
    public void testGet_WithRootFolder() throws Exception {
        when(this.fileService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.findById(any(UUID.class))).thenReturn(this.file);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/folders/root/files/{fileId}",
                this.file.getId())
                .contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(jsonPath( "_links.folder.href").exists())
                .andExpect(jsonPath( "_links.download.href").exists());
    }

    @Test
    public void testGet_WithSomeFolder() throws Exception {
        when(this.fileService.exists(any(UUID.class), any(UUID.class))).thenReturn(true);
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.findById(any(UUID.class), any(UUID.class))).thenReturn(this.file);

        this.mockMvc.perform(MockMvcRequestBuilders.get(
                "/api/folders/{folderId}/files/{fileId}", this.folder.getId(), this.file.getId())
                .contentType(MediaType.APPLICATION_JSON))
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
                .andExpect(jsonPath( "_links.folder.href").exists())
                .andExpect(jsonPath( "_links.download.href").exists());
    }

    @Test
    public void testMove_ValidateWithNotExistFolder() throws Exception {
        when(this.folderService.findById(any(UUID.class))).thenReturn(null);
        this.body = new HashMap<>();
        this.body.put("folderId", "2d2fe797-49ba-4bcd-8f42-1ccf0168771d");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/{folderId}/files/{fileId}/move",
                "dad3cfda-5124-4389-b5c2-2433a380cc49", this.file.getId())
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testMove_ValidateWithNotExistFile() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.findById(any(UUID.class), any(UUID.class))).thenReturn(null);

        this.body = new HashMap<>();
        this.body.put("folderId", "2d2fe797-49ba-4bcd-8f42-1ccf0168771d");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/{folderId}/files/{fileId}/move",
                this.folder.getId(), "dad3cfda-5124-4389-b5c2-2433a380cc49")
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("File was not found"));
    }

    @Test
    public void testMove_ValidateWithoutBody() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/{folderId}/files/{fileId}/move",
                this.folder.getId(), this.file.getId()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testMove_ValidateWithNullFolderIdInBody() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/{folderId}/files/{fileId}/move",
                this.folder.getId(), "dad3cfda-5124-4389-b5c2-2433a380cc49"))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Data is required"));
    }

    @Test
    public void testMove() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.exists(any(UUID.class), any(UUID.class))).thenReturn(true);

        this.body = new HashMap<>();
        this.body.put("folderId", "2d2fe797-49ba-4bcd-8f42-1ccf0168771d");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/{folderId}/files/{fileId}/move",
                UUID.randomUUID().toString(), UUID.randomUUID().toString())
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(this.fileService, times(1)).move(
                any(UUID.class), any(UUID.class), any(FilePojo.class));
    }

    @Test
    public void testMove_FromRootFolder() throws Exception {
        when(this.fileService.exists(any(UUID.class))).thenReturn(true);

        this.body = new HashMap<>();
        this.body.put("folderId", "2d2fe797-49ba-4bcd-8f42-1ccf0168771d");
        String jsonBody = mapper.writeValueAsString(body);

        this.mockMvc.perform(MockMvcRequestBuilders.patch(
                "/api/folders/root/files/{fileId}/move",
                UUID.randomUUID().toString())
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(this.fileService, times(1)).move(
                any(UUID.class), any(FilePojo.class));
    }

    @Test
    public void testDelete_WhenInRootFileDoesNotExist_ReturnError() throws Exception {
        when(this.fileService.exists(any(UUID.class))).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/folders/root/files/{fileId}",
                UUID.randomUUID().toString()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("File was not found"));
    }

    @Test
    public void testDelete_WhenInFolderFileDoesNotExist_ReturnError() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.exists(any(UUID.class))).thenReturn(false);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/folders/{folderId}/files/{fileId}",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("File was not found"));
    }

    @Test
    public void testDelete_WhenFolderDoesNotExist_ReturnError() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(false);
        when(this.fileService.exists(any(UUID.class))).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/folders/{folderId}/files/{fileId}",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()))
                .andExpect(
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message").value("Folder was not found"));
    }

    @Test
    public void testDelete_WhenFileInRoot_ReturnSuccess() throws Exception {
        when(this.fileService.exists(any(UUID.class))).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/folders/root/files/{fileId}",
                UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(this.fileService, times(1)).delete(
                any(UUID.class));
    }

    @Test
    public void testDelete_WhenFileInFolder_ReturnSuccess() throws Exception {
        when(this.folderService.exists(any(UUID.class))).thenReturn(true);
        when(this.fileService.exists(any(UUID.class), any(UUID.class))).thenReturn(true);

        this.mockMvc.perform(MockMvcRequestBuilders.delete(
                "/api/folders/{folderId}/files/{fileId}",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()))
                .andExpect(status().isNoContent());

        verify(this.fileService, times(1)).delete(
                any(UUID.class));
    }
}
