package com.ikarabulut.shareable.common.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
public class FileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false, insertable = false, unique = true)
    private Long id;
    private final UUID uuid = UUID.randomUUID();
    @NotNull
    @Pattern(regexp = "^[^.]+\\.[a-zA-Z]{3}$")
    private String name;
    @NotNull
    private String description;
    private Boolean isMalware;
    private int ownedBy;
    @NotNull
    private String signature;
    private String extension;


    public Long getId() { return this.id; }

    public UUID getUuid() { return this.uuid; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsMalware() { return this.isMalware; }
    public void setIsMalware(Boolean isMalware) { this.isMalware = isMalware; }

    public int getOwnedBy() { return this.ownedBy; }
    public void setOwnedBy(int ownedBy) { this.ownedBy = ownedBy; }

    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public void setExtension(String extension) { this.extension = extension; }

    public String getExtension() { return this.extension; }
}