package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mydisc.MyDisc.controller.FileController;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.File;
import org.springframework.hateoas.ResourceSupport;

import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class FileResource extends ResourceSupport {
    private File file;
    private Map<String, String> body;

    public FileResource(File file, Map<String, String> body) {
        this.file = file;
        this.body = body;

        UUID id = file.getId();

        if (file.hasFolder()) {
            add(linkTo(methodOn(FileController.class).get(file.getFolder().getId(), id)).withSelfRel());
            add(linkTo(methodOn(FolderController.class).get(file.getFolder().getId())).withRel("folder"));
            add(linkTo(methodOn(FileController.class).delete(file.getFolder().getId(), id)).withRel("delete"));
        } else {
            add(linkTo(methodOn(FileController.class).get(id)).withSelfRel());
            add(linkTo(methodOn(FolderController.class).get()).withRel("folder"));
            add(linkTo(methodOn(FileController.class).delete(id)).withRel("delete"));
        }

        add(linkTo(methodOn(FileController.class).download(file.getId())).withRel("download"));
    }

    @JsonProperty("file")
    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
