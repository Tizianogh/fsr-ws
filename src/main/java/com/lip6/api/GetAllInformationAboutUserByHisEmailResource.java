package com.lip6.api;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.lip6.domain.model.Contact;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/contact")
public class GetAllInformationAboutUserByHisEmailResource extends HttpServlet {

	@Autowired
	@Qualifier("contactService")
	private ContactService contactService;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String email = request.getParameter("email");
		if (email == null || email.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter()
					.append("[" + new Date() + "]" + "Le ou les argument(s) est/sont peut-être manquant(s).");
		} else {
			List<Contact> allInformationsAboutContactByHisEmail = contactService
					.retrieveAllInformationsAboutContactByHisEmail(email);
			if (allInformationsAboutContactByHisEmail.isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.getWriter()
						.append("[" + new Date() + "]" + "Aucun contact en base de données avec cet email : " + email);
			} else {
				response.setStatus(HttpServletResponse.SC_FOUND);
				response.getWriter()
						.append("[" + new Date() + "]" + "Informations à propos de l'utilisateur possédant cet email : " + email);
			}
		}
	}

}
