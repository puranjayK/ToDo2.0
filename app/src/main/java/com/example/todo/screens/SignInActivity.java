package com.example.todo.screens;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.JsonPlaceHolderAPI;
import com.example.todo.Login;
import com.example.todo.Profile;
import com.example.todo.R;

import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    private Button login;
    private EditText username, password;
    private TextView register;
    SharedPreferences sharedPreferences;
    private ProgressBar progressBar;

    private static String token;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.actionBarColor));
        actionBar.setBackgroundDrawable(colorDrawable);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        Intent i = getIntent();

        login = findViewById(R.id.login);
        register = findViewById(R.id.register_login);
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password_login);
        progressBar = findViewById(R.id.progressBar2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMethod();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(i);

            }
        });

        if (sharedPreferences.getBoolean("logged", false)) {
            goToTasks();
        }
    }

    public void LoginMethod() {
        Login login = new Login(username.getText().toString(), password.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<Login> call = jsonPlaceHolderAPI.login(login);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    if (response.code() == 400) {
                        if (username.getText().toString().equals(""))
                            Toast.makeText(SignInActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
                        if (password.getText().toString().equals(""))
                            Toast.makeText(SignInActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SignInActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(SignInActivity.this, "Unable to Login", Toast.LENGTH_SHORT).show();
                    return;
                }
                token = response.body().getToken();
                System.out.println(token);
                sharedPreferences.edit().putString("token", token).apply();
                sharedPreferences.edit().putBoolean("logged", true).apply();
                goToTasks();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SignInActivity.this, "Fail " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void goToTasks() {
        Intent i = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
