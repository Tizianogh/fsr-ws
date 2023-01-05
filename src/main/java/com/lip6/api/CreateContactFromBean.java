package com.lip6.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lip6.domain.model.Address;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.PhoneNum;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/contact/bean/new")
public class CreateContactFromBean extends HttpServlet {
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
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

		Contact firstContactFromBean = (Contact) context.getBean("firstContactFromBean");
		Contact secondContactFromBean = (Contact) context.getBean("secondContactFromBean");

		Address addressForBeanNumberOne = (Address) context.getBean("adressForContactNumberOne");
		Address addressForBeanNumberTwo = (Address) context.getBean("adressForContactNumberTwo");

		Set<PhoneNum> setOfNumsForBeanOne = Stream
				.of((PhoneNum) context.getBean("numNumberOne"), (PhoneNum) context.getBean("numNumberTwo"))
				.collect(Collectors.toSet());
		Set<PhoneNum> setOfNumsForBeanTwo = Stream
				.of((PhoneNum) context.getBean("numNumberThree"), (PhoneNum) context.getBean("numNumberFour"))
				.collect(Collectors.toSet());

		firstContactFromBean.setAddress(addressForBeanNumberOne);
		addressForBeanNumberOne.setContact(firstContactFromBean);

		secondContactFromBean.setAddress(addressForBeanNumberTwo);
		addressForBeanNumberTwo.setContact(secondContactFromBean);

		firstContactFromBean.setPhones(setOfNumsForBeanOne);
		setOfNumsForBeanOne.forEach(n -> n.setContact(firstContactFromBean));

		secondContactFromBean.setPhones(setOfNumsForBeanTwo);
		setOfNumsForBeanTwo.forEach(n -> n.setContact(secondContactFromBean));

		contactService.createContact(firstContactFromBean);
		contactService.createContact(secondContactFromBean);

		response.setStatus(HttpServletResponse.SC_CREATED);
		response.getWriter()
				.append("[" + new Date() + "]" + "Le contact : " + firstContactFromBean.getFirstName() + ", "
						+ firstContactFromBean.getLastName() + ", " + firstContactFromBean.getEmail()
						+ ", a été ajouté en base.")
				.append("[" + new Date() + "]" + "Le contact : " + secondContactFromBean.getFirstName() + ", "
						+ secondContactFromBean.getLastName() + ", " + secondContactFromBean.getEmail()
						+ ", a été ajouté en base.");

	}

}
