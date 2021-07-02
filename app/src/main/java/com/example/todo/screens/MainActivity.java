package com.example.todo.screens;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Add_Edit_Task;
import com.example.todo.DialogCloseListener;
import com.example.todo.JsonPlaceHolderAPI;
import com.example.todo.MyApplication;
import com.example.todo.R;

import com.example.todo.ToDo;
import com.example.todo.adapter.newToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DialogCloseListener, newToDoAdapter.OnNoteListener {
//    public class MainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private newToDoAdapter tasksAdapter;

    public static List<ToDoModel> getTaskList() {
        return taskList;
    }

    public static void setTaskList(List<ToDoModel> taskList) {
        MainActivity.taskList = taskList;
    }

    public static List<ToDoModel> taskList;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;
    private FloatingActionButton addFAB;
    private EditText searchText;
    private ImageView searchImage,back;
    private static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI=retrofit.create(JsonPlaceHolderAPI.class);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addFAB=findViewById(R.id.addFAB);
        searchText=findViewById(R.id.searchText);
        searchImage=findViewById(R.id.searchImage);
        back=findViewById(R.id.back);

        back.setVisibility(View.INVISIBLE);
        token=SignInActivity.getToken();
        System.out.println(token);
        getAllTasks();


        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Edit_Task.newInstance().show(getSupportFragmentManager(),Add_Edit_Task.TAG);
            }
        });
    }

    public void getAllTasks(){

        Call<List<ToDoModel>> call = jsonPlaceHolderAPI.getToDo("Token "+token);

        call.enqueue(new Callback<List<ToDoModel>>() {
            @Override
            public void onResponse(Call<List<ToDoModel>> call, Response<List<ToDoModel>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Error " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ToDoModel> toDos=response.body();
                taskList=toDos;
                tasksAdapter=new newToDoAdapter(MainActivity.this,toDos,MainActivity.this);
                recyclerView.setAdapter(tasksAdapter);
                tasksAdapter.setTask(taskList);
                Toast.makeText(MainActivity.this,"List updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ToDoModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Fail " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasksAdapter.notifyDataSetChanged();
        tasksAdapter.setTask(taskList);





        for(ToDoModel list: taskList)
            System.out.println(list.getTask());
//        ToDoModel updatedTask= new ToDoModel();
//        if(MyApplication.getEditedTask().getTask()!=null){
//            updatedTask.setId(MyApplication.getEditedTask().getId());
//            updatedTask.setTask(MyApplication.getEditedTask().getTask());
////        if(updatedTask.getTask()!=null)
//            taskList.set(MyApplication.getEditPosition(),updatedTask);
//        }
//
//        tasksAdapter.setTask(taskList);
//        tasksAdapter.setTask(taskList);
////        recyclerView.setAdapter(tasksAdapter);
////        tasksAdapter.setTask(taskList);
////        System.out.println(taskList);
        tasksAdapter.notifyDataSetChanged();
        tasksAdapter.setTask(taskList);


    }

    @Override
    public void onEditClick(int position) {

        tasksAdapter.editTask(position);
        tasksAdapter.notifyDataSetChanged();
        System.out.println("CLICKED" + position);
        MyApplication.setEditPosition(position);
    }
    @Override
    public void onDeleteClick(int position) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(tasksAdapter.getContext());
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this Task?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        tasksAdapter.deleteItem(position);
                        ToDoModel item = taskList.get(position);
                        Call<ToDo> call = jsonPlaceHolderAPI.deleteToDo("Token "+ token, item.getId());
                        call.enqueue(new Callback<ToDo>() {
                            @Override
                            public void onResponse(Call<ToDo> call, Response<ToDo> response) {
                                if(!response.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Error " + response.code(),Toast.LENGTH_SHORT).show();
                                    return;

                                }

                                System.out.println("Code: " +response.code());
                                taskList.remove(position);
                                tasksAdapter.notifyItemChanged(position);
                                tasksAdapter.setTask(taskList);
                            }

                            @Override
                            public void onFailure(Call<ToDo> call, Throwable t) {
                                Toast.makeText(MainActivity.this,"Fail " + t.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tasksAdapter.notifyItemChanged(position);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
//        searchImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                back.setVisibility(View.VISIBLE);
//                String search= searchText.getText().toString().trim();
//                if(!search.equals("")){
//                    taskList=db.getSearchList(search);
//                }
//                else
//                    taskList=db.getAllTasks();
//
//                tasksAdapter.setTask(taskList);
//                tasksAdapter.notifyDataSetChanged();
//            }
//        });
//
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                taskList=db.getAllTasks();
//                tasksAdapter.setTask(taskList);
//                tasksAdapter.notifyDataSetChanged();
//                back.setVisibility(View.INVISIBLE);
//            }
//        });


//