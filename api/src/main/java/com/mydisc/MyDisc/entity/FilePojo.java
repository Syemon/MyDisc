package com.mydisc.MyDisc.entity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class FilePojo {
    @NotNull
    private UUID folderId;

    public FilePojo() {
    }

    public UUID getFolderId() {
        return folderId;
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }
}