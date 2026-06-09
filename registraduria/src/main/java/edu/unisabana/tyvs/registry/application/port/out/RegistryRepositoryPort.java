package edu.unisabana.tyvs.registry.application.port.out;

import edu.unisabana.tyvs.registry.domain.model.Person;

public interface RegistryRepositoryPort {

    void initSchema() throws Exception;

    void deleteAll() throws Exception;

    boolean existsById(int id) throws Exception;

    void save(Person person) throws Exception;
}