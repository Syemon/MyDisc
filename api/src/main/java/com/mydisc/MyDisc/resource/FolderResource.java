package com.mydisc.MyDisc.resource;

import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.Folder;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class FolderResource extends ResourceSupport {
    private Folder folderObj;
    private Object folder;

    public FolderResource(Folder folderObj, Object folder) {
        this.folderObj = folderObj;
        this.folder = folder;

        UUID id = folderObj.getId();

        add(linkTo(methodOn(FolderController.class).get(id)).withSelfRel());

        if (null != folderObj.getParent()) {
            add(linkTo(methodOn(FolderController.class).get(folderObj.getParent().getId())).withRel("parent"));
        }

        add(linkTo(methodOn(FolderController.class).delete(id)).withRel("delete"));
    }

    public Object getFolder() {
        return folder;
    }

    public void setFolder(Object folder) {
        this.folder = folder;
    }
}
