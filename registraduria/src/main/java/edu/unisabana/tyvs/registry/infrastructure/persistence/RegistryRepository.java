package edu.unisabana.tyvs.registry.infrastructure.persistence;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class RegistryRepository implements RegistryRepositoryPort {

    private final String jdbcUrl;

    public RegistryRepository() {
        this.jdbcUrl = "jdbc:h2:mem:regdb;DB_CLOSE_DELAY=-1";
    }

    public RegistryRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public void initSchema() throws Exception {
        String sql = """
                CREATE TABLE IF NOT EXISTS voters (
                    id INT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    age INT NOT NULL,
                    gender VARCHAR(20) NOT NULL,
                    alive BOOLEAN NOT NULL
                )
                """;

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    @Override
    public void deleteAll() throws Exception {
        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM voters")) {
            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsById(int id) throws Exception {
        String sql = "SELECT COUNT(*) FROM voters WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    @Override
    public void save(Person person) throws Exception {
        String sql = "INSERT INTO voters(id, name, age, gender, alive) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, person.getId());
            statement.setString(2, person.getName());
            statement.setInt(3, person.getAge());
            statement.setString(4, person.getGender().name());
            statement.setBoolean(5, person.isAlive());

            statement.executeUpdate();
        }
    }
}