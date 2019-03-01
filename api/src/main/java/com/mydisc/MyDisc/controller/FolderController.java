package com.mydisc.MyDisc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderPojo;
import com.mydisc.MyDisc.exception.NotFoundFolderException;
import com.mydisc.MyDisc.resource.FolderResource;
import com.mydisc.MyDisc.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
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

    @GetMapping(value = "/folders/root/children", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FolderResource>> listChildren() {
        List<Folder> folders = folderService.findChildren();

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

    @GetMapping(value = "/folders/{folderId}/children", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FolderResource>> listChildren(@PathVariable UUID folderId) {
        List<Folder> folders = folderService.findChildren(folderId);

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

    @GetMapping("/folders/root")
    public FolderResource get() {
        Map<String, String> body = new HashMap<>();
        body.put("id", "root");
        body.put("name", "root");

        return new FolderResource(body);
    }

    @GetMapping("/folders/{folderId}")
    public FolderResource get(@PathVariable UUID folderId) {
        try {
            folderService.findById(folderId);
        } catch (Exception exception) {
            throw new NotFoundFolderException("Not found - " + folderId);
        }
        Folder folder = folderService.findById(folderId);

        Map<String, String> body = new HashMap<>();
        body.put("id", folder.getId().toString());
        body.put("name", folder.getName());
        
        return new FolderResource(folder, body);
    }


    @PostMapping(value = "/folders", produces = { "application/hal+json" })
    public FolderResource create(@RequestBody FolderPojo folderPojo) {
        Folder folder = folderService.save(folderPojo);

        Map<String, String> body = new HashMap<>();
        body.put("name", folder.getName());

        return new FolderResource(folder, body);
    }

    @PatchMapping(value = "/folders/{folderId}", produces = { "application/hal+json" })
    public FolderResource rename(@PathVariable UUID folderId, @RequestBody FolderPojo folderPojo) {
        try {
            folderService.findById(folderId);
        } catch (Exception exc) {
            throw new NotFoundFolderException("Not found - " + folderId);
        }

        folderPojo.setId(folderId);
        Folder folder = folderService.rename(folderPojo);

        Map<String, String> body = new HashMap<>();
        body.put("name", folder.getName());

        return new FolderResource(folder, body);
    }

    @DeleteMapping("/folders/{folderId}")
    public ResponseEntity delete(@PathVariable UUID folderId) {
        try {
            folderService.findById(folderId);
        } catch (Exception exc) {
            throw new NotFoundFolderException("Not found - " + folderId);
        }
        folderService.delete(folderId);

        return ResponseEntity.noContent().build();
    }
}
