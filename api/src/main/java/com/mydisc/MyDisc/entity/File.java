package com.mydisc.MyDisc.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name="file")
public class File extends BaseEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID id;

    @Nullable
    @ManyToOne()
    @JoinColumn(name="folder_id")
    private Folder folder;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name="storage_name")
    private String storageName;

    @NotNull
    @Column(name="type")
    private String type;

    @NotNull
    @Column(name="size")
    private long size;

    @Nullable
    @Column(name="deleted")
    private boolean deleted;

    public UUID getId() {
        return id;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean hasFolder() {
        return null != this.getFolder();
    }
}
