package com.lip6.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Contact;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/contact/new")
public class CreateContactResource extends HttpServlet {

	@Autowired
	@Qualifier("contactService")
	private ContactService contactService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String email = request.getParameter("email");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");
		
		if ((email == null || email.isEmpty()) || (firstName == null || firstName.isEmpty())
				|| (lastName == null || lastName.isEmpty())) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter()
					.append("[" + new Date() + "]" + "Le ou les argument(s) est/sont peut-être manquant(s).");
		} else {
			Contact contact = new Contact(firstName, lastName, email);
			Optional<Contact> retrievedContact = contactService.createContact(contact);

			if (!retrievedContact.isPresent()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().append("[" + new Date() + "]" + " L'email de l'utilisateur : "
						+ contact.getEmail() + ", existe déjà en base de données. Vous ne pouvez pas créer un contact avec cette même adresse mail");
			} else {
				response.setStatus(HttpServletResponse.SC_CREATED);
				response.getWriter().append("[" + new Date() + "]" + "Le contact : " + contact.getFirstName() + ", "
						+ contact.getLastName() + ", " + contact.getEmail() + ", a été ajouté en base.");
			}
		}
	}
}
