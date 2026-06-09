package edu.unisabana.tyvs.registry.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Gender;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;

class RegistryWithMockTest {

    private RegistryRepositoryPort repo;
    private Registry registry;

    @BeforeEach
    void setUp() {
        repo = mock(RegistryRepositoryPort.class);
        registry = new Registry(repo);
    }

    @Test
    void shouldReturnDuplicatedWhenPersonAlreadyExists() throws Exception {
        Person person = new Person("Laura", 200, 35, Gender.FEMALE, true);

        when(repo.existsById(200)).thenReturn(true);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.DUPLICATED, result);
        verify(repo).existsById(200);
        verify(repo, never()).save(person);
    }

    @Test
    void shouldSaveWhenPersonIsValidAndDoesNotExist() throws Exception {
        Person person = new Person("Mario", 201, 45, Gender.MALE, true);

        when(repo.existsById(201)).thenReturn(false);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.VALID, result);
        verify(repo).existsById(201);
        verify(repo).save(person);
    }

    @Test
    void shouldNotCallRepositoryWhenPersonIsUnderage() throws Exception {
        Person person = new Person("Juan", 202, 16, Gender.MALE, true);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.UNDERAGE, result);
        verifyNoInteractions(repo);
    }

    @Test
    void shouldNotCallRepositoryWhenPersonIsDead() throws Exception {
        Person person = new Person("Sofia", 203, 70, Gender.FEMALE, false);

        RegisterResult result = registry.registerVoter(person);

        assertEquals(RegisterResult.DEAD, result);
        verifyNoInteractions(repo);
    }

    @Test
    void shouldPropagateExceptionWhenRepositoryFails() throws Exception {
        // Arrange
        Person person = new Person("Error SQL", 500, 35, Gender.MALE, true);

        when(repo.existsById(500)).thenThrow(new RuntimeException("Error simulado de base de datos"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> registry.registerVoter(person)
        );

        assertEquals("Error simulado de base de datos", exception.getMessage());
        verify(repo).existsById(500);
        verify(repo, never()).save(person);
    }
}