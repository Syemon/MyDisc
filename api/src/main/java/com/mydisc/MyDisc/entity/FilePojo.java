package com.mydisc.MyDisc.entity;

import java.util.Optional;
import java.util.UUID;

public class FilePojo {
    private UUID folderId;

    public FilePojo() {
    }

    public Optional<UUID> getFolderId() {
        return Optional.ofNullable(folderId);
    }

    public void setFolderId(UUID folderId) {
        this.folderId = folderId;
    }
}