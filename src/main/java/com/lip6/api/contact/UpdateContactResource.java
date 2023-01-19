package com.lip6.api.contact;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lip6.domain.model.Contact;
import com.lip6.domain.model.Response;
import com.lip6.domain.model.Response.Builder;
import com.lip6.domain.services.interfaces.ContactService;

@WebServlet(name = "contact/update", urlPatterns = {"/contact/update"})
public class UpdateContactResource extends HttpServlet {

  private ApplicationContext context;

  private ContactService contactService;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    this.context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    this.contactService = (ContactService) context.getBean("contactImpService");
  }


  protected void doPut(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");

    PrintWriter out = response.getWriter();

    ObjectMapper objectMapper = new ObjectMapper();

    Builder responseBuilder = null;

    try {
      Contact contactModified = objectMapper.readValue(request.getReader(), Contact.class);
      if (contactModified == null) {
        responseBuilder = new Response.Builder().with($ -> {
          $.message = String.format("[%s] - Error - La modification n'est pas concluante",
              new Date());
          $.timeStamp = new Date();
          $.status = BAD_REQUEST;
          $.statusCode = BAD_REQUEST.value();
        });
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        int numberOfRowsModified = contactService.updateContact(contactModified);
        if (numberOfRowsModified < 1) {
          responseBuilder = new Response.Builder().with($ -> {
            $.message = String.format(
                "[%s] - Error - La modification n'est pas concluante",
                new Date());
            $.timeStamp = new Date();
            $.status = BAD_REQUEST;
            $.statusCode = BAD_REQUEST.value();
          });
          response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
          responseBuilder = new Response.Builder().with($ -> {
            $.message = String.format(
                "[%s] - Completed - Contact modifié.", new Date());
            $.timeStamp = new Date();
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

    } catch (JsonGenerationException e) {
      e.printStackTrace();
    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
