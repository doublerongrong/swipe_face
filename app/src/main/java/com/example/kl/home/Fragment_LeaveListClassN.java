package com.example.kl.home;


import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


//課堂進假單 A
public class Fragment_LeaveListClassN extends AppCompatActivity {
    private static final String TAG = "LeaveList1";


    private String class_id;
    private String teacher_email;
    private String checkWay; //確認假單批改完後導向
    private ImageButton backIBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leavelist_class);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            class_id = args.getString("info");
            teacher_email = args.getString("teacher_email");
            checkWay = "課堂中";
        }

        backIBtn = (ImageButton) findViewById(R.id.backIBtn) ;

        backIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Log.d(TAG, "class_id:" + class_id);//fragment傳值
        Log.d(TAG, "teacher_email:" + teacher_email);//fragment傳值
        Log.d(TAG, "checkWay:" + checkWay);//fragment傳值

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        //1
        Bundle arg1 = new Bundle();
        arg1.putString("PassTeacher_email", teacher_email);
        arg1.putString("PassClass_Id", class_id);
        arg1.putString("PassCheck_Way", checkWay);
        arg1.putString("PassList_Way", "未審核");

        tabHost.addTab(tabHost.newTabSpec("Checking")
                        .setIndicator("未審核"),
                Fragment_LeaveListClassN_View.class,
                arg1);
        Log.d(TAG, "TEST LOG RUN Times_class " + arg1.toString());

        //2

        Bundle arg2 = new Bundle();
        arg2.putString("PassTeacher_email", teacher_email);
        arg2.putString("PassClass_Id", class_id);
        arg2.putString("PassCheck_Way", checkWay);
        arg2.putString("PassList_Way", "准假");

        tabHost.addTab(tabHost.newTabSpec("Check0")
                        .setIndicator("准假"),
                Fragment_LeaveListClassN_View.class,
                arg2);
        //3

        Bundle arg3 = new Bundle();
        arg3.putString("PassTeacher_email", teacher_email);
        arg3.putString("PassClass_Id", class_id);
        arg3.putString("PassCheck_Way", checkWay);
        arg3.putString("PassList_Way", "不准假");

        tabHost.addTab(tabHost.newTabSpec("Check1")
                        .setIndicator("不准假"),
                Fragment_LeaveListClassN_View.class,
                arg3);



    }



}
