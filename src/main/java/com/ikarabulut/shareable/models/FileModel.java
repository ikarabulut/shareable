package com.ikarabulut.shareable.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FileModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private Boolean isMalware;
    private int ownedBy;
    private String signature;


    public Integer getId() { return this.id; }
    public void setId(Integer id) { this.id = id; }

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
}