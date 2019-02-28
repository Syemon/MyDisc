package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public File upload(@PathVariable("folderId") UUID folderId, @RequestPart("file")MultipartFile file) {
        return fileService.upload(folderId, file);
    }
}
