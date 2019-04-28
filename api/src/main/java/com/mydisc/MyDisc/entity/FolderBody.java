package com.mydisc.MyDisc.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class FolderBody {
    private String id;

    private String name;

    @JsonProperty("created_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createdAt;

    @JsonProperty("updated_at")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date updatedAt;

    public FolderBody() {
    }

    public String getId() {
        return id;
    }

    public FolderBody setId(String id) {
        this.id = id;

        return this;
    }

    public String getName() {
        return name;
    }

    public FolderBody setName(String name) {
        this.name = name;

        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public FolderBody setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;

        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public FolderBody setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;

        return this;
    }
}
