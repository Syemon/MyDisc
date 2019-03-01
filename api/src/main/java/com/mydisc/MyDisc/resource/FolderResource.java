package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.Folder;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class FolderResource extends ResourceSupport {
    private Folder folder;
    private Map<String, String> body;

    public FolderResource(Map<String, String> body) {
        this.body = body;

        add(linkTo(methodOn(FolderController.class).get()).withSelfRel());
        add(linkTo(methodOn(FolderController.class).listChildren()).withRel("children"));
    }

    public FolderResource(Folder folder, Map<String, String> body) {
        this.folder = folder;
        this.body = body;

        UUID id = folder.getId();

        add(linkTo(methodOn(FolderController.class).get(id)).withSelfRel());

        if (null != folder.getParent()) {
            add(linkTo(methodOn(FolderController.class).get(folder.getParent().getId())).withRel("parent"));
            add(linkTo(methodOn(FolderController.class).listChildren(folder.getParent().getId())).withRel("children"));
        } else {
            add(linkTo(methodOn(FolderController.class).listChildren()).withRel("children"));
        }

        add(linkTo(methodOn(FolderController.class).delete(id)).withRel("delete"));
    }

    @JsonProperty("folder")
    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
