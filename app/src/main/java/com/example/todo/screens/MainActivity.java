package com.example.todo.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
//import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Add_Edit_Task;
import com.example.todo.DialogCloseListener;
import com.example.todo.JsonPlaceHolderAPI;

import com.example.todo.Profile;
import com.example.todo.R;

import com.example.todo.ToDo;
import com.example.todo.adapter.newToDoAdapter;
import com.example.todo.model.ToDoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DialogCloseListener, newToDoAdapter.OnNoteListener {

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
    SharedPreferences sharedPreferences;
    private static String token;
//    private
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.actionBarColor));
        actionBar.setBackgroundDrawable(colorDrawable);


        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI=retrofit.create(JsonPlaceHolderAPI.class);

        recyclerView =findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addFAB=findViewById(R.id.addFAB);

        pb=findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);

        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        token=sharedPreferences.getString("token",null);

        getAllTasks();
        getProfile();

        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Edit_Task.newInstance().show(getSupportFragmentManager(),Add_Edit_Task.TAG);
            }
        });



        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        Menu optionsMenu=menu;
        MenuItem searchItem=optionsMenu.findItem(R.id.search_icon);
        MenuItem profileItem=optionsMenu.findItem(R.id.profile_icon);
        profileItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Intent i= new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(i);
                finish();

                return false;
            }
        });
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                SearchView searchView= (SearchView) searchItem.getActionView();
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(!newText.equals("")){
                    List<ToDoModel> searchList=new ArrayList<>();
                    for(ToDoModel task:taskList){
                        if(task.getTask().contains(newText)){

                            searchList.add(task);

                        }
                    }
                    tasksAdapter.setTask(searchList);
                    tasksAdapter.notifyDataSetChanged();
                }
                else
                    getAllTasks();
                return false;
                    }
                });
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    public void getAllTasks(){

        pb.setVisibility(View.VISIBLE);
        Call<List<ToDoModel>>   call = jsonPlaceHolderAPI.getToDo("Token "+token);

        call.enqueue(new Callback<List<ToDoModel>>() {

            @Override
            public void onResponse(Call<List<ToDoModel>> call, Response<List<ToDoModel>> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Error " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                taskList=response.body();

                tasksAdapter=new newToDoAdapter(MainActivity.this,taskList,MainActivity.this);
                recyclerView.setAdapter(tasksAdapter);

                tasksAdapter.setTask(taskList);
                pb.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onFailure(Call<List<ToDoModel>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Fail " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        getAllTasks();
    }
    public void getProfile(){
        Call<Profile> profileCall = jsonPlaceHolderAPI.getProfile("Token " + sharedPreferences.getString("token",null));
        profileCall.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if(!response.isSuccessful()){
                    System.out.println(sharedPreferences.getString("token",null));
                    System.out.println(response.code());
                    Toast.makeText(MainActivity.this,"Error in fetching profile",Toast.LENGTH_SHORT).show();
                    return;
                }
                String name=response.body().getName();
                String username= response.body().getUsername();
                String email= response.body().getEmail();

                sharedPreferences.edit().putString("name",name).apply();
                sharedPreferences.edit().putString("username",username).apply();
                sharedPreferences.edit().putString("email",email).apply();

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }
    @Override
    public void onEditClick(int position) {

        tasksAdapter.editTask(position);
        tasksAdapter.notifyDataSetChanged();

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
