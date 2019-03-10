package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.exception.FileNotFoundException;
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
        try {
            File resultFile = fileService.findById(fileId);
        } catch (Exception exc) {
            throw new FileNotFoundException("File was not found");
        }

        File resultFile = fileService.findById(fileId);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @GetMapping(value = "/folders/{folderId}/files/{fileId}", produces = { "application/hal+json" })
    public FileResource get(@PathVariable("folderId") UUID folderId, @PathVariable("fileId") UUID fileId) {
        try {
            folderService.findById(folderId);
        } catch (Exception exc) {
            throw new FolderNotFoundException("Not found - " + folderId);
        }

        try {
            File resultFile = fileService.findById(fileId);
        } catch (Exception exc) {
            throw new FileNotFoundException("File was not found");
        }

        File resultFile = fileService.findById(folderId, fileId);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }

    @GetMapping(value = "/downloads/{fileId}", produces = { "application/hal+json" })
    public ResponseEntity<Resource> download(@PathVariable("fileId") UUID fileId) {
        try {
            File resultFile = fileService.findById(fileId);
        } catch (Exception exc) {
            throw new FileNotFoundException("File was not found");
        }

        return fileService.download(fileId);
    }

    @GetMapping(value = "/folders/root/files", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FileResource>> list() {
        Resources<FileResource> resources = fileService.list();

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FileResource>> list(@PathVariable("folderId") UUID folderId) {
        Resources<FileResource> resources = fileService.list(folderId);

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
        File resultFile = fileService.upload(folderId, file);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }
}
