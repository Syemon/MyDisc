package com.mydisc.MyDisc.resource;

import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderBody;
import org.springframework.hateoas.Resources;

import java.util.*;

public class FolderResourceCreator {
    public static Resources<FolderResource> getResources(List<Folder> folders) {
        List<FolderResource> folderResources = new ArrayList<>();

        for (Folder folder : folders) {
            FolderBody body = new FolderBody();
            body.setId(folder.getId().toString())
                .setName(folder.getName());

            folderResources.add(new FolderResource(folder, body));
        }

        return new Resources<>(folderResources);
    }

    public static FolderResource getResource() {
        FolderBody body = new FolderBody();
        body.setId("root")
            .setName("root")
            .setCreatedAt(new Date())
            .setUpdatedAt(new Date());

        return new FolderResource(body);
    }

    public static FolderResource getResource(Folder folder) {
        FolderBody body = new FolderBody();
        body.setId(folder.getId().toString())
            .setName(folder.getName())
            .setCreatedAt(folder.getCreatedAt())
            .setUpdatedAt(folder.getUpdatedAt());

        return new FolderResource(folder, body);
    }
}
