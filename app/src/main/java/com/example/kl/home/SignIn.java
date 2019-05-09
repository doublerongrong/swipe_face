package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity {

    private EditText input_account;
    private EditText input_password;

    private TextView forgetPassword, signUp;

    private Button buttonLogin;
    private ImageButton backBtn;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mAuth = FirebaseAuth.getInstance();

        forgetPassword = (TextView)findViewById(R.id.forgetPassword);
        signUp = (TextView)findViewById(R.id.signUpText);

        input_account = (EditText) findViewById(R.id.input_account);
        input_password = (EditText) findViewById(R.id.input_password);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        backBtn = (ImageButton)findViewById(R.id.backIBtn);

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

        backBtn.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this,WelcomePage.class);
            startActivity(i);
        });

        forgetPassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        forgetPassword.setOnClickListener(view -> {
            LayoutInflater lf = (LayoutInflater) SignIn.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_forget_password,null);

            final EditText etShow = vg.findViewById(R.id.et_email);
            new AlertDialog.Builder(SignIn.this)
                    .setView(vg)
                    .setPositiveButton("確定", (dialog, which) -> {
                        String email = etShow.getText().toString();
                        if (email.isEmpty()) {
                            Toast.makeText(SignIn.this, "請輸入電子信箱", Toast.LENGTH_SHORT).show();

                        }
                        //else if(isVaildEmailFormat() != true){
                            //Toast.makeText(SignIn.this, "非電子信箱格式", Toast.LENGTH_SHORT).show();

                        //}
                        else {
                            mAuth.sendPasswordResetEmail(email);
                            Toast.makeText(SignIn.this,"請去信箱確認重設密碼信",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("取消", null).show();
        });

        signUp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        signUp.setOnClickListener(view -> {
            Intent i = new Intent();
            i.putExtra("request",1);
            i.setClass(this,SignUp.class);
            startActivity(i);
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
            Toast.makeText(this, "請輸入電子信箱", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SignIn.this, "電子信箱或密碼有誤", Toast.LENGTH_LONG).show();

                }else{
                    Intent i = new Intent();
                    i.putExtra("teacherEmail",email);
                    i.setClass(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });

    }

    private  boolean isVaildEmailFormat() {
        EditText etMail = (EditText) findViewById(R.id.et_email);
        if (etMail == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(etMail.getText().toString()).matches();
    }
}