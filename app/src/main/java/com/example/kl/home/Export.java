package com.example.kl.home;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import it.sephiroth.android.library.easing.Expo;


public class Export extends AppCompatActivity {
    private final String TAG = "Export";
    private EditText etEmail;
    private ImageButton ibStuList;
    private ImageButton ibStuScore;
    private ImageButton ibGroupList;
    private ImageButton ibGroupScore;

    private String classId;
    private String class_id;
    private ImageButton backIBtn;
    private final OkHttpClient client = new OkHttpClient();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");
        class_id = bundle.getString("class_id");
        Log.d(TAG, "bundle : classId" + classId + " class_id : " + class_id);
        system system1 =new system();

        etEmail = findViewById(R.id.etEmail);
        ibStuScore = findViewById(R.id.ibStuScore);
        ibStuScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "stuScore";
                sendEmail(type);
            }
        });
        ibGroupList = findViewById(R.id.ibGroupList);
        ibGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "groupList";
                sendEmail(type);
            }
        });
        ibGroupScore = findViewById(R.id.ibGroupScore);
        ibGroupScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "groupScore";
                sendEmail(type);
            }
        });
        ibStuList = findViewById(R.id.ibStuList);
        ibStuList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "stuList";
                sendEmail(type);
            }
        });
        backIBtn = findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());
        Log.d(TAG,"IP:"+ system.ip);


    }

    private void sendEmail(String type) {

        String email = etEmail.getText().toString().trim();
        String url = "http://"+system.ip+":8080/ProjectApi/api/Export/";
        Log.d(TAG,"url: "+url);

        Log.d(TAG,"type: "+type);
        if ("".equals(email)) {
            Toast.makeText(this, "請輸入信箱", Toast.LENGTH_SHORT).show();
        } else {
            if(type.equals("stuScore")){
                url = url+"StudentGrade/"+class_id+"/"+email;
            }
            if(type.equals("stuList")){
                url = url+"StudentList/"+class_id+"/"+email;
            }
            if(type.equals("groupScore")){
                url = url+"GroupGrade/"+class_id+"/"+email;
            }
            if(type.equals("groupList")){
                url = url+"GroupList/"+class_id+"/"+email;
            }
            Log.d(TAG,"email: "+email);
            Log.d(TAG,"url: "+url);
            RequestBody reqbody = RequestBody.create(null, new byte[0]);
            Request.Builder formBody = new Request.Builder().url(url).method("POST", reqbody).header("Content-Length", "0");
            client.newCall(formBody.build()).enqueue(new Callback() {

                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {

                }

            });
        }
    }
}
