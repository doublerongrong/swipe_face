package com.example.kl.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class S_SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button buttonRegister;
    private EditText editTextName1;
    private EditText editTextName2;
    private EditText editTextEmail;
    private EditText editTextPassword1;
    private EditText editTextPassword2;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;

    private Spinner spinner1;
    private Spinner spinner2;

    private String[] school = {"請選擇學校","私立輔仁大學","國立台灣大學"}; //宣告字串陣列
    private String[] department = {"請選擇科系","資訊管理學系","日本語文學系"}; //宣告字串陣列

    private ArrayAdapter<String> listAdapter; //宣告listAdapter物件
    private ArrayAdapter<String> listAdapter2; //宣告listAdapter物件


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);


        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        editTextName1 = (EditText) findViewById(R.id.editTextName1);
        editTextName2 = (EditText) findViewById(R.id.editTextName2);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword1 = (EditText) findViewById(R.id.editTextPassword1);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);


        buttonRegister.setOnClickListener(this);

        spinner1=(Spinner)findViewById(R.id.spinner_school); //指向畫面上id為changetype1的Spinner物件
        spinner2=(Spinner)findViewById(R.id.spinner_department); //指向畫面上id為changetype1的Spinner物件
        listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, school);
        listAdapter2 = new ArrayAdapter<String>(this, R.layout.myspinner, department);

        listAdapter.setDropDownViewResource(R.layout.myspinner_dropdown);
        listAdapter2.setDropDownViewResource(R.layout.myspinner_dropdown);

        spinner1.setAdapter(listAdapter);
        spinner2.setAdapter(listAdapter2);
    }

    private void registerUser() {
        String name1 = editTextName1.getText().toString().trim();
        String name2 = editTextName2.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password1 = editTextPassword1.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        final String name = name1 + name2;

        final String school = spinner1.getSelectedItem().toString();
        final String department = spinner2.getSelectedItem().toString();
        int t ;



        if(TextUtils.isEmpty(name1)) {
            //name1 is empty
            Toast.makeText(this, "請輸入姓氏", Toast.LENGTH_SHORT).show();
            t = 1;
            //stopping the function execution further
            return;
        }else{
            t = 0;
        }
        Log.e("test1",Integer.toString(t));

        if(TextUtils.isEmpty(name2)) {
            //name2 is empty
            Toast.makeText(this, "請輸入名字", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            t =1;
            return;
        }else{
            t = 0;
        }
        Log.e("test2",Integer.toString(t));
        if(school == "請選擇學校") {
            //school is empty
            Toast.makeText(this, "請選擇學校", Toast.LENGTH_SHORT).show();
            t = 1;
            //stopping the function execution further
            return;
        }else {
            t = 0;
        }
        Log.e("test3",Integer.toString(t));
        if(department == "請選擇科系") {
            //department is empty
            Toast.makeText(this, "請選擇系所", Toast.LENGTH_SHORT).show();
            t = 1;
            //stopping the function execution further
            return;
        }else {
            t = 0;
        }
        Log.e("test4",Integer.toString(t));



        if(TextUtils.isEmpty(email)) {
            //email is empty
            Toast.makeText(this, "請輸入email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            t = 1;
            return;
        }else {
            t = 0;
        }
        Log.e("test5",Integer.toString(t));

        if(TextUtils.isEmpty(password1)) {
            //password1 is empty
            Toast.makeText(this, "請輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            t = 1;
            return;
        }
        else if(password1.length() < 5){
            //length of password1  is small than 5
            Toast.makeText(this,"密碼請輸入至少五碼",Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            t = 1;
            return;
        }else{
            t = 0;
        }
        Log.e("test6",Integer.toString(t));

        if(TextUtils.isEmpty(password2)) {
            //password2 is empty
            Toast.makeText(this, "請再次輸入密碼", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            t = 1;

            return;
        }
        else if (!password1.equals(password2)){

                Toast.makeText(this,"密碼確認有誤",Toast.LENGTH_SHORT).show();
                t = 1;
                return ;

        }else {
            t = 0;
        }
        Log.e("test7",Integer.toString(t));


        //if validations are ok
        //we will first show a progressbar
        if(t == 0) {
            Log.e("work","go");

            //progressDialog.setMessage("Registering User...");
            //progressDialog.show();
            firebaseAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(email, password1)

                    .addOnCompleteListener(v -> {



                                Map<String, Object> user = new HashMap<>();
                                user.put("teacher_name", name);
                                user.put("teacher_email", email);
                                user.put("teacher_school", school);
                                user.put("teacher_department", department);


                                db.collection("Student").add(user);

                                //user is successfully registered and logged in
                                //we will start the profile activity here
                                Toast.makeText(S_SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();



                    });
            Intent i = new Intent();
            i.setClass(S_SignUp.this,TrainAndTest.class);
            startActivity(i);
            finish();

        }







    }


    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            registerUser();

        }

    }
}