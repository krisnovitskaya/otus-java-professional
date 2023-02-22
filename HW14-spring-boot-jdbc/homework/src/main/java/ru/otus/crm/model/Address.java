package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table(name = "address")
public record Address(@Id Long id, @NonNull String street, @NonNull Long clientId) {
}
