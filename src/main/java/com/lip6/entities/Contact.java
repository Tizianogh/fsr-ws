package com.lip6.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Contact {

	private String firstName;
	private String lastName;
	private String email;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idContact;

	public Contact() {
	}

	public Contact(String firstName, String lastName, String email, long idContact) {
		this(firstName, lastName, email);
		this.idContact = idContact;
	}

	public Contact(String firstName, String lastName, String email) {
		super();
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

	public void setAddress(Address address) {
		this.address = address;
	}
	
	

	public Set<ContactGroup> getContactGroups() {
		return contactGroups;
	}

	public void setContactGroups(Set<ContactGroup> contactGroups) {
		this.contactGroups = contactGroups;
	}



	@OneToOne(cascade = CascadeType.ALL, mappedBy = "contact")
	private Address address;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contact")
	Set<PhoneNum> phones = new HashSet<PhoneNum>();

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "CTC_GRP", joinColumns = @JoinColumn(name = "CTC_ID"), inverseJoinColumns = @JoinColumn(name = "GRP_ID"))
	private Set<ContactGroup> contactGroups = new HashSet<>();
}
