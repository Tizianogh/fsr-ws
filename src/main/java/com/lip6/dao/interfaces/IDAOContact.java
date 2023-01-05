package com.lip6.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.lip6.domain.model.Contact;

public interface IDAOContact {
	public Contact addContact(Contact contact);

	public boolean deleteContact(long id);

	public Contact getContact(long id);

	public boolean modifyContact(long id, String firstname, String lastname, String email);

	public ArrayList<Contact> getContactByFirstName(String firstname);

	public ArrayList<Contact> getContactByLastName(String lastname);
	
	public List<Contact> getAllInformationsAboutContactByHisEmail(String lastname);

	public int getNumberOfContactByEmail(String email);
}
