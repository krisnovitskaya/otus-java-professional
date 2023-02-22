package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

@Table(name = "phone")
public record Phone(@Id Long id, @NonNull String number, @NonNull Long clientId) {
}
