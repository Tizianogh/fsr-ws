package com.lip6.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.stereotype.Service;

public class JpaUtil {

	private static EntityManagerFactory emf = null;

	public static EntityManagerFactory getEmf() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("projetJPA");
		}
		return emf;
	}

	//TODO: destroy bean
	public static void close() {
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}
}
