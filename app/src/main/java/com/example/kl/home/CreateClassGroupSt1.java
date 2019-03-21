package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class CreateClassGroupSt1 extends AppCompatActivity {
    String classIdG,classYear,className;
    TextView tvClassName;
    Button btNextStepButton;
    Integer classNum;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createclass_group_st1);
        tvClassName = findViewById(R.id.textViewClassName);
        btNextStepButton = findViewById(R.id.nextStepButton);
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classIdG = bundle.getString("classId");
        classYear = bundle.getString("classYear");
        className = bundle.getString("className");
        classNum = bundle.getInt("classStuNum");
        tvClassName.setText(classYear+" "+className);
        btNextStepButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(this, CreateClassGroupSt2.class);
            Bundle bundleGroup = new Bundle();
            bundleGroup.putString("classId", classIdG);
            bundleGroup.putString("classYear", classYear);
            bundleGroup.putString("className", className);
            bundleGroup.putInt("classNum", classNum);
            intent.putExtras(bundleGroup);
            startActivity(intent);
        });
    }
}
