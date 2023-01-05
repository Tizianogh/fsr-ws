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
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.lip6.domain.model.*;
import com.lip6.dao.interfaces.*;

import com.lip6.util.JpaUtil;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

@Service
@Qualifier("daoContact")
public class DAOContact implements IDAOContact {

	@Autowired
	private EntityManagerFactory emf;

	@Override
	public List<Contact> getAllInformationsAboutContactByHisEmail(String lastname) {
		String request = "SELECT c.idContact, c.firstName, c.lastName, c.email, c.PhoneNum.idPhoneNum, c.PhoneNum.num, c.ContactGroup.idContact, c.ContactGroup.libelle FROM Contact c WHERE c.email=:mail";
		EntityManager entityManager = this.emf.createEntityManager();

		TypedQuery<Contact> fullInformationOfContactByEmailQuery = entityManager.createQuery(request, Contact.class);

		List<Contact> getFullInformationOfContactByEmail = fullInformationOfContactByEmailQuery.getResultList();

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
		EntityManager entityManager = JpaUtil.getEmf().createEntityManager();

		Contact contact = entityManager.find(Contact.class, id);

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
	public Contact getContact(long id) {
		ResultSet rec = null;
		Contact contact = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contact WHERE id = " + id);

			if (rec.next() == false) {
				System.out.println("ResultSet in empty in Java");
			} else {
				do {
					contact = new Contact();
					contact.setIdContact(Long.parseLong(rec.getString("id")));
					contact.setFirstName(rec.getString("firstname"));
					contact.setLastName(rec.getString("lastname"));
					contact.setEmail(rec.getString("email"));
				} while (rec.next());
			}

			stmt.close();
			rec.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
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
}
