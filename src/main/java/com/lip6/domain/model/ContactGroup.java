package com.lip6.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ContactGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idContactGroup;

    private String libelle;

    @ManyToMany(cascade = CascadeType.PERSIST, mappedBy = "contactGroups")
    @JsonIgnore
    private Set<Contact> contacts = new HashSet<Contact>();

    public ContactGroup() {
    }

    public ContactGroup(String libelle) {

        this.libelle = libelle;
    }

    public Set<Contact> getContacts() {
        return this.contacts;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public long getIdContactGroup() {
        return idContactGroup;
    }

    public void setIdContactGroup(long idContactGroup) {
        this.idContactGroup = idContactGroup;
    }
}
