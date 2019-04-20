package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.exception.FolderNotFoundException;
import com.mydisc.MyDisc.resource.FileResource;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FolderService;
import com.mydisc.MyDisc.utils.FileResourceBodyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FolderService folderService;

    @GetMapping(value = "/folders/root/files/{fileId}", produces = { "application/hal+json" })
    public FileResource get(@PathVariable("fileId") UUID fileId) {
        if (!fileService.exists(fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        File resultFile = fileService.findById(fileId);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @GetMapping(value = "/folders/{folderId}/files/{fileId}", produces = { "application/hal+json" })
    public FileResource get(@PathVariable("folderId") UUID folderId, @PathVariable("fileId") UUID fileId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!fileService.exists(folderId, fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        File resultFile = fileService.findById(folderId, fileId);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @GetMapping(value = "/downloads/{fileId}", produces = { "application/hal+json" })
    public ResponseEntity<Resource> download(@PathVariable("fileId") UUID fileId) {
        if (!fileService.exists(fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        return fileService.download(fileId);
    }

    @GetMapping(value = "/folders/root/files", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FileResource>> list() {
        List<File> files = fileService.list();
        Resources<FileResource> resources = fileService.getFileResources(files);

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FileResource>> list(@PathVariable("folderId") UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        List<File> files = fileService.list(folderId);
        Resources<FileResource> resources = fileService.getFileResources(files);

        return ResponseEntity.ok(resources);
    }

    @PostMapping(value = "/folders/root/files", produces = { "application/hal+json" })
    public FileResource upload(@RequestPart("file")MultipartFile file) {
        File resultFile = fileService.upload(file);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @PostMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public FileResource upload(@PathVariable("folderId") UUID folderId, @RequestPart("file")MultipartFile file) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        File resultFile = fileService.upload(folderId, file);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @PatchMapping(value = "/folders/root/files/{fileId}/move")
    public ResponseEntity moveFile(
            @PathVariable("fileId") UUID fileId,
            @Valid @RequestBody FilePojo filePojo
    ) {
        if (!fileService.exists(fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        fileService.move(fileId, filePojo);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/folders/{folderId}/files/{fileId}/move")
    public ResponseEntity moveFile(
            @PathVariable("folderId") UUID folderId,
            @PathVariable("folderId") UUID fileId,
            @Valid @RequestBody FilePojo filePojo
    ) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!fileService.exists(folderId, fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        fileService.move(folderId, fileId, filePojo);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/folders/root/files/{fileId}")
    public ResponseEntity delete(@PathVariable UUID fileId) {
        if (!fileService.exists(fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        fileService.delete(fileId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/folders/{folderId}/files/{fileId}")
    public ResponseEntity delete(@PathVariable UUID folderId, @PathVariable UUID fileId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!fileService.exists(folderId, fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        fileService.delete(fileId);

        return ResponseEntity.noContent().build();
    }
}
