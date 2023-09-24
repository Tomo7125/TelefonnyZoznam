package com.example.telefonnyzoznam;

public class UserData {

    private String email;
    private static UserData instance;

    private UserData() {
        // Privátny konštruktor, aby sa nedalo vytvárať viac inštancií
    }

    public static UserData getInstance() {
        if (instance == null) {
            instance = new UserData();
        }
        return instance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
