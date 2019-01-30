package com.mydisc.MyDisc.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @ManyToOne(cascade={CascadeType.ALL})
    @JoinColumn(name="parent_id")
    private Folder parent;

    @OneToMany(mappedBy="parent")
    private Set<Folder> children = new HashSet<Folder>();

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
        this.parent = parent;
        parent.addChild(this);
    }

    public Set<Folder> getChildren() {
        return children;
    }

    public void setChildren(Set<Folder> children) {
        this.children = children;
    }

    private void addChild(Folder child) {
        this.getChildren().add(child);
    }

    public void removeChild(Folder child) {
        this.getChildren().remove(child);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
