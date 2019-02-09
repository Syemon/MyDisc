package com.mydisc.MyDisc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import com.mydisc.MyDisc.exception.NotFoundException;
import com.mydisc.MyDisc.resource.FolderResource;
import com.mydisc.MyDisc.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class FolderController {
    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping(value = "/folders", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FolderResource>> list() {
        List<Folder> folders = folderService.findAll();

        List<FolderResource> folderResources = new ArrayList<>();

        for (Folder folder : folders) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = new HashMap<>();
            body.put("name", folder.getName());

            folderResources.add(new FolderResource(folder, body));
        }

        Resources<FolderResource> resources = new Resources<>(folderResources);

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/folders/{folderId}")
    public Folder get(@PathVariable UUID folderId) {
        try {
            folderService.findById(folderId);
        } catch (Exception exception) {
            throw new NotFoundException("Not found - " + folderId);
        }

        return folderService.findById(folderId);
    }


    @PostMapping(value = "/folders", produces = { "application/hal+json" })
    public FolderResource create(@RequestBody FolderPojo folderPojo) {
        Folder folder = folderService.save(folderPojo);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> body = new HashMap<>();
        body.put("name", folder.getName());

        return new FolderResource(folder, body);
    }
//
//    @PutMapping("/folders")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public Folder update(@RequestBody FolderPojo folder) {
//        folderService.save(folder);
//
//        return folder;
//    }

    @DeleteMapping("/folders/{folderId}")
    public String delete(@PathVariable UUID folderId) {
        folderService.delete(folderId);

        return "success";
    }
}
