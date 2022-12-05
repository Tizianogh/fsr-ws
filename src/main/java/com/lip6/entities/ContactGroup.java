package com.lip6.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class ContactGroup {
	private String libelle;

	public ContactGroup() {
	}

	public ContactGroup(String libelle) {
		this.libelle = libelle;
	}
	
	public Set<Contact> getContacts(){
		return this.contacts;
	}
	

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idContactGroup;

	@ManyToMany(mappedBy = "contactGroups")
	private Set<Contact> contacts = new HashSet<Contact>();

}
