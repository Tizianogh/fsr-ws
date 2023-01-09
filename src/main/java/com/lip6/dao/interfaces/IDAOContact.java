package com.lip6.dao.interfaces;

import java.util.List;

import com.lip6.domain.model.Contact;
import com.lip6.domain.model.DTO.ContactDTO;

public interface IDAOContact {
    public Contact addContact(Contact contact);

    public boolean deleteContact(long id);

    public ContactDTO getContact(long id);

    public List<Contact> getAllInformationsAboutContactByHisEmail(String lastname);

    public int getNumberOfContactByEmail(String email);

    public List<Contact> getAllContacts();

    public int updateContact(Contact contact);
}
