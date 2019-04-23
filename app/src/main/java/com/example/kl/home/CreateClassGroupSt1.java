package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.Class;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateClassGroupSt1 extends AppCompatActivity {
    final String TAG = "CreateClassGroupSt1";
    String classId;//classDocId
    String classYear;//class學年
    String className;//課程名
    Date classCreateTime;//課程建立時間
    Integer classNum;//課程學生數
    TextView tvClassName;
    TextView tvClassGroupInfo;
    Button btNextStepButton;
    FirebaseFirestore db;
    boolean group_state_go;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createclass_group_st1);

        //init DB
        db = FirebaseFirestore.getInstance();

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        assert bundle != null;
        classId = bundle.getString("classId");
        Log.d(TAG,"classId : "+classId);

        //init xml
        btNextStepButton = findViewById(R.id.nextStepButton);

        if(!classId.isEmpty()) {
            DocumentReference docRef = db.collection("Class").document(classId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Class aClass = documentSnapshot.toObject(Class.class);
                className = aClass.getClass_name();
                classYear = aClass.getClass_year();
                classNum = aClass.getStudent_id().size();
                classCreateTime =  aClass.getCreate_time();
                group_state_go = aClass.isGroup_state_go();

                //init xml
                tvClassGroupInfo = findViewById(R.id.classGroupInfo);
                tvClassName = findViewById(R.id.textViewClassName);
                tvClassName.setText(classYear+"\t"+className);

                if(group_state_go){
                    tvClassGroupInfo.setText("已開始分組\n"+"結束時間 : "+sdf.format(classCreateTime));

                    //Button Click
                    btNextStepButton.setOnClickListener(v -> {
                        Toast.makeText(CreateClassGroupSt1.this, "已開始分組",
                                Toast.LENGTH_SHORT).show();
                    });

                }
                else{
                    tvClassGroupInfo.setText("尚未分組");

                    //Button Click
                    btNextStepButton.setOnClickListener(v -> {
                        Intent intent = new Intent();
                        intent.setClass(this, CreateClassGroupSt2.class);
                        Bundle bundleGroup = new Bundle();
                        bundleGroup.putString("classId", classId);
                        bundleGroup.putString("classYear", classYear);
                        bundleGroup.putString("className", className);
                        bundleGroup.putInt("classNum", classNum);
                        intent.putExtras(bundleGroup);
                        startActivity(intent);
                    });
                }

            });
        }




    }
}
