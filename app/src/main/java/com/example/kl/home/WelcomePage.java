package com.example.kl.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {

    private Button login;
    private TextView creatAccount;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        mAuth = FirebaseAuth.getInstance();

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

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(WelcomePage.this, MainActivity.class));

                }
            }
        };

    }
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }
}
