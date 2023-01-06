package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dtos.ClientDto;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;


public class ClientApiServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;


    public ClientApiServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ClientDto> listClient = dbServiceClient.findAllDto();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put(TEMPLATE_ATTR_CLIENTS, listClient);
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client newClient = convertToNewClient(request.getParameter("name"), request.getParameter("address"), request.getParameter("phones"));
        dbServiceClient.saveClient(newClient);
        response.setStatus(SC_CREATED);
        response.sendRedirect("/api/client");
    }

    private static  Client convertToNewClient(String name, String address, String phones) {
        return new Client(null, name, new Address(null, address), Arrays.stream(phones.split(", ")).map(str -> new Phone(null, str)).toList());
    }
}
