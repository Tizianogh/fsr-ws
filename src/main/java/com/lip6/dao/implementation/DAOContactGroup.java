package com.lip6.dao.implementation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.hibernate.annotations.QueryHints;
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
  public ContactGroup getContactGroupProxyById(Long idGroupContact) {
    String request = "SELECT DISTINCT c FROM ContactGroup c LEFT JOIN FETCH c.contacts WHERE c.idContactGroup= :idContactGroup";
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
    ContactGroup cgById = this.getContactGroupProxyById(idContactGroup);
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
    ContactGroup cgById = this.getContactGroupProxyById(idContactGroup);

    tx.begin();

    cgById.getContacts().remove(contactByEmail);
    contactByEmail.getContactGroups().remove(cgById);
    
    entityManager.merge(cgById);
    entityManager.merge(contactByEmail);
    entityManager.flush();
    
    tx.commit();
    entityManager.close();

    return cgById;
  }
}
