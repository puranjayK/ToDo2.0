package com.example.todo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.newToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.example.todo.screens.MainActivity;
import com.example.todo.screens.SignInActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Add_Edit_Task extends BottomSheetDialogFragment {
    public static final String TAG = "BottomActionDialog";
    private ProgressBar pb;
    private EditText taskText;
    private Button saveButton;
    private RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;
    private newToDoAdapter tasksAdapter;
    public static List<ToDoModel> taskList = MainActivity.getTaskList();

    public static Add_Edit_Task newInstance() {
        return new Add_Edit_Task();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskText = getView().findViewById(R.id.newTaskText);
        saveButton = getView().findViewById(R.id.newTaskButton);
        saveButton.setEnabled(false);
        pb = getView().findViewById(R.id.pb);
        pb.setVisibility(View.INVISIBLE);
        sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
//        recyclerView.setAdapter(tasksAdapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);


        boolean isEdit = false;
        final Bundle bundle = getArguments();

        if (bundle != null) {
            isEdit = true;
            String task = bundle.getString("task");
            taskText.setText(task);
            if (task.length() > 0) {
                saveButton.setEnabled(true);

            }
        }
        taskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                } else {
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(Color.parseColor("#70F1AE"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final boolean finalIsEdit = isEdit;
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = taskText.getText().toString();

                if (finalIsEdit) {
                    Edit(bundle, text);
                } else {
                    Add();

                }


            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener) activity).handleDialogClose(dialog);
        }
    }

    public void Add() {
        String text = taskText.getText().toString();
        ToDo todo = new ToDo(text);
        pb.setVisibility(View.VISIBLE);
        Call<Void> call = jsonPlaceHolderAPI.createToDo("Token " + sharedPreferences.getString("token", null), todo);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error in adding task", Toast.LENGTH_SHORT).show();
                    dismiss();
                    return;
                }

                pb.setVisibility(View.INVISIBLE);
                dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());

            }
        });


    }

    public void Edit(Bundle bundle, String editedTask) {
        ToDo edited = new ToDo(editedTask);

        System.out.println("ID: " + bundle.getInt("id"));
        pb.setVisibility(View.VISIBLE);
        Call<ToDoModel> call = jsonPlaceHolderAPI.updateToDo("Token " + sharedPreferences.getString("token", null),
                bundle.getInt("id"),
                edited);
        call.enqueue(new Callback<ToDoModel>() {
            @Override
            public void onResponse(Call<ToDoModel> call, Response<ToDoModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Error in editing task", Toast.LENGTH_SHORT).show();
                    System.out.println("Error" + response.code());
                    dismiss();
                    return;
                }
                System.out.println("Response: " + response.body().getTask());
                dismiss();


            }

            @Override
            public void onFailure(Call<ToDoModel> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
