package com.lip6.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PhoneNum {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idPhoneNum;
	
	private String num;

	@ManyToOne
	@JoinColumn(name = "id_contact")
	private Contact contact;

	public PhoneNum() {

	}
	
	

	public PhoneNum(String num) {
		this.num = num;
	}



	public String getNum() {
		return num;
	}



	public void setNum(String num) {
		this.num = num;
	}



	public void setContact(Contact contact) {
		this.contact = contact;
	}

}
