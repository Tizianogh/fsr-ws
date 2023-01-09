package com.lip6.api.Contact;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.model.DTO.ContactDTO;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet("/retrieve/contact")
public class GetContactByIdResource extends HttpServlet {

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
        ObjectMapper objectMapper = new ObjectMapper();
        String idContact = request.getParameter("idContact");

        PrintWriter out = response.getWriter();

        Builder responseBuilder = null;

        if (idContact == null || idContact.isEmpty()) {
            responseBuilder = new Response.Builder().with($ -> {
                $.message = String.format("[%s] - Error - Le paramètre id est null", new Date());
                $.timeStamp = new Date();
                $.status = BAD_REQUEST;
                $.statusCode = BAD_REQUEST.value();
            });

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            Long idContactParseToLong = Long.parseLong(idContact);
            Optional<ContactDTO> retrieveIdContact = this.contactService.getContactById(idContactParseToLong);
            if (!retrieveIdContact.isPresent()) {
                responseBuilder = new Response.Builder().with($ -> {
                    $.message = String.format("[%s] - Error -  L'utilisateur n'a été trouvé en base de données.", new Date(),
                            retrieveIdContact.get().getFirstName(),
                            retrieveIdContact.get().getLastName());
                    $.timeStamp = new Date();
                    $.status = NOT_FOUND;
                    $.statusCode = NOT_FOUND.value();
                });
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            } else {
                responseBuilder = new Response.Builder().with($ -> {
                    $.data = new AbstractMap.SimpleEntry("result", retrieveIdContact.get());
                    $.message = String.format("[%s] - Completed - L'utilisateur '%s', '%s' a été trouvé en base de données.",
                            new Date(),
                            retrieveIdContact.get().getFirstName(),
                            retrieveIdContact.get().getLastName());
                    $.timeStamp = new Date();
                    $.status = OK;
                    $.statusCode = OK.value();
                });
                response.setStatus(HttpServletResponse.SC_OK);
            }

        }

        String jsonString = objectMapper.writeValueAsString(responseBuilder);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        out.print(jsonString);
        out.flush();
        out.close();
    }

}
