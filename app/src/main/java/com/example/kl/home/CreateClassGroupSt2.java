package com.example.kl.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.Class;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CreateClassGroupSt2 extends AppCompatActivity {
    final String TAG = "CreateClassGroupSt2";
    String classId;//課程DocId
    String classYear;//課程學年度
    String className;//課程名稱
    Integer classNum; //課程學生人數
    TextView stCreateclasstime;
    TextView tvClassName;
    TextView tvClassNum;
    EditText etGroupNumLow;
    EditText etGroupNumHigh;
    ImageButton ibBackIBtn;
    CardView cvNextStepButton;
    FirebaseFirestore db;
    AttributeCheck attributeCheck = new AttributeCheck();
    Date nowDate;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createclass_group_st2);

        //init db
        db = FirebaseFirestore.getInstance();

        //init function
        CustomDateTimePicker custom;
        nowDate = new Date();

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");

        if(!classId.isEmpty()) {
            DocumentReference docRef = db.collection("Class").document(classId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Class aClass = documentSnapshot.toObject(Class.class);
                className = aClass.getClass_name();
                classYear = aClass.getClass_year();
                classNum = aClass.getStudent_id().size();

                //init xml
                tvClassName = findViewById(R.id.textViewClassName);
                tvClassName.setText(classYear + "\t" + className);
                tvClassNum = findViewById(R.id.classNum);
                tvClassNum.setText("班級成員\t:\t\t" +classNum.toString() + "人");

            });
        }

        //init xml
        stCreateclasstime = findViewById(R.id.createclasstime);
        stCreateclasstime.setText(sdf.format(nowDate));
        etGroupNumLow = findViewById(R.id.groupNumLow);
        etGroupNumHigh = findViewById(R.id.groupNumHigh);
        cvNextStepButton = findViewById(R.id.nextStepButton);
        ibBackIBtn = findViewById(R.id.backIBtn);


        custom = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {

                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                        stCreateclasstime.setText("");
                        stCreateclasstime.setText(String.format("%d-%d-%d %d:%d:%d",
                                year,
                                monthNumber + 1,
                                calendarSelected.get(Calendar.DAY_OF_MONTH),
                                hour24,
                                min,
                                sec));
                    }

                    @Override
                    public void onCancel() {
                    }
                });
        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());
        stCreateclasstime.setOnClickListener(v -> custom.showDialog());


        //Button Click
        cvNextStepButton.setOnClickListener(v -> {
            if (attributeCheck.stringsNotNull(stCreateclasstime.getText().toString()) &&
                    attributeCheck.stringsNotNull(etGroupNumLow.getText().toString()) &&
                    attributeCheck.stringsNotNull(etGroupNumHigh.getText().toString())) {
                setGroupSet();
            } else {
                Toast.makeText(this, "請確認是否填寫", Toast.LENGTH_LONG).show();
            }
        });

        ibBackIBtn.setOnClickListener(v -> finish());

    }

    //設定小組設定
    private void setGroupSet() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        try {
            ts = Timestamp.valueOf(stCreateclasstime.getText().toString());
            Log.d(TAG,"TimeStamp : "+ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> group = new HashMap<>();
        group.put("create_time", ts);
        group.put("group_numLow", Integer.valueOf(etGroupNumLow.getText().toString()));
        group.put("group_numHigh", Integer.valueOf(etGroupNumHigh.getText().toString()));
        group.put("group_state_go",true);

        db.collection("Class")
                .document(classId)
                .update(group);

        Intent intent = new Intent();
        intent.setClass(CreateClassGroupSt2.this, CreateClassGroupSt1.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundleGroup = new Bundle();
        bundleGroup.putString("classId", classId);
        intent.putExtras(bundleGroup);
        startActivity(intent);
    }
}
