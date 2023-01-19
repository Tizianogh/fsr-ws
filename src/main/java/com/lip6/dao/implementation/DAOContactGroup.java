package com.lip6.dao.implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.lip6.dao.interfaces.IDAOContact;
import com.lip6.dao.interfaces.IDAOContactGroup;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.model.DTO.ContactGroupDTO;

@Service
@Qualifier("daoContactGroup")
public class DAOContactGroup implements IDAOContactGroup {
  @Autowired
  private EntityManagerFactory emf;

  @Autowired
  @Qualifier("daoContact")
  private IDAOContact daoc;

  @Override
  public ContactGroup createContactGroup(ContactGroup contactGroup) {
    EntityManager entityManager = this.emf.createEntityManager();
    EntityTransaction tx = entityManager.getTransaction();

    tx.begin();
    entityManager.persist(contactGroup);
    tx.commit();
    entityManager.close();

    return contactGroup;
  }

  @Override
  public ContactGroupDTO getContactGroupById(Long idGroupContact) {
    EntityManager entityManager = this.emf.createEntityManager();

    String getDTOContactGroupByIdRequest =
        "SELECT NEW com.lip6.domain.model.DTO.ContactGroupDTO(cg.idContactGroup, cg.libelle) from ContactGroup cg where cg.idContactGroup= :id";

    ContactGroupDTO contactGroupDTO =
        entityManager.createQuery(getDTOContactGroupByIdRequest, ContactGroupDTO.class)
            .setParameter("id", idGroupContact).getSingleResult();

    entityManager.close();
    return contactGroupDTO;
  }

  @Override
  public ContactGroup getContactGroupWithFetch(Long idGroupContact) {
    String request =
        "SELECT DISTINCT c FROM ContactGroup c LEFT JOIN FETCH c.contacts WHERE c.idContactGroup= :idContactGroup";
    EntityManager entityManager = this.emf.createEntityManager();

    ContactGroup contactGroupProxyById =
        entityManager.createQuery(request, ContactGroup.class)
            .setParameter("idContactGroup", idGroupContact).getSingleResult();


    return contactGroupProxyById;
  }

  @Override
  public ContactGroup addContactToGroup(Contact emailContact, Long idContactGroup) {
    EntityManager entityManager = this.emf.createEntityManager();
    EntityTransaction tx = entityManager.getTransaction();

    Contact contactByEmail =
        this.daoc.getAllInformationsAboutContactByHisEmail(emailContact.getEmail()).get(0);
    ContactGroup cgById = this.getContactGroupWithFetch(idContactGroup);
    tx.begin();

    cgById.getContacts().add(contactByEmail);
    contactByEmail.getContactGroups().add(cgById);

    entityManager.merge(cgById);
    entityManager.merge(contactByEmail);

    tx.commit();

    entityManager.close();

    return cgById;
  }

  @Override
  public ContactGroup getGroupContactById(Long idGroupContact) {
    EntityManager entityManager = this.emf.createEntityManager();
    ContactGroup find = entityManager.find(ContactGroup.class, idGroupContact);
    entityManager.close();
    return find;

  }

  @Override
  public ContactGroup deletContactFromGroup(Contact emailContact, Long idContactGroup) {
    EntityManager entityManager = this.emf.createEntityManager();
    EntityTransaction tx = entityManager.getTransaction();
    Contact contactByEmail =
        this.daoc.getAllInformationsAboutContactByHisEmail(emailContact.getEmail()).get(0);
    ContactGroup cgById = this.getContactGroupWithFetch(idContactGroup);

    tx.begin();

    cgById.getContacts().stream().filter($ -> $.getIdContact() == contactByEmail.getIdContact())
        .forEach($ -> cgById.getContacts().remove($));
    contactByEmail.getContactGroups().stream()
        .filter($ -> $.getIdContactGroup() == cgById.getIdContactGroup())
        .forEach($ -> contactByEmail.getContactGroups().remove($));

    entityManager.merge(cgById);
    entityManager.merge(contactByEmail);

    tx.commit();
    entityManager.close();

    return cgById;
  }

  @Override
  public boolean deleteContactGroup(Long idContactGroup) {
    EntityManager entityManager = this.emf.createEntityManager();
    EntityTransaction tx = entityManager.getTransaction();
    ContactGroup cgById = entityManager.find(ContactGroup.class, idContactGroup);
    if (cgById != null) {
      tx.begin();
      entityManager.remove(cgById);

      for (Contact contact : cgById.getContacts()) {
        contact.getContactGroups().remove(cgById);
        entityManager.merge(contact);
      }

      tx.commit();

      entityManager.close();
      return true;
    }
    return false;
  }

}
