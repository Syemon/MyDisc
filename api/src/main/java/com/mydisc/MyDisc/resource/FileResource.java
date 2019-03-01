package com.mydisc.MyDisc.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mydisc.MyDisc.controller.FolderController;
import com.mydisc.MyDisc.entity.File;
import com.mydisc.MyDisc.entity.Folder;
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

        add(linkTo(methodOn(FolderController.class).get(id)).withSelfRel());

        if (null == file.getFolder()) {
            add(linkTo(methodOn(FolderController.class).get()).withRel("folder"));
        } else {
            add(linkTo(methodOn(FolderController.class).get(file.getId())).withRel("folder"));
        }
    }

    @JsonProperty("file")
    public Map<String, String> getBody() {
        return body;
    }

    public void setBody(Map<String, String> body) {
        this.body = body;
    }
}
