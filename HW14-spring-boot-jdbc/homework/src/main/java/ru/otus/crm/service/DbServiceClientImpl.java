package ru.otus.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceClientImpl implements DBServiceClient {
    private final ClientRepository repository;
    @Override
    @Transactional
    public Client saveClient(Client client) {
        return repository.save(client);
    }

    @Override
    public Optional<Client> getClient(long id) {
        return repository.findById(id);
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id){
        repository.deleteById(id);
    }
}
