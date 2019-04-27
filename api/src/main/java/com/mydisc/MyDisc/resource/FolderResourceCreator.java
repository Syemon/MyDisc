package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydisc.MyDisc.entity.Folder;
import org.springframework.hateoas.Resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderResourceCreator {
    public static Resources<FolderResource> getResources(List<Folder> folders) {
        List<FolderResource> folderResources = new ArrayList<>();

        for (Folder folder : folders) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> body = new HashMap<>();
            body.put("name", folder.getName());

            folderResources.add(new FolderResource(folder, body));
        }

        return new Resources<>(folderResources);
    }

    public static FolderResource getResource() {
        Map<String, String> body = new HashMap<>();
        body.put("id", "root");
        body.put("name", "root");

        return new FolderResource(body);
    }

    public static FolderResource getResource(Folder folder) {
        Map<String, String> body = new HashMap<>();
        body.put("id", folder.getId().toString());
        body.put("name", folder.getName());

        return new FolderResource(folder, body);
    }
}
