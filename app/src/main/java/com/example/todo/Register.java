package com.example.todo;

public class Register {
    private String name;
    private String email;
    private String username;
    private String password;
    private String token;

    public String getToken() {
        return token;
    }



    public Register(String name, String email, String username, String password) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
