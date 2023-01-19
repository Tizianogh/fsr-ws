package com.lip6.api.contact;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.services.interfaces.ContactService;


@WebServlet(name = "contact/delete", urlPatterns = {"/contact/delete"})
public class DeleteContactResource extends HttpServlet {
  private ApplicationContext context;

  private ContactService contactService;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    this.contactService = (ContactService) context.getBean("contactImpService");
  }

  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");

    PrintWriter out = response.getWriter();

    ObjectMapper objectMapper = new ObjectMapper();

    Builder responseBuilder = null;

    String idContact = request.getParameter("idContact");

    if (idContact == null || idContact.isEmpty()) {
      responseBuilder = new Response.Builder().with($ -> {
        $.message = String.format(
            "[%s] - Error - Le ou les argument(s) est/sont peut-être manquant(s).", new Date());
        $.timeStamp = new Date();
        $.status = BAD_REQUEST;
        $.statusCode = BAD_REQUEST.value();
      });

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      long idContactModified = Long.parseLong(idContact);
      boolean deletedContact = this.contactService.deleteContact(idContactModified);
      if (!deletedContact) {
        responseBuilder = new Response.Builder().with($ -> {
          $.message = String.format(
              "[%s] - Error -  Contact non supprimé", new Date());
          $.timeStamp = new Date();
          $.status = NOT_FOUND;
          $.statusCode = NOT_FOUND.value();
        });

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      } else {
        responseBuilder = new Response.Builder().with($ -> {
          $.message = String.format(
              "[%s] - Succesfull - Contact supprimé.",
              new Date());
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
