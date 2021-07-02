package com.example.todo.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText name,username,email,password;
    private static String token;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI=retrofit.create(JsonPlaceHolderAPI.class);

        name=findViewById(R.id.name_register);
        username=findViewById(R.id.username_register);
        email=findViewById(R.id.email_register);
        password=findViewById(R.id.password_register);

        register_button =findViewById(R.id.register_register);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterMethod();
            }
        });
    }
    public void RegisterMethod(){
        Register register = new Register(name.getText().toString(),email.getText().toString(),username.getText().toString(),password.getText().toString());
//        Register r = new Register("puranjay","p11@gmail.com","pk00003","01012002");
        System.out.println("Registration" + name.getText().toString() + email.getText().toString() +username.getText().toString() + password.getText().toString());
        Call<Register> call =jsonPlaceHolderAPI.createAcc(register);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Error " + response.code(),Toast.LENGTH_SHORT).show();
                    return;
                }
                token=response.body().getToken();
                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);

            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Fail " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}

//ToDo Error handling