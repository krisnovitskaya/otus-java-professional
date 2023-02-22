package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDto;
import ru.otus.exceptions.ResourceNotFoundException;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ClientController {

    private final DBServiceClient serviceClient;

    @GetMapping("/client/all")
    public String getAllClient(Model model){
        List<ClientDto> list = serviceClient.findAll().stream().map(ClientDto::new).toList();
        model.addAttribute("clients", list);
        return "clients";
    }

    @GetMapping("/client")
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        ClientDto client;
        if (id != null) {
            client = new ClientDto(serviceClient.getClient(id).orElseThrow(() -> new ResourceNotFoundException("Not found client with id= " + id)));
        } else {
            client = new ClientDto();
        }
        model.addAttribute("client", client);
        return "client-form";
    }

    @PostMapping("/client")
    public String saveProduct(ClientDto client) {
        serviceClient.saveClient(client.toClient());
        return "redirect:/client/all";
    }

    @GetMapping("/client/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        serviceClient.deleteById(id);
        return "redirect:/client/all";
    }
}
