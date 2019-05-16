package com.example.kl.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    Integer classStuNum; //課程學生人數
    TextView stCreateclasstime;
    TextView tvClassName;
    TextView tvClassNum;
    EditText etGroupNumLow;
    EditText etGroupNumHigh;
    ImageButton ibBackIBtn;
    CardView cvNextStepButton;
    FirebaseFirestore db;
    AttributeCheck attributeCheck = new AttributeCheck();
    private ImageView img_pgbar;
    private AnimationDrawable ad;
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
        assert bundle != null;
        classId = bundle.getString("classId");
        className = bundle.getString("className");
        classYear = bundle.getString("classYear");
        classStuNum = bundle.getInt("classStuNum");
        Log.d(TAG, "classId\t: " + classId+"\tclassYear:\t"+classYear+"\tclassName:\t"+className+"\tclassStuNum\t"+classStuNum.toString());






        //init xml
        tvClassName = findViewById(R.id.textViewClassName);
        tvClassName.setText(classYear + "\t" + className);
        tvClassNum = findViewById(R.id.classNum);
        tvClassNum.setText("班級成員\t:\t\t" +classStuNum.toString() + "人");
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
        //讀取dialog
        LayoutInflater lf = (LayoutInflater) CreateClassGroupSt2.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_create_class_groupst2,null);
        img_pgbar = (ImageView)vg.findViewById(R.id.img_pgbar);
        ad = (AnimationDrawable)img_pgbar.getDrawable();
        ad.start();
        android.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(CreateClassGroupSt2.this);
        builder1.setView(vg);
        AlertDialog dialog = builder1.create();
        dialog.show();
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
                .update(group).addOnCompleteListener(task -> {
                    dialog.dismiss();
            finish();
        });

        Intent intentToCreateClassGroupSt3 = new Intent();
        intentToCreateClassGroupSt3.setClass(CreateClassGroupSt2.this, CreateClassGroupSt3.class);
        Bundle bundleToCreateClassGroupSt3 = new Bundle();
        bundleToCreateClassGroupSt3.putString("classId", classId);
        bundleToCreateClassGroupSt3.putString("classYear", classYear);
        bundleToCreateClassGroupSt3.putString("className", className);
        bundleToCreateClassGroupSt3.putInt("classStuNum", classStuNum);
        intentToCreateClassGroupSt3.putExtras(bundleToCreateClassGroupSt3);
        startActivity(intentToCreateClassGroupSt3);

    }
}
