package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.FilePojo;
import com.mydisc.MyDisc.exception.FolderNotFoundException;
import com.mydisc.MyDisc.exception.NotEnoughDiscSpaceException;
import com.mydisc.MyDisc.resource.FileResource;
import com.mydisc.MyDisc.resource.FileResourceCreator;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.service.FolderService;
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
    private FileService fileService;
    private FolderService folderService;

    @Autowired
    public FileController(FileService fileService, FolderService folderService) {
        this.fileService = fileService;
        this.folderService = folderService;
    }

    @GetMapping(value = "/folders/root/files/{fileId}", produces = { "application/hal+json" })
    public FileResource get(@PathVariable("fileId") UUID fileId) {
        if (!fileService.exists(fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        File file = fileService.findById(fileId);

        return FileResourceCreator.getResource(file);
    }

    @GetMapping(value = "/folders/{folderId}/files/{fileId}", produces = { "application/hal+json" })
    public FileResource get(@PathVariable("folderId") UUID folderId, @PathVariable("fileId") UUID fileId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!fileService.exists(folderId, fileId)) {
            throw new FolderNotFoundException("File was not found");
        }

        File file = fileService.findById(folderId, fileId);

        return FileResourceCreator.getResource(file);

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

        return ResponseEntity.ok(
                FileResourceCreator.getResources(files)
        );
    }

    @GetMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FileResource>> list(@PathVariable("folderId") UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        List<File> files = fileService.list(folderId);

        return ResponseEntity.ok(
                FileResourceCreator.getResources(files)
        );
    }

    @PostMapping(value = "/folders/root/files", produces = { "application/hal+json" })
    public FileResource upload(@RequestPart("file")MultipartFile file) {
        if (!fileService.isEnoughSpace(file)) {
            throw new NotEnoughDiscSpaceException("You don't have enough space");
        }

        File resultFile = fileService.upload(file);

        return FileResourceCreator.getResource(resultFile);
    }

    @PostMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public FileResource upload(@PathVariable("folderId") UUID folderId, @RequestPart("file")MultipartFile file) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!fileService.isEnoughSpace(file)) {
            throw new NotEnoughDiscSpaceException("You don't have enough space");
        }

        File resultFile = fileService.upload(folderId, file);

        return FileResourceCreator.getResource(resultFile);
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
