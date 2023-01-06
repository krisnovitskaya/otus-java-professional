package ru.otus.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class ClientDto {

    private Long id;

    private String name;

    private String address;

    private String phones;

    public ClientDto(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.address = client.getAddress().getStreet();
        this.phones = client.getPhones().stream().map(Phone::getNumber).collect(Collectors.joining(", "));
    }
}
