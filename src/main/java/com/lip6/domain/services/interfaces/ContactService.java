package com.lip6.domain.services.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.lip6.domain.model.Contact;

public interface ContactService {
	public void deleteContact(long id);
	
	public Optional<Contact> createContact(Contact contact);
	
	public int verifyIfEmailExistInDatabase(Contact contact);
	
	public List<Contact> retrieveAllInformationsAboutContactByHisEmail(String email);
	
	public Optional<Contact> createContactFromBean(Contact contact);
}