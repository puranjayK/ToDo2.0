package com.example.todo.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todo.JsonPlaceHolderAPI;
import com.example.todo.Login;
import com.example.todo.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {
    private Button login;
    private EditText username,password;
    private TextView register;

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SignInActivity.token = token;
    }

    private static String token;
    private JsonPlaceHolderAPI jsonPlaceHolderAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("https://todo-app-csoc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderAPI=retrofit.create(JsonPlaceHolderAPI.class);

        login=findViewById(R.id.login);
        register=findViewById(R.id.register_login);
        username=findViewById(R.id.username_login);
        password=findViewById(R.id.password_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMethod();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SignInActivity.this,RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void LoginMethod(){
        Login login=new Login(username.getText().toString(),password.getText().toString());
        Call<Login> call=jsonPlaceHolderAPI.login(login);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(!response.isSuccessful()){
                    if(response.code()==400){
                        if(username.getText().toString().equals(""))
                            Toast.makeText(SignInActivity.this,"Please Enter Username",Toast.LENGTH_SHORT).show();
                        if (password.getText().toString().equals(""))
                            Toast.makeText(SignInActivity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(SignInActivity.this,"Invalid Username or Password",Toast.LENGTH_SHORT).show();

                    }
                        Toast.makeText(SignInActivity.this,"Unable to Login",Toast.LENGTH_SHORT).show();

                    return;
                }
                token=response.body().getToken();
                System.out.println(token);
                Intent i = new Intent(SignInActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(SignInActivity.this,"Fail " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}
