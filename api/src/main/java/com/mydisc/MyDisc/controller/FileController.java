package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.resource.FileResource;
import com.mydisc.MyDisc.service.FileService;
import com.mydisc.MyDisc.utils.FileResourceBodyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = "/folders/{folderId}/files", produces = { "application/hal+json" })
    public FileResource upload(@PathVariable("folderId") UUID folderId, @RequestPart("file")MultipartFile file) {
        File resultFile = fileService.upload(folderId, file);
        Map<String, String> body = FileResourceBodyProcessor.getFullFileBody(resultFile);

        return new FileResource(resultFile, body);
    }
}
