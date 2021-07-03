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
import android.widget.TextView;

import com.example.todo.R;

public class ProfileActivity extends AppCompatActivity {
Button logout;
TextView profile;
SharedPreferences sharedPreferences;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       Intent i =new Intent(this,MainActivity.class);
       startActivity(i);
       finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout=findViewById(R.id.logout);
        profile=findViewById(R.id.profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable
                = new ColorDrawable(getResources().getColor(R.color.actionBarColor));
        actionBar.setBackgroundDrawable(colorDrawable);

        sharedPreferences=getSharedPreferences("login",MODE_PRIVATE);
        String name=sharedPreferences.getString("name","Name");
        String username= sharedPreferences.getString("username","Username");
        String email= sharedPreferences.getString("email","abc@xyz.com");
        profile.setText("\nName: " + name +
                "\n\n" +"Username: " + username+
                "\n\n" +"Email: " + email+"\n");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                sharedPreferences.edit().putBoolean("logged",false).apply();
                Intent i=new Intent(ProfileActivity.this,SignInActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

}