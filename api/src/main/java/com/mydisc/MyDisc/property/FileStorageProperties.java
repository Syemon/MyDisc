package com.mydisc.MyDisc.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private int storageSpace;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public int getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(int storageSpace) {
        this.storageSpace = storageSpace;
    }
}
