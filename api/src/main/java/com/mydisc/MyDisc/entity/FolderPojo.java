package com.mydisc.MyDisc.entity;

import java.util.UUID;

public class FolderPojo {

    private UUID id;
    private String parentId;
    private String name;

    public FolderPojo() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
