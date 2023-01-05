package com.lip6.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idAddress;

	@OneToOne
	@JoinColumn(name="id_user")
	private Contact contact;

	private String rue;

	private String departement;

	private String ville;

	public Address() {

	}

	public Address(String rue, String departement, String ville) {
		this.rue = rue;
		this.departement = departement;
		this.ville = ville;
	}

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

	@Override
	public String toString() {
		return "Address [idAddress=" + idAddress + ", contact=" + contact + ", rue=" + rue + ", departement="
				+ departement + ", ville=" + ville + "]";
	}

}
