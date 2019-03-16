package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private EditText input_account;
    private EditText input_password;

    private Button buttonLogin;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mAuth = FirebaseAuth.getInstance();

        input_account = (EditText) findViewById(R.id.input_account);
        input_password = (EditText) findViewById(R.id.input_password);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){

                    startActivity(new Intent(SignIn.this, MainActivity.class));

                }
            }
        };

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startSignIn();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {

        String email = input_account.getText().toString();
        String password = input_password.getText().toString();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "請輸入帳號", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            //name2 is empty
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(SignIn.this, "帳號或密碼有誤", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}