package edu.unisabana.tyvs.registry.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;

class RegistryWithFakeRepositoryTest {

    @Test
    void shouldRegisterPersonUsingFakeRepository() throws Exception {
        // Arrange
        FakeRegistryRepository fakeRepository = new FakeRegistryRepository();
        Registry registry = new Registry(fakeRepository);

        Person person = new Person("Persona Fake", 700, 28, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
        assertEquals(1, fakeRepository.count());
    }

    @Test
    void shouldRejectDuplicatedPersonUsingFakeRepository() throws Exception {
        // Arrange
        FakeRegistryRepository fakeRepository = new FakeRegistryRepository();
        Registry registry = new Registry(fakeRepository);

        Person person1 = new Person("Persona Uno", 701, 30, Gender.MALE, true);
        Person person2 = new Person("Persona Dos", 701, 40, Gender.FEMALE, true);

        // Act
        RegisterResult result1 = registry.registerVoter(person1);
        RegisterResult result2 = registry.registerVoter(person2);

        // Assert
        assertEquals(RegisterResult.VALID, result1);
        assertEquals(RegisterResult.DUPLICATED, result2);
        assertEquals(1, fakeRepository.count());
    }
}