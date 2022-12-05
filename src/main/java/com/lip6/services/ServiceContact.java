package com.lip6.services;

import com.lip6.daos.DAOContact;
import com.lip6.daos.IDAOContact;

public class ServiceContact {
	private final IDAOContact daoc=new DAOContact();

	public void createContact(String fname, String lname, String email) {
		if (fname !="" && lname !="" && email!="") {
			daoc.addContact(fname, lname, email);
			System.out.println("Contact ajoute!");
		}

		
			System.out.println("Contact non ajoute!");
	}
	
	public void createContact() {

			daoc.addContact();
	}
	
	public void deleteContact(long id) {
		if(daoc.deleteContact(id)) {
			System.out.println("Contact supprimé");
		}
		
		System.out.println("Contact supprimé");
	}

}
