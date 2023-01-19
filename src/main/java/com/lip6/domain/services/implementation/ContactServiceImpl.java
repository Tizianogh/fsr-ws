package com.lip6.domain.services.implementation;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lip6.dao.interfaces.IDAOContact;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.DTO.ContactDTO;
import com.lip6.domain.services.interfaces.ContactService;

@Service
@Qualifier("contactService")
@Transactional
public class ContactServiceImpl implements ContactService {

  @Autowired
  @Qualifier("daoContact")
  private IDAOContact daoc;

  @Override
  public Optional<Contact> createContact(Contact contact) {
    int resultOfContactByEmail = this.verifyIfEmailExistInDatabase(contact);

    if (resultOfContactByEmail > 0) {
      return Optional.empty();
    }

    System.out
        .println("Le contact : " + contact.getFirstName() + ", " + contact.getLastName() + ", "
            + contact.getEmail() + ", a été ajouté en base.");
    return Optional.of(this.daoc.addContact(contact));
  }

  @Override
  public Optional<Contact> createContactFromBean(Contact contact) {
    // TODO Auto-generated method stub
    return Optional.empty();
  }

  @Override
  public int verifyIfEmailExistInDatabase(Contact contact) {
    int resultOfContactByEmail = this.daoc.getNumberOfContactByEmail(contact.getEmail());

    if (resultOfContactByEmail > 0) {
      System.out.println(
          "L'email de l'utilisateur : " + contact.getFirstName() + ", a été retrouvé en base.");
    } else {
      System.out.println(
          "Aucun utilisateur trouvé en base de données avec l'email suivant : "
              + contact.getEmail());
    }

    return resultOfContactByEmail;
  }

  @Override
  public boolean deleteContact(long id) {
    boolean deletedContact = daoc.deleteContact(id);
    if (deletedContact) {
      System.out.println("Contact supprimé");
      return deletedContact;
    }
    return !deletedContact;
  }

  @Override
  public List<Contact> retrieveAllInformationsAboutContactByHisEmail(String email) {
    List<Contact> allInformationsAboutUserByHisEmail =
        this.daoc.getAllInformationsAboutContactByHisEmail(email);

    if (allInformationsAboutUserByHisEmail.isEmpty()) {
      System.out.println(
          "Il n'existe pas d'utilisateur en base de données qui possède cet email : " + email);
    }

    return allInformationsAboutUserByHisEmail;
  }

  @Override
  public Optional<ContactDTO> getContactById(long id) {
    Optional<ContactDTO> contactById = Optional.ofNullable(this.daoc.getContact(id));

    if (!contactById.isPresent()) {
      System.out.println("Aucun contact avec l'id " + id + " trouvé en base de données.");
      return Optional.empty();
    }
    System.out
        .println("Le contact : " + contactById.get().getEmail() + ", a été retrouvé en base.");

    return contactById;
  }

  @Override
  public List<Contact> getAllContacts() {
    List<Contact> contacts = this.daoc.getAllContacts();
    if (contacts.isEmpty())
      System.out.println("Aucun contact en base de données");

    return contacts;
  }

  @Override
  public int updateContact(Contact contact) {
    int numberOfUpdatedRows = this.daoc.updateContact(contact);

    if (numberOfUpdatedRows >= 1) {
      System.out.println(numberOfUpdatedRows + " ligne(s) modifiée(s)");
    }
    return numberOfUpdatedRows;
  }
}
