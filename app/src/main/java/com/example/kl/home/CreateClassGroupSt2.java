package com.example.kl.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class CreateClassGroupSt2 extends AppCompatActivity {
    String classIdG, classYear, className;
    TextView stCreateclasstime, tvClassName, tvClassNum;
    EditText etGroupNumLow, etGroupNumHigh;
    Button btNextStepButton;
    Integer classNum;
    FirebaseFirestore db;
    AttributeCheck attributeCheck = new AttributeCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createclass_group_st2);
        CustomDateTimePicker custom;
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        db = FirebaseFirestore.getInstance();
        classIdG = bundle.getString("classId");
        classYear = bundle.getString("classYear");
        className = bundle.getString("className");
        classNum = bundle.getInt("classStuNum");
        stCreateclasstime = findViewById(R.id.createclasstime);
        etGroupNumLow = findViewById(R.id.groupNumLow);
        etGroupNumHigh = findViewById(R.id.groupNumHigh);
        btNextStepButton = findViewById(R.id.nextStepButton);
        tvClassName = findViewById(R.id.textViewClassName);
        tvClassName.setText(classYear + "\t" + className);
        tvClassNum = findViewById(R.id.classNum);
        tvClassNum.setText("班級成員\t:\t\t" +classNum.toString() + "人");
        Log.d("TESTGROUPST2", (classIdG + className + classYear));

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
                        stCreateclasstime.setText(year
                                + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                + " " + hour24 + ":" + min
                                + ":" + sec);
                    }

                    @Override
                    public void onCancel() {
                    }
                });
        custom.set24HourFormat(false);
        custom.setDate(Calendar.getInstance());
        stCreateclasstime.setOnClickListener(v -> custom.showDialog());

        btNextStepButton.setOnClickListener(v -> {
            if (attributeCheck.stringsNotNull(stCreateclasstime.getText().toString()) &&
                    attributeCheck.stringsNotNull(etGroupNumLow.getText().toString()) &&
                    attributeCheck.stringsNotNull(etGroupNumHigh.getText().toString())) {
                setGroupSet();
            } else {
                Toast.makeText(this, "請確認是否填寫", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setGroupSet() {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        System.out.println("Timestamp1" + stCreateclasstime.getText().toString());

        try {

            ts = Timestamp.valueOf(stCreateclasstime.getText().toString());
            System.out.println("Timestamp" + ts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> group = new HashMap<>();
        group.put("create_time", ts);
        group.put("group_numLow", Integer.valueOf(etGroupNumLow.getText().toString()));
        group.put("group_numHigh", Integer.valueOf(etGroupNumHigh.getText().toString()));

        db.collection("Class")
                .document("T40Qbn2ROrFBRiCorPxd")
                .update(group);


    }
}
