package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.Class;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateClassGroupSt3 extends AppCompatActivity {
    final String TAG = "CreateClassGroupSt3";
    String classId;//classDocId
    String classYear;//class學年
    String className;//課程名
    Integer groupNumHigh;
    Integer groupNumLow;
    Date classCreateTime;//課程建立時間
    Integer classStuNum;//課程學生數
    TextView tvClassName;
    TextView tvGroupNumHigh;
    TextView tvGroupNumLow;
    TextView tvClassNum;
    TextView tvCreateClassTime;
    CardView cvNextStepButton;
    FirebaseFirestore db;
    ImageButton backIBtn;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createclass_group_st3);

        //init DB
        db = FirebaseFirestore.getInstance();

        //init xml
        cvNextStepButton = findViewById(R.id.nextStepButton);

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        assert bundle != null;
        classId = bundle.getString("classId");
        className = bundle.getString("className");
        classYear = bundle.getString("classYear");
        classStuNum = bundle.getInt("classStuNum");
        Log.d(TAG, "classId\t: " + classId+"\tclassYear:\t"+classYear+"\tclassName:\t"+className+"\tclassStuNum\t"+classStuNum.toString());


        if (!classId.isEmpty()) {
            DocumentReference docRef = db.collection("Class").document(classId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Class aClass = documentSnapshot.toObject(Class.class);
                classCreateTime = aClass.getCreate_time();
                groupNumHigh = aClass.getGroup_numHigh();
                groupNumLow = aClass.getGroup_numLow();
                //init xml
                tvClassName = findViewById(R.id.textViewClassName);
                tvClassName.setText(classYear + "\t" + className);
                tvGroupNumHigh = findViewById(R.id.groupNumHigh);
                tvGroupNumHigh.setText(groupNumHigh.toString());
                tvGroupNumLow = findViewById(R.id.groupNumLow);
                tvGroupNumLow.setText(groupNumLow.toString());
                tvClassNum = findViewById(R.id.classNum);
                tvClassNum.setText("班級成員\t:\t\t" +classStuNum.toString() + "人");
                tvCreateClassTime = findViewById(R.id.createclasstime);
                tvCreateClassTime.setText(sdf.format(classCreateTime));
                backIBtn = findViewById(R.id.backIBtn);
                backIBtn.setOnClickListener(v -> finish());

                cvNextStepButton.setOnClickListener(v -> {
                    Toast.makeText(CreateClassGroupSt3.this, "已開始分組",
                            Toast.LENGTH_SHORT).show();

                });


            });
        }
    }
}
