package com.lip6.api.Contact;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/contacts")
public class GetAllContactsResource extends HttpServlet {
    @Autowired
    @Qualifier("contactService")
    private ContactService contactService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        List<Contact> contacts = this.contactService.getAllContacts();

        PrintWriter out = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();

        Builder responseBuilder = null;

        if (contacts.isEmpty()) {
            responseBuilder = new Response.Builder().with($ -> {
                $.message = String.format("[%s] - Error - Aucun contact en base de données", new Date());
                $.timeStamp = new Date();
                $.status = NOT_FOUND;
                $.statusCode = NOT_FOUND.value();
            });
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            responseBuilder = new Response.Builder().with($ -> {
                $.data = new AbstractMap.SimpleEntry("results", contacts);
                $.message = String.format("[%s] - Completed - %s contact(s) ont été trouvé(s)", new Date(), contacts.size());
                $.timeStamp = new Date();
                $.status = OK;
                $.statusCode = OK.value();
            });
            response.setStatus(HttpServletResponse.SC_OK);
        }

        String jsonString = objectMapper.writeValueAsString(responseBuilder);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(jsonString);
        out.flush();
        out.close();

    }

}
