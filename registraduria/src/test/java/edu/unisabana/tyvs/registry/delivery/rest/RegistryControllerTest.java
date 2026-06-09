package edu.unisabana.tyvs.registry.delivery.rest;

import static org.hamcrest.Matchers.containsString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.unisabana.tyvs.registry.RegistryApplication;
import edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRepository;

@SpringBootTest(classes = RegistryApplication.class)
@AutoConfigureMockMvc
class RegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RegistryRepository repository;

    @BeforeEach
    void setup() throws Exception {
        repository.initSchema();
        repository.deleteAll();
    }

    @Test
    void shouldRegisterPersonThroughRestEndpoint() throws Exception {
        String request = """
                {
                    "name": "Ana",
                    "id": 300,
                    "age": 30,
                    "gender": "FEMALE",
                    "alive": true
                }
                """;

        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("VALID")));
    }

    @Test
    void shouldRejectUnderagePersonThroughRestEndpoint() throws Exception {
        String request = """
                {
                    "name": "Luis",
                    "id": 301,
                    "age": 15,
                    "gender": "MALE",
                    "alive": true
                }
                """;

        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("UNDERAGE")));
    }

    @Test
    void shouldRejectDuplicatedPersonThroughRestEndpoint() throws Exception {
        String request = """
                {
                    "name": "Camila",
                    "id": 302,
                    "age": 28,
                    "gender": "FEMALE",
                    "alive": true
                }
                """;

        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("VALID")));

        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DUPLICATED")));
    }

    @Test
    void shouldRejectDeadPersonThroughRestEndpoint() throws Exception {
        // Arrange
        String request = """
                {
                    "name": "Pedro",
                    "id": 303,
                    "age": 50,
                    "gender": "MALE",
                    "alive": false
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DEAD")));
    }

    @Test
    void shouldRejectInvalidJsonThroughRestEndpoint() throws Exception {
        // Arrange
        String invalidRequest = """
                {
                    "name": "Persona Invalida",
                    "id": "ABC",
                    "age": 30,
                    "gender": "MALE",
                    "alive": true
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInvalidWhenRequiredBusinessDataIsMissing() throws Exception {
        // Arrange
        String request = """
                {
                    "name": "",
                    "id": -1,
                    "age": 25,
                    "gender": "OTHER",
                    "alive": true
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/registry/register")
                        .contentType("application/json")
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("INVALID")));
    }
}