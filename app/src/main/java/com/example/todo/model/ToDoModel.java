package com.example.todo.model;

import com.google.gson.annotations.SerializedName;

public class ToDoModel {
    private int id;
    @SerializedName("title")
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

}
