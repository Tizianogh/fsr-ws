package com.lip6.domain.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class ContactGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idContactGroup;

  private String libelle;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST},
      mappedBy = "contactGroups")
  @JsonIgnore
  private Set<Contact> contacts = new HashSet<Contact>();

  public ContactGroup() {}

  public ContactGroup(String libelle) {
    this.libelle = libelle;
  }

  // public ContactGroup(String libelle, Set<Contact> contact) {
  // this.libelle=libelle;
  // this.contacts=contact;
  // }

  public Set<Contact> entityManager() {
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

  public Set<Contact> getContacts() {
    return contacts;
  }

  public void removeContact(Contact c) {
    this.contacts.remove(c);
    c.getContactGroups().remove(this);
  }

  public void addContact(Contact c) {
    this.contacts.add(c);
    c.getContactGroups().add(this);
  }


  @Override
  public String toString() {
    return "ContactGroup [idContactGroup=" + idContactGroup + ", libelle=" + libelle + ", contacts="
        + contacts + "]";
  }
}
