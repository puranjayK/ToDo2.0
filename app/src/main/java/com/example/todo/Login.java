package com.example.todo;

public class Login {
    private String username;
    private String password;
    private String token;
    private String non_field_errors;

    public String getNon_field_errors() {
        return non_field_errors;
    }

    public void setNon_field_errors(String non_field_errors) {
        this.non_field_errors = non_field_errors;
    }

    public String getToken() {
        return token;
    }



    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
