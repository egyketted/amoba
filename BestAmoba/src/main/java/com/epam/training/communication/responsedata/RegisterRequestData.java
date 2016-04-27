package com.epam.training.communication.responsedata;

public class RegisterRequestData {

    private String name;

    public RegisterRequestData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
