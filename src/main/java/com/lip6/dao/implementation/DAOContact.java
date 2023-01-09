package com.lip6.dao.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lip6.dao.interfaces.IDAOContact;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.DTO.ContactDTO;

@Service
@Qualifier("daoContact")
public class DAOContact implements IDAOContact {

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public List<Contact> getAllInformationsAboutContactByHisEmail(String email) {
        String request = "SELECT DISTINCT c FROM Contact c LEFT JOIN FETCH c.phones WHERE c.email=:mail";
        String request2 = "SELECT DISTINCT c FROM Contact c LEFT JOIN FETCH c.contactGroups WHERE c in :getFullInformationOfContactByEmail";

        EntityManager entityManager = this.emf.createEntityManager();

        List<Contact> getFullInformationOfContactByEmail = entityManager.createQuery(request, Contact.class)
                .setParameter("mail", email).setHint(QueryHints.PASS_DISTINCT_THROUGH, false).getResultList();

        getFullInformationOfContactByEmail = entityManager.createQuery(request2, Contact.class)
                .setParameter("getFullInformationOfContactByEmail", getFullInformationOfContactByEmail)
                .setHint(QueryHints.PASS_DISTINCT_THROUGH, false).getResultList();
        entityManager.close();
        return getFullInformationOfContactByEmail;
    }

    @Override
    public int getNumberOfContactByEmail(String email) {
        EntityManager entityManager = this.emf.createEntityManager();

        TypedQuery<Long> numberOfContactByEmailQuery = entityManager.createNamedQuery("Contact.CountContactByEmail",
                Long.class);
        numberOfContactByEmailQuery.setParameter("mail", email);

        int numberOfContactByEmail = numberOfContactByEmailQuery.getSingleResult().intValue();

        entityManager.close();

        return numberOfContactByEmail;
    }

    @Override
    public Contact addContact(Contact contact) {
        EntityManager entityManager = this.emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        entityManager.persist(contact);

        tx.commit();

        entityManager.close();
        return contact;
    }

    @Override
    public boolean deleteContact(long id) {
        EntityManager entityManager = this.emf.createEntityManager();

        ContactDTO contact = this.getContact(id);

        if (contact != null) {
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();

            entityManager.remove(contact);

            tx.commit();

            entityManager.close();

            return true;
        }

        String.format("Aucun contact avec pour id %s n'a été trouvé..", id);

        return false;

    }

    @Override
    public ContactDTO getContact(long id) {
        EntityManager entityManager = this.emf.createEntityManager();

        String getDTOContactByIdRequest = "SELECT NEW com.lip6.domain.model.DTO.ContactDTO(c.idContact, c.email, c.lastName, c.firstName) from Contact c where c.idContact= :id";

        ContactDTO contact = entityManager.createQuery(getDTOContactByIdRequest, ContactDTO.class)
                .setParameter("id", id).getSingleResult();

        entityManager.close();
        return contact;
    }

    @Override
    public List<Contact> getAllContacts() {
        EntityManager entityManager = this.emf.createEntityManager();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = builder.createQuery(Contact.class);
        Root<Contact> variableRoot = query.from(Contact.class);
        query.select(variableRoot);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public int updateContact(Contact contact) {
        EntityManager entityManager = this.emf.createEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        String request = "UPDATE Contact c SET c.firstName=:firstName, c.lastName=:lastName, c.email= :email WHERE c.idContact=:idContact";
        Query query = entityManager.createQuery(request).setParameter("firstName", contact.getFirstName())
                .setParameter("lastName", contact.getLastName()).setParameter("email", contact.getEmail())
                .setParameter("idContact", contact.getIdContact());

        int numberOfRowsUpdated = query.executeUpdate();
        tx.commit();
        entityManager.close();
        return numberOfRowsUpdated;
    }
}
