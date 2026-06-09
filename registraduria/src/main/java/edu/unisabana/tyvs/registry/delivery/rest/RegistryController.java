package edu.unisabana.tyvs.registry.delivery.rest;

import edu.unisabana.tyvs.registry.application.usecase.Registry;
import edu.unisabana.tyvs.registry.delivery.rest.dto.PersonRequest;
import edu.unisabana.tyvs.registry.domain.model.Person;
import edu.unisabana.tyvs.registry.domain.model.RegisterResult;
import edu.unisabana.tyvs.registry.infrastructure.persistence.RegistryRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registry")
public class RegistryController {

    private final Registry registry;

    public RegistryController(RegistryRepository repository) throws Exception {
        repository.initSchema();
        this.registry = new Registry(repository);
    }

    @PostMapping("/register")
    public RegisterResult register(@RequestBody PersonRequest request) throws Exception {
        Person person = new Person(
                request.getName(),
                request.getId(),
                request.getAge(),
                request.getGender(),
                request.isAlive()
        );

        return registry.registerVoter(person);
    }
}