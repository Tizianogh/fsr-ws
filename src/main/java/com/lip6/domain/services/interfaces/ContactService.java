package com.lip6.domain.services.interfaces;

import java.util.List;
import java.util.Optional;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.DTO.ContactDTO;

public interface ContactService {
  public boolean deleteContact(long id);

  public Optional<Contact> createContact(Contact contact);

  public int verifyIfEmailExistInDatabase(Contact contact);

  public List<Contact> retrieveAllInformationsAboutContactByHisEmail(String email);

  public Optional<Contact> createContactFromBean(Contact contact);

  public Optional<ContactDTO> getContactById(long id);

  public List<Contact> getAllContacts();

  public int updateContact(Contact contact);

}
