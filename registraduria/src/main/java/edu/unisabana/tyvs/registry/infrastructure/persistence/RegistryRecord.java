package edu.unisabana.tyvs.registry.infrastructure.persistence;

public class RegistryRecord {

    private final int id;
    private final String name;
    private final int age;
    private final String gender;
    private final boolean alive;

    public RegistryRecord(int id, String name, int age, String gender, boolean alive) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.alive = alive;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public boolean isAlive() {
        return alive;
    }
}