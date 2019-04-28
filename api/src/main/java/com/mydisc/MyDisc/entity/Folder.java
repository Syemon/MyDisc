package com.mydisc.MyDisc.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name="folder")
public class Folder {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    private UUID id;

    @NotNull
    @Column(name="name")
    private String name;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="parent_id")
    private Folder parent;

    @Nullable
    @OneToMany(mappedBy="parent", cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Folder> children;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Folder() {

    }

    public Folder(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
        if (parent == null) {
            if (null != this.parent) {
                this.parent.removeChild(this);
            }
            this.parent = null;
            return;
        }

        this.parent = parent;

        if (parent.getChildren() == null) {
            parent.children = new ArrayList<Folder>();
        }

        if (!parent.children.contains(this)) {
            parent.getChildren().add(this);
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Folder> getChildren() {
        return children;
    }

    public void addChild(Folder child) {
        if (children == null) {
            children = new ArrayList<Folder>();
        }

        children.add(child);
        child.setParent(this);
    }

    public void removeChild(Folder child) {
        this.getChildren().remove(child);
    }

    public boolean hasChildren() {
        if (null == this.children) {
            return false;
        } else if (this.children.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean hasParent() {
        return null != this.getParent();
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
