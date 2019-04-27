package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.*;
import com.mydisc.MyDisc.exception.FolderNotFoundException;
import com.mydisc.MyDisc.exception.RequiredParameterException;
import com.mydisc.MyDisc.resource.FolderResource;
import com.mydisc.MyDisc.resource.FolderResourceCreator;
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

    @GetMapping(value = "/folders/root/children", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FolderResource>> listChildren() {
        List<Folder> folders = folderService.findChildren();

        return ResponseEntity.ok(
                FolderResourceCreator.getResources(folders)
        );
    }

    @GetMapping(value = "/folders/{folderId}/children", produces = { "application/hal+json" })
    public ResponseEntity<Resources<FolderResource>> listChildren(@PathVariable UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        List<Folder> folders = folderService.findChildren(folderId);

        return ResponseEntity.ok(
                FolderResourceCreator.getResources(folders)
        );
    }

    @GetMapping("/folders/root")
    public FolderResource get() {
        return FolderResourceCreator.getResource();
    }

    @GetMapping("/folders/{folderId}")
    public FolderResource get(@PathVariable UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        Folder folder = folderService.findById(folderId);

        return FolderResourceCreator.getResource(folder);
    }

    @PostMapping(value = "/folders", produces = { "application/hal+json" })
    public FolderResource create(@RequestBody FolderPojo folderPojo) {
        if (null != folderPojo.getParentId()) {
            UUID folderId = UUID.fromString(folderPojo.getParentId());
            if (!folderService.exists(folderId)) {
                throw new FolderNotFoundException("Folder was not found");
            }
        }

        Folder folder = folderService.save(folderPojo);

        return FolderResourceCreator.getResource(folder);
    }

    @PatchMapping(value = "/folders/{folderId}", produces = { "application/hal+json" })
    public FolderResource rename(@PathVariable UUID folderId, @RequestBody FolderPojo folderPojo) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (null == folderPojo.getName()) {
            throw new RequiredParameterException("Name is required");
        }

        folderPojo.setId(folderId);
        Folder folder = folderService.rename(folderPojo);

        return FolderResourceCreator.getResource(folder);
    }

    @DeleteMapping("/folders/{folderId}")
    public ResponseEntity delete(@PathVariable UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        folderService.delete(folderId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/folders/{folderId}/move/root")
    public ResponseEntity moveFile(@PathVariable("folderId") UUID folderId) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        folderService.move(folderId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/folders/{folderId}/move/{targetFolderId}")
    public ResponseEntity moveFile(
            @PathVariable("folderId") UUID folderId,
            @PathVariable("targetFolderId") UUID targetFolderId
    ) {
        if (!folderService.exists(folderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }
        if (!folderService.exists(targetFolderId)) {
            throw new FolderNotFoundException("Folder was not found");
        }

        folderService.move(folderId, targetFolderId);

        return ResponseEntity.noContent().build();
    }
}
