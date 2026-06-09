package edu.unisabana.tyvs.registry.delivery.rest.dto;

import edu.unisabana.tyvs.registry.domain.model.Gender;

public class PersonRequest {

    private String name;
    private int id;
    private int age;
    private Gender gender;
    private boolean alive;

    public PersonRequest() {
    }

    public PersonRequest(String name, int id, int age, Gender gender, boolean alive) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.alive = alive;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isAlive() {
        return alive;
    }
}