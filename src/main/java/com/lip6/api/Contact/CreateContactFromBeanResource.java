package com.lip6.api.Contact;

import static org.springframework.http.HttpStatus.CREATED;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
import java.util.Arrays;
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
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.services.interfaces.ContactGroupService;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/contact/bean/new")
public class CreateContactFromBeanResource extends HttpServlet {
    @Autowired
    @Qualifier("contactService")
    private ContactService contactService;

    @Autowired
    @Qualifier("contactGroupService")
    private ContactGroupService contactGroupService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        /*
         * Contact firstContactFromBean = (Contact) context.getBean("firstContactFromBean");
         * Contact secondContactFromBean = (Contact) context.getBean("secondContactFromBean");
         * 
         * Address addressForBeanNumberOne = (Address) context.getBean("adressForContactNumberOne");
         * Address addressForBeanNumberTwo = (Address) context.getBean("adressForContactNumberTwo");
         * 
         * Set<PhoneNum> setOfNumsForBeanOne = Stream
         * .of((PhoneNum) context.getBean("numNumberOne"), (PhoneNum) context.getBean("numNumberTwo"))
         * .collect(Collectors.toSet());
         * Set<PhoneNum> setOfNumsForBeanTwo = Stream
         * .of((PhoneNum) context.getBean("numNumberThree"), (PhoneNum) context.getBean("numNumberFour"))
         * .collect(Collectors.toSet());
         * 
         * firstContactFromBean.setAddress(addressForBeanNumberOne);
         * addressForBeanNumberOne.setContact(firstContactFromBean);
         * 
         * secondContactFromBean.setAddress(addressForBeanNumberTwo);
         * addressForBeanNumberTwo.setContact(secondContactFromBean);
         * 
         * firstContactFromBean.setPhones(setOfNumsForBeanOne);
         * setOfNumsForBeanOne.forEach($ -> $.setContact(firstContactFromBean));
         * 
         * secondContactFromBean.setPhones(setOfNumsForBeanTwo);
         * setOfNumsForBeanTwo.forEach($ -> $.setContact(secondContactFromBean));
         * 
         * ContactGroup firstContactGroup = (ContactGroup) context.getBean("firstGroupContactFromBean");
         * 
         * firstContactGroup.getContacts().add(firstContactFromBean);
         * firstContactFromBean.getContactGroups().add(firstContactGroup);
         * 
         * firstContactGroup.getContacts().add(secondContactFromBean);
         * secondContactFromBean.getContactGroups().add(firstContactGroup);
         */
        Contact firstContactFromBean = (Contact) context.getBean("firstContactFromBean");
        Contact secondContactFromBean = (Contact) context.getBean("secondContactFromBean");
        ContactGroup firstContactGroup = (ContactGroup) context.getBean("firstGroupContactFromBean");

        contactGroupService.createContactGroup(firstContactGroup);

        List<Contact> contacts = Arrays.asList(firstContactFromBean, secondContactFromBean);

        PrintWriter out = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();

        Builder responseBuilder = new Response.Builder().with($ -> {
            $.message = String.format(
                    "[%s] - Completed - La création des contacts depuis les beans a été réalisée.", new Date());
            $.timeStamp = new Date();
            $.data = new AbstractMap.SimpleEntry("results", contacts);
            $.status = CREATED;
            $.statusCode = CREATED.value();
        });

        response.setStatus(HttpServletResponse.SC_CREATED);

        String jsonString = objectMapper.writeValueAsString(responseBuilder);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(jsonString);
        out.flush();
        out.close();
    }
}