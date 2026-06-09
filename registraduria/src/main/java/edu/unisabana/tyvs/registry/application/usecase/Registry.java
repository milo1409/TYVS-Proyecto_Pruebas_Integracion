package edu.unisabana.tyvs.registry.application.usecase;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;

public class Registry {

    private final RegistryRepositoryPort repository;

    public Registry(RegistryRepositoryPort repository) {
        this.repository = repository;
    }

    public RegisterResult registerVoter(Person person) throws Exception {
        if (person == null || person.getId() <= 0 || person.getName() == null || person.getName().trim().isEmpty()) {
            return RegisterResult.INVALID;
        }

        if (person.getAge() < 18) {
            return RegisterResult.UNDERAGE;
        }

        if (!person.isAlive()) {
            return RegisterResult.DEAD;
        }

        if (repository.existsById(person.getId())) {
            return RegisterResult.DUPLICATED;
        }

        repository.save(person);
        return RegisterResult.VALID;
    }
}