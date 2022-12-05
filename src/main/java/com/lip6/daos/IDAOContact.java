package com.lip6.daos;

import java.util.ArrayList;

import com.lip6.entities.Contact;

public interface IDAOContact {

	
	public void addContact( String firstname, String lastname, String email);
	
	public boolean deleteContact(long id);
	public void addContactFromBean();	
	public Contact getContact(long id);
	
	public void addContact();
	
	public boolean modifyContact(long id, String firstname, String lastname, String email);
	
	public ArrayList<Contact> getContactByFirstName(String firstname);
	
	public ArrayList<Contact> getContactByLastName(String lastname);
	
	public ArrayList<Contact> getContactByEmail(String email);
	
}
