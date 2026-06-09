package edu.unisabana.tyvs.registry.application.usecase;

import java.util.HashMap;
import java.util.Map;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;

public class FakeRegistryRepository implements RegistryRepositoryPort {

    private final Map<Integer, Person> database = new HashMap<>();

    @Override
    public void initSchema() {
        // No requiere esquema porque usa memoria
    }

    @Override
    public void deleteAll() {
        database.clear();
    }

    @Override
    public boolean existsById(int id) {
        return database.containsKey(id);
    }

    @Override
    public void save(Person person) {
        database.put(person.getId(), person);
    }

    public int count() {
        return database.size();
    }
}