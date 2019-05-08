package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class CreateClassGroupSt1 extends AppCompatActivity {
    private final String TAG = "CreateClassGroupSt1";
    private CardView cvNextStepButton;
    private String classId;
    private String classYear;
    private String className;
    private Integer classStuNum;
    private ImageButton backIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_class_group_st1);

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        assert bundle != null;
        classId = bundle.getString("classId");
        className = bundle.getString("className");
        classYear = bundle.getString("classYear");
        classStuNum = bundle.getInt("classStuNum");
        Log.d(TAG, "classId\t: " + classId+"\tclassYear:\t"+classYear+"\tclassName:\t"+className+"\tclassStuNum\t"+classStuNum.toString());

        //init xml
        cvNextStepButton = findViewById(R.id.nextStepButton);
        cvNextStepButton.setOnClickListener(v -> nextStep());
        backIBtn = findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());
    }

    private void nextStep() {
        Intent intentToCreateClassGroupSt2 = new Intent();
        intentToCreateClassGroupSt2.setClass(this, CreateClassGroupSt2.class);
        Bundle bundleToCreateClassGroupSt2 = new Bundle();
        bundleToCreateClassGroupSt2.putString("classId", classId);
        bundleToCreateClassGroupSt2.putString("classYear", classYear);
        bundleToCreateClassGroupSt2.putString("className", className);
        bundleToCreateClassGroupSt2.putInt("classStuNum", classStuNum);
        intentToCreateClassGroupSt2.putExtras(bundleToCreateClassGroupSt2);
        startActivity(intentToCreateClassGroupSt2);
        finish();
    }
}
