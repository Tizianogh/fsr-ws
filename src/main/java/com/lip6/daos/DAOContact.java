package com.lip6.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.springframework.beans.factory.annotation.Autowired;

import com.lip6.entities.Address;
import com.lip6.entities.Contact;
import com.lip6.entities.ContactGroup;
import com.lip6.entities.Messages;
import com.lip6.entities.PhoneNum;
import com.lip6.util.JpaUtil;

public class DAOContact implements IDAOContact {

	/**
	 * Rajoute un contact dans la base de donnees.
	 * 
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @return renvoit le nouveau contact
	 */
	@Override
	public void addContact(String firstname, String lastname, String email) {
		EntityManager em = JpaUtil.getEmf().createEntityManager();

		Contact contact = new Contact();

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		contact.setFirstName(firstname);
		contact.setLastName(lastname);
		contact.setEmail(email);
		em.persist(contact);

		tx.commit();

		em.close();

	}
	
	@Autowired
	@Override
	public void addContactFromBean(Contact contact) {
		EntityManager em = JpaUtil.getEmf().createEntityManager();
		Contact c = contact;

		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(contact);
		tx.commit();

		em.close();
	}

	/**
	 * Suppresion d'un contact a partir de son identifiant
	 * 
	 * @param id
	 * @return vrai si la suppression a bien ete effectuee
	 */
	@Override
	public boolean deleteContact(long id) {
		EntityManager em = JpaUtil.getEmf().createEntityManager();

		Contact contact = em.find(Contact.class, id);

		if (contact != null) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			em.remove(contact);

			tx.commit();

			em.close();

			return true;
		}

		String.format("Aucun contact avec pour id %s n'a été trouvé..", id);

		return false;

	}

	/**
	 * Recuperation d'un contact a partir de son identifiant
	 * 
	 * @param id
	 * @return
	 */
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
			rec = stmt.executeQuery("SELECT * FROM contacts WHERE id = " + id);

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

	/**
	 * Methode qui modifie les coordonees d'un contact
	 * 
	 * @param id
	 * @param firstname
	 * @param alstname
	 * @param email
	 * @return
	 */
	@Override
	public boolean modifyContact(long id, String firstname, String lastname, String email) {
		boolean success = false;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			String sqlFirstName = "UPDATE contacts SET firstname = " + "'" + firstname + "'" + " WHERE id = " + id;
			String sqlLastName = "UPDATE contacts SET lastname = " + "'" + lastname + "'" + " WHERE id = " + id;
			String sqlEmail = "UPDATE contacts SET email = " + "'" + email + "'" + " WHERE id = " + id;

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

	/**
	 * Renvoit la liste des contacts correspondant au prenom firrstname
	 * 
	 * @param firstname
	 * @return
	 */
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

	/**
	 * Renvoit la liste des contacts correspondant au nom lastname
	 * 
	 * @param lastname
	 * @return
	 */
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

	/**
	 * Renvoit la liste des contacts correspondant a l'email email
	 * 
	 * @param email
	 * @return
	 */
	@Override
	public ArrayList<Contact> getContactByEmail(String email) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();

		ResultSet rec = null;
		Connection con = null;
		try {
			Class.forName(Messages.getString("driver"));
			con = DriverManager.getConnection(Messages.getString("database"), Messages.getString("username"),
					Messages.getString("password"));
			Statement stmt = con.createStatement();
			rec = stmt.executeQuery("SELECT * FROM contacts WHERE email = " + "'" + email + "'");

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
	public void addContact() {
	EntityManager em = JpaUtil.getEmf().createEntityManager();
		
	Contact contact = new Contact("Noir","Xavier", "Xavier.Blanc@gmail.com");
	Address address=new Address("15 rue de la Paix", "Paris", "75009");
	ContactGroup cg = new ContactGroup("Miagiste");
	PhoneNum phone1=new PhoneNum("0625086509");
	PhoneNum phone2=new PhoneNum("0625086510");
	phone1.setContact(contact);
	phone2.setContact(contact);
	contact.getPhones().add(phone1);
	contact.getPhones().add(phone2);
	contact.setAddress(address);
	
	address.setContact(contact);
	
	cg.getContacts().add(contact);
	contact.getContactGroups().add(cg);
	
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
	
		em.persist(contact);
		
		tx.commit();
		
		em.close();		
	}

}
