package com.lip6.domain.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
@NamedQuery(name = "Contact.CountContactByEmail",
    query = "SELECT count(c) FROM Contact c WHERE c.email=:mail")
// @NamedNativeQuery(name = "Contact.FindContactByIdNative", query = "SELECT * FROM contact WHERE
// idContact=?", resultClass =
// Contact.class)
public class Contact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idContact;

  private String firstName;

  private String lastName;

  private String email;

  @OneToOne(cascade = CascadeType.PERSIST, mappedBy = "contact")
  private Address address;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "contact", orphanRemoval = true)
  Set<PhoneNum> phones = new HashSet<PhoneNum>();

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "CTC_GRP", joinColumns = @JoinColumn(name = "CTC_ID"),
      inverseJoinColumns = @JoinColumn(name = "GRP_ID"))
  private Set<ContactGroup> contactGroups = new HashSet<>();

  public Contact() {}
   
  public Contact(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstname) {
    this.firstName = firstname;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastname) {
    this.lastName = lastname;
  }

  public long getIdContact() {
    return idContact;
  }

  public void setIdContact(long idContact) {
    this.idContact = idContact;
  }

  public Set<PhoneNum> getPhones() {
    return phones;
  }

  public Address getAddress() {
    return address;
  }

  public void setPhones(Set<PhoneNum> phones) {
    this.phones = phones;
  }

  public void setAddress(Address address) {
    this.address = address;
    address.setContact(this);
  }

  public Set<ContactGroup> getContactGroups() {
    return contactGroups;
  }

  public void setContactGroups(Set<ContactGroup> contactGroups) {
    this.contactGroups = contactGroups;
  }

  @PrePersist
  private void prePersist() {
    phones.forEach(c -> c.setContact(this));
  }

  @Override
  public String toString() {
    return "Contact [idContact=" + idContact + ", firstName=" + firstName + ", lastName=" + lastName
        + ", email="
        + email + ", address=" + address + ", phones=" + phones + ", contactGroups=" + contactGroups
        + "]";
  }
}
