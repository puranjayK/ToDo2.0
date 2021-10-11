package com.example.todo.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.todo.JsonPlaceHolderAPI;
import com.example.todo.R;
import com.example.todo.Register;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private Button register_button;
    private EditText name, username, email, password;
    private static String token;
    SharedPreferences sharedPreferences;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.actionBarColor));
        actionBar.setBackgroundDrawable(colorDrawable);

        jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        name = findViewById(R.id.name_register);
        username = findViewById(R.id.username_register);
        email = findViewById(R.id.email_register);
        password = findViewById(R.id.password_register);
        progressBar = findViewById(R.id.progressBar3);
        register_button = findViewById(R.id.register_register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterMethod();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(this, SignInActivity.class);
        startActivity(i);
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void RegisterMethod() {
        String nameString = name.getText().toString(),
                emailString = email.getText().toString(),
                usernameString = username.getText().toString(),
                passwordString = password.getText().toString();

        Register register = new Register(nameString, emailString, usernameString, passwordString);
        System.out.println("Registration" + name.getText().toString() + email.getText().toString() + username.getText().toString() + password.getText().toString());
        progressBar.setVisibility(View.VISIBLE);
        Call<Register> call = jsonPlaceHolderAPI.createAcc(register);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (!response.isSuccessful()) {
                    if (response.code() == 400) {
                        if (nameString.equals(""))
                            Toast.makeText(RegisterActivity.this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();
                        if (emailString.equals(""))
                            Toast.makeText(RegisterActivity.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                        if (usernameString.equals(""))
                            Toast.makeText(RegisterActivity.this, "Please Enter Your Username", Toast.LENGTH_SHORT).show();
                        if (passwordString.equals(""))
                            Toast.makeText(RegisterActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    return;
                }
                token = response.body().getToken();
                sharedPreferences.edit().putString("token", token).apply();
                sharedPreferences.edit().putBoolean("logged", true).apply();
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(RegisterActivity.this, "Fail " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

