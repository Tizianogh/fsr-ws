package com.lip6.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long idAddress;

  @OneToOne
  private Contact contact;

  private String rue;

  private String departement;

  private String ville;

  private String pays;

  public Address() {

  }

  public Address(String rue, String departement, String ville, String pays) {
    this.rue = rue;
    this.departement = departement;
    this.ville = ville;
    this.pays = pays;
  }

  // public Address(String rue, String departement, String ville, String pays, Contact contact) {
  // this.rue = rue;
  // this.departement = departement;
  // this.ville = ville;
  // this.pays = pays;
  // this.contact=contact;
  // }

  public String getRue() {
    return rue;
  }

  public void setRue(String rue) {
    this.rue = rue;
  }

  public String getDepartement() {
    return departement;
  }

  public void setDepartement(String departement) {
    this.departement = departement;
  }

  public String getVille() {
    return ville;
  }

  public void setVille(String ville) {
    this.ville = ville;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public String getPays() {
    return pays;
  }

  public void setPays(String pays) {
    this.pays = pays;
  }

  @Override
  public String toString() {
    return "Address [idAddress=" + idAddress + ", contact=" + contact + ", rue=" + rue
        + ", departement=" + departement
        + ", ville=" + ville + ", pays=" + pays + "]";
  }

}
