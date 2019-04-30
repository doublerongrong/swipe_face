package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class RollcallSelect extends AppCompatActivity {

    private ImageButton photo_rollcall, callname_rollcall, random_rollcall,backBtn;
    private String classId,classDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall_select);

        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        classDoc = bundle.getString("class_doc");
        Log.i("classid:",classId);
        Log.i("classdoc:",classDoc);

        photo_rollcall = (ImageButton)findViewById(R.id.photo_rollcall);
        photo_rollcall.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), PhotoRollcall.class);
            intent.putExtra("class_id", classId);
            intent.putExtra("class_doc",classDoc);
            startActivity(intent);
        });
        callname_rollcall = (ImageButton)findViewById(R.id.callname_rollcall);
        callname_rollcall.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),CallNameRollCall.class);
            intent.putExtra("class_id", classId);
            intent.putExtra("class_doc",classDoc);
            startActivity(intent);
        });
        random_rollcall = (ImageButton)findViewById(R.id.random_rollcall);
        random_rollcall.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(getApplicationContext(),RandomRollcall.class);
            i.putExtra("class_id", classId);
            i.putExtra("class_doc",classDoc);
            startActivity(i);
        });

        backBtn = (ImageButton)findViewById(R.id.backIBtn);
        backBtn.setOnClickListener(view -> {
            finish();
        });


    }
}
