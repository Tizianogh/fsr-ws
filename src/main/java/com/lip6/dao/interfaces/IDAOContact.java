package com.lip6.dao.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.lip6.domain.model.Contact;
import com.lip6.domain.model.DTO.ContactDTO;

public interface IDAOContact {
    public Contact addContact(Contact contact);

    public boolean deleteContact(long id);

    public ContactDTO getContact(long id);

    public boolean modifyContact(long id, String firstname, String lastname, String email);

    public ArrayList<Contact> getContactByFirstName(String firstname);

    public ArrayList<Contact> getContactByLastName(String lastname);

    public List<Contact> getAllInformationsAboutContactByHisEmail(String lastname);

    public int getNumberOfContactByEmail(String email);

    public List<Contact> getAllContacts();
}
