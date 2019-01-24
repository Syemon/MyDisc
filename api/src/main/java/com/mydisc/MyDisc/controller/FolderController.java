package com.mydisc.MyDisc.controller;

import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class FolderController {
    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/folders")
    public List<Folder> list() {
        return folderService.findAll();
    }

    @GetMapping("/folders/{folderId}")
    public Folder get(@PathVariable UUID folderId) {
        return folderService.findById(folderId);
    }

    @PostMapping("/folders")
    public Folder create(@RequestBody Folder folder) {
        folderService.save(folder);

        return folder;
    }

    @PutMapping("/folders")
    public Folder update(@RequestBody Folder folder) {
        folderService.save(folder);

        return folder;
    }

    @DeleteMapping("/folders/{folderId}")
    public String delete(@PathVariable UUID folderId) {
        folderService.delete(folderId);

        return "success";
    }
}
