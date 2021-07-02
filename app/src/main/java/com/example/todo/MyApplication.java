package com.example.todo;

import android.app.Application;

import com.example.todo.model.ToDoModel;

import java.util.List;

public class MyApplication extends Application {
    private static List<ToDoModel> taskList;
    private static ToDoModel editedTask=new ToDoModel() ;
    private static int editPosition;

    public static int getEditPosition() {
        return editPosition;
    }

    public static void setEditPosition(int editPosition) {
        MyApplication.editPosition = editPosition;
    }

    public static ToDoModel getEditedTask() {
        return editedTask;
    }

    public static void setEditedTask(ToDoModel editedTask) {
        MyApplication.editedTask = editedTask;
    }

    public static List<ToDoModel> getTaskList() {
        return taskList;
    }

    public static void setTaskList(List<ToDoModel> taskList) {
        MyApplication.taskList = taskList;
    }
}
