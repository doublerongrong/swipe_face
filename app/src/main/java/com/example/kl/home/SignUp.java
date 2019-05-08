package com.example.kl.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPassword1;
    private EditText editTextPassword2;
    private EditText editTextKey;
    private ImageButton backBtn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private List<String> class_id,teacher_officetime;
    private String teacher_office,teacher_registrationToken;

    private int check = 0, request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.t_sign_up);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle.getInt("request") == 0){
            request = 0;
        }
        if(bundle.getInt("request") == 1){
            request =1;
        }

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextName = (EditText) findViewById(R.id.input_name);
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword1 = (EditText) findViewById(R.id.input_password);
        editTextPassword2 = (EditText) findViewById(R.id.input_passwordCheck);
        editTextKey = (EditText) findViewById(R.id.input_key);
        backBtn = (ImageButton)findViewById(R.id.backIBtn);

        teacher_officetime = new ArrayList<>();
        class_id = new ArrayList<>();

        buttonRegister.setOnClickListener(this);
        backBtn.setOnClickListener(view -> {
            Intent i = new Intent();
            if (request == 0){
                i.setClass(this,WelcomePage.class);
            }
            if (request == 1){
                i.setClass(this,SignIn.class);
            }
            startActivity(i);
            finish();
        });
        editTextPassword1.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = getResources().getDrawable(R.drawable.baseline_check_circle_24px);
                    d.setBounds(0, 0, 70, 70);
                    editTextPassword1.setCompoundDrawables(null,null,d,null);
                    //d.setBounds(0, 0, 10, 30); //必须设置大小，否则不显示
                    //editTextPassword2.setError("密碼確認成功", d);
                    check = 1;
                    // 此处为得到焦点时的处理内容
                } else {
                    String password1 = editTextPassword1.getText().toString().trim();
                    if(password1.length() < 6){
                        //length of password1  is small than 6
                        //Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                        //d.setBounds(0, 0, 40, 40);
                       // editTextPassword1.setCompoundDrawables(null,null,d,null);

                        //Toast.makeText(this,"密碼確認有誤",Toast.LENGTH_SHORT).show();
                        //Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                        //d.setBounds(0, 0, 10, 30); //必须设置大小，否则不显示
                        editTextPassword1.setError("密碼至少六碼");
                        //stopping the function execution further
                        check = 0;
                        return;
                    }else{
                        check = 1;
                    }

                    // 此处为失去焦点时的处理内容
                }
            }
        });

        editTextPassword2.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Drawable d = getResources().getDrawable(R.drawable.baseline_check_circle_24px);
                    d.setBounds(0, 0, 70, 70);
                    editTextPassword2.setCompoundDrawables(null,null,d,null);
                    //d.setBounds(0, 0, 10, 30); //必须设置大小，否则不显示
                    //editTextPassword2.setError("密碼確認成功", d);
                    check = 1;
                    // 此处为得到焦点时的处理内容
                } else {
                    String password1 = editTextPassword1.getText().toString().trim();
                    String password2 = editTextPassword2.getText().toString().trim();
                     if (!password1.equals(password2)){
                       // Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                       // d.setBounds(0, 0, 40, 40);
                        //editTextPassword2.setCompoundDrawables(null,null,d,null);

                        //Toast.makeText(this,"密碼確認有誤",Toast.LENGTH_SHORT).show();
                        //Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                        //d.setBounds(0, 0, 10, 30); //必须设置大小，否则不显示

                        Drawable customErrorDrawable = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                        customErrorDrawable.setBounds(0, 0, 57, 57);
                        editTextPassword2.setError("密碼確認錯誤",customErrorDrawable);
                        check = 0;
                        return ;

                    }
                    else if(TextUtils.isEmpty(password2)){
                         //Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                         //d.setBounds(0, 0, 40, 40);
                        // editTextPassword2.setCompoundDrawables(null,null,d,null);

                         //Toast.makeText(this,"密碼確認有誤",Toast.LENGTH_SHORT).show();
                         //Drawable d = getResources().getDrawable(R.drawable.baseline_cancel_24px);
                         //d.setBounds(0, 0, 10, 30); //必须设置大小，否则不显示
                         editTextPassword2.setError("請再次確認密碼");
                         check = 0;
                         return ;
                    }else {
                        check = 1;
                    }
                    // 此处为失去焦点时的处理内容
                }
            }
        });


    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        String key = editTextKey.getText().toString().trim();


        if(TextUtils.isEmpty(name)) {
            //name1 is empty
            Toast.makeText(this, "請輸入姓名", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            check = 0;
            return;
        }else{
            check = 1;
        }

        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "請輸入email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            check = 0;
            return;
        }
        else if (isVaildEmailFormat() != true) {
            Toast.makeText(this, "請輸入email格式", Toast.LENGTH_SHORT).show();
            check = 0;
            return;
        }else{
            check = 1;
        }
        if(TextUtils.isEmpty(password1)) {
            //password1 is empty
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            check = 0;
            return;
        }else{
            check = 1;
        }
        if(TextUtils.isEmpty(password2)) {
            //password2 is empty
            Toast.makeText(this, "請再次輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            check = 0;
            return;
        }else {
            check = 1;
        }

        if(TextUtils.isEmpty(key)) {
            //key is empty
            Toast.makeText(this, "請輸入金鑰", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            check = 0;
            return;
        }else{
            check = 1;
        }

        //if validations are ok
        //we will first show a progressbar

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        if (check == 1) {

            firebaseAuth.createUserWithEmailAndPassword(email, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                teacher_registrationToken = FirebaseInstanceId.getInstance().getToken();
                                Map<String, Object> user = new HashMap<>();
                                user.put("class_id",class_id);
                                user.put("teacher_email", email);
                                user.put("teacher_name", name);
                                user.put("teacher_office",teacher_office);
                                user.put("teacher_officetime",teacher_officetime);
                                user.put("teacher_registrationToken",teacher_registrationToken);


                                db.collection("Teacher").add(user).addOnCompleteListener(task1 -> {
                                    Intent i = new Intent();
                                    i.putExtra("teacherEmail",email);
                                    i.setClass(getApplicationContext(),UserSignUpSetting.class);
                                    startActivity(i);
                                    finish();
                                });

                                //user is successfully registered and logged in
                                //we will start the profile activity here
                                Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, "Could not register. please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }






    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            registerUser();
        }
    }

    private  boolean isVaildEmailFormat() {
        EditText etMail = (EditText) findViewById(R.id.input_email);
        if (etMail == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(etMail.getText().toString()).matches();
    }
}
