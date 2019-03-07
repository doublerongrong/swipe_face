package com.example.kl.home;

import android.app.ProgressDialog;
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
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;


public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextName1;
    private EditText editTextName2;
    private EditText editTextEmail;
    private EditText editTextPassword1;
    private EditText editTextPassword2;
    private EditText editTextKey;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextName1 = (EditText) findViewById(R.id.editTextName1);
        editTextName2 = (EditText) findViewById(R.id.editTextName2);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword1 = (EditText) findViewById(R.id.editTextPassword1);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        editTextKey = (EditText) findViewById(R.id.editTextKey);

        buttonRegister.setOnClickListener(this);
    }

    private void registerUser() {
        String name1 = editTextName1.getText().toString().trim();
        String name2 = editTextName2.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String key = editTextKey.getText().toString().trim();
        final String name = name1 + name2;


        if(TextUtils.isEmpty(name1)) {
            //name1 is empty
            Toast.makeText(this, "請輸入姓氏", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(name2)) {
            //name2 is empty
            Toast.makeText(this, "請輸入名字", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "請輸入email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(password1)) {
            //password1 is empty
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(password2)) {
            //password2 is empty
            Toast.makeText(this, "請再次輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(key)) {
            //key is empty
            Toast.makeText(this, "請輸入金鑰", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Map<String,Object> user = new HashMap<>();
                            user.put("teacher_name",name);
                            user.put("teacher_email",email);


                            db.collection("Teacher").add(user);

                            //user is successfully registered and logged in
                            //we will start the profile activity here
                            Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignUp.this, "Could not register. please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });






    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            registerUser();
        }
    }
}
