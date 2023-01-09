package com.lip6.dao.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
import com.lip6.domain.model.Messages;
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
    public boolean modifyContact(long id, String firstname, String lastname, String email) {
        boolean success = false;
        Connection con = null;
        try {
            Class.forName(Messages.getString("driver"));
            con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
                    Messages.getString("password"));
            Statement stmt = con.createStatement();
            String sqlFirstName = "UPDATE contact SET firstname = " + "'" + firstname + "'" + " WHERE id = " + id;
            String sqlLastName = "UPDATE contact SET lastname = " + "'" + lastname + "'" + " WHERE id = " + id;
            String sqlEmail = "UPDATE contact SET email = " + "'" + email + "'" + " WHERE id = " + id;

            if (firstname != "")
                stmt.executeUpdate(sqlFirstName);
            if (lastname != "")
                stmt.executeUpdate(sqlLastName);
            if (email != "")
                stmt.executeUpdate(sqlEmail);

            success = true;
            stmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public ArrayList<Contact> getContactByFirstName(String firstname) {

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        ResultSet rec = null;
        Connection con = null;
        try {
            Class.forName(Messages.getString("driver"));
            con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
                    Messages.getString("password"));
            Statement stmt = con.createStatement();
            rec = stmt.executeQuery("SELECT * FROM contacts WHERE firstname = " + "'" + firstname + "'");

            while (rec.next()) {
                Contact contact = new Contact();
                contact.setIdContact(Long.parseLong(rec.getString("id")));
                contact.setFirstName(rec.getString("firstname"));
                contact.setLastName(rec.getString("lastname"));
                contact.setEmail(rec.getString("email"));

                contacts.add(contact);
            }

            stmt.close();
            rec.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public ArrayList<Contact> getContactByLastName(String lastname) {

        ArrayList<Contact> contacts = new ArrayList<Contact>();

        ResultSet rec = null;
        Connection con = null;
        try {
            Class.forName(Messages.getString("driver"));
            con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
                    Messages.getString("password"));
            Statement stmt = con.createStatement();
            rec = stmt.executeQuery("SELECT * FROM contacts WHERE lastname = " + "'" + lastname + "'");

            while (rec.next()) {
                Contact contact = new Contact();
                contact.setIdContact(Long.parseLong(rec.getString("id")));
                contact.setFirstName(rec.getString("firstname"));
                contact.setLastName(rec.getString("lastname"));
                contact.setEmail(rec.getString("email"));
                contacts.add(contact);
            }

            stmt.close();
            rec.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
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
}
