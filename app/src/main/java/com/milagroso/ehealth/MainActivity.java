package com.milagroso.ehealth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.milagroso.database.Firebase;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view){
        Firebase database = new Firebase();
        EditText input = (EditText) findViewById(R.id.correo);
        String email = input.getText().toString();
        System.out.println(email + "------------------------");
        database.getPacientes(email);
    }
}