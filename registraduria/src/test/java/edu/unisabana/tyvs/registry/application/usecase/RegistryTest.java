package edu.unisabana.tyvs.registry.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;
import edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRepository;

class RegistryTest {

    private RegistryRepositoryPort repo;
    private Registry registry;

    @BeforeEach
    void setup() throws Exception {
        String jdbc = "jdbc:h2:mem:regdbtest;DB_CLOSE_DELAY=-1";
        repo = new RegistryRepository(jdbc);
        repo.initSchema();
        repo.deleteAll();

        registry = new Registry(repo);
    }

    @Test
    void shouldRegisterValidPerson() throws Exception {
        Person person = new Person("Ana", 100, 30, Gender.FEMALE, true);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.VALID, result);
        assertTrue(repo.existsById(100));
    }

    @Test
    void shouldRejectDuplicatedPerson() throws Exception {
        Person person1 = new Person("Ana", 100, 30, Gender.FEMALE, true);
        Person person2 = new Person("Ana Dos", 100, 40, Gender.FEMALE, true);

        RegisterResult result1 = registry.registerVoter(person1);
        RegisterResult result2 = registry.registerVoter(person2);

        assertEquals(RegisterResult.VALID, result1);
        assertEquals(RegisterResult.DUPLICATED, result2);
        assertTrue(repo.existsById(100));
    }

    @Test
    void shouldRejectUnderagePerson() throws Exception {
        Person person = new Person("Carlos", 101, 15, Gender.MALE, true);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.UNDERAGE, result);
        assertFalse(repo.existsById(101));
    }

    @Test
    void shouldRejectDeadPerson() throws Exception {
        Person person = new Person("Pedro", 102, 50, Gender.MALE, false);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.DEAD, result);
        assertFalse(repo.existsById(102));
    }

    @Test
    void shouldRejectInvalidPerson() throws Exception {
        Person person = new Person("", -1, 25, Gender.OTHER, true);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.INVALID, result);
        assertFalse(repo.existsById(-1));
    }

    @Test
    void shouldThrowExceptionWhenH2ConnectionFails() {
        // Arrange
        String invalidJdbc = "jdbc:h2:tcp://localhost/~/base_inexistente";
        RegistryRepositoryPort brokenRepo = new RegistryRepository(invalidJdbc);
        Registry brokenRegistry = new Registry(brokenRepo);

        Person person = new Person("Error Conexion", 999, 30, Gender.MALE, true);

        // Act & Assert
        assertThrows(Exception.class, () -> brokenRegistry.registerVoter(person));
    }
}