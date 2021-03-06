package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mydisc.MyDisc.controller.FileController;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.Folder;
import com.mydisc.MyDisc.entity.FolderBody;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class FolderResource extends ResourceSupport {
    private FolderBody body;

    public FolderResource(FolderBody body) {
        this.body = body;

        add(linkTo(methodOn(FolderController.class).get()).withSelfRel());
        add(linkTo(methodOn(FolderController.class).listChildren()).withRel("children"));
        add(linkTo(methodOn(FileController.class).list()).withRel("files"));
    }

    public FolderResource(Folder folder, FolderBody body) {
        this.body = body;

        UUID id = folder.getId();

        add(linkTo(methodOn(FolderController.class).get(id)).withSelfRel());

        if (folder.hasParent()) {
            add(linkTo(methodOn(FolderController.class).get(folder.getParent().getId())).withRel("parent"));
            add(linkTo(methodOn(FolderController.class).listChildren(id)).withRel("children"));
            add(linkTo(methodOn(FileController.class).list(id)).withRel("files"));
        } else if (folder.hasChildren()) {
            add(linkTo(methodOn(FolderController.class).get()).withRel("parent"));
            add(linkTo(methodOn(FolderController.class).listChildren(id)).withRel("children"));
            add(linkTo(methodOn(FileController.class).list(id)).withRel("files"));
        } else {
            add(linkTo(methodOn(FolderController.class).get()).withRel("parent"));
            add(linkTo(methodOn(FolderController.class).listChildren(id)).withRel("children"));
            add(linkTo(methodOn(FileController.class).list(id)).withRel("files"));
        }

        add(linkTo(methodOn(FolderController.class).delete(id)).withRel("delete"));
    }

    @JsonProperty("folder")
    public FolderBody getBody() {
        return body;
    }
}
