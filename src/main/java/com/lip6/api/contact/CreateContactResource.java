package com.lip6.api.contact;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.AbstractMap;
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
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        ObjectMapper objectMapper = new ObjectMapper();

        Builder responseBuilder = null;

        try {
            Contact createdContact = objectMapper.readValue(request.getReader(), Contact.class);
            if (createdContact == null) {
                responseBuilder = new Response.Builder().with($ -> {
                    $.message = String.format("[%s] - Error - La création du contact n'est pas concluante.",
                            new Date());
                    $.timeStamp = new Date();
                    $.status = BAD_REQUEST;
                    $.statusCode = BAD_REQUEST.value();
                });
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                Optional<Contact> retrievedContact = contactService.createContact(createdContact);
                if (!retrievedContact.isPresent()) {
                    responseBuilder = new Response.Builder().with($ -> {
                        $.message = String.format(
                                "[%s] - Error - L'email de l'utilisateur existe déjà en base de données.", new Date());
                        $.timeStamp = new Date();
                        $.status = BAD_REQUEST;
                        $.statusCode = BAD_REQUEST.value();
                    });
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    responseBuilder = new Response.Builder().with($ -> {
                        $.message = String.format(
                                "[%s] - Completed - Le contact a été ajouté en base de données.", new Date());
                        $.timeStamp = new Date();
                        $.data = new AbstractMap.SimpleEntry("result", retrievedContact.get());
                        $.status = CREATED;
                        $.statusCode = CREATED.value();
                    });
                    response.setStatus(HttpServletResponse.SC_CREATED);
                }

                String jsonString = objectMapper.writeValueAsString(responseBuilder);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                out.print(jsonString);
                out.flush();
            }

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
