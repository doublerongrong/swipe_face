package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    private Button login;
    private TextView creatAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        login = (Button)findViewById(R.id.loginBtn);
        login.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this,SignIn.class);
            startActivity(i);
        });

        creatAccount = (TextView) findViewById(R.id.createAct);
        creatAccount.setOnClickListener(view -> {
            Intent i = new Intent();
            i.putExtra("request",0);
            i.setClass(this,SignUp.class);
            startActivity(i);
        });
    }
}
