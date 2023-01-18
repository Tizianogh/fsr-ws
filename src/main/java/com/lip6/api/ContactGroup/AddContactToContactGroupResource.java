package com.lip6.api.ContactGroup;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.ContactGroup;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.services.interfaces.ContactGroupService;

@WebServlet("/cg/add")
public class AddContactToContactGroupResource extends HttpServlet {
  private ApplicationContext context;

  private ContactGroupService contactGroupService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    this.contactGroupService = (ContactGroupService) context.getBean("contactGroupImpService");
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");
    PrintWriter out = response.getWriter();

    ObjectMapper objectMapper = new ObjectMapper();

    Builder responseBuilder = null;

    String idGroup = request.getParameter("idGroup");

    if (idGroup == null || idGroup.isEmpty()) {

      responseBuilder = new Response.Builder().with($ -> {
        $.message = String.format(
            "[%s] - Error - Le ou les argument(s) est/sont peut-être manquant(s).", new Date());
        $.timeStamp = new Date();
        $.status = BAD_REQUEST;
        $.statusCode = BAD_REQUEST.value();
      });

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else {
      Long idGroupModifiedToLong = Long.parseLong(idGroup);
      Contact createdContact = objectMapper.readValue(request.getReader(), Contact.class);
      Optional<ContactGroup> retrievedContactGroup =
          this.contactGroupService.addContactToContactGroup(createdContact,
              idGroupModifiedToLong);
      if (!retrievedContactGroup.isPresent()) {
        responseBuilder = new Response.Builder().with($ -> {
          $.message = String.format(
              "[%s] - Error - Opération échouée", new Date());
          $.timeStamp = new Date();
          $.status = BAD_REQUEST;
          $.statusCode = BAD_REQUEST.value();
        });
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        responseBuilder = new Response.Builder().with($ -> {
          $.message = String.format(
              "[%s] - Completed - Le contact a bien été ajouté au groupe.", new Date());
          $.timeStamp = new Date();
          $.data = new AbstractMap.SimpleEntry("result", retrievedContactGroup.get());
          $.status = HttpStatus.OK;
          $.statusCode = HttpStatus.OK.value();
        });
        response.setStatus(HttpServletResponse.SC_OK);
      }

      String jsonString = objectMapper.writeValueAsString(responseBuilder);

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      out.print(jsonString);
      out.flush();
    }

  }

}
