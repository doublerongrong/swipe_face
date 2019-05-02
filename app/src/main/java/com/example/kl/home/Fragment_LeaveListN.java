package com.example.kl.home;


import android.content.Context;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//底部進假單
public class Fragment_LeaveListN extends Fragment {
    private static final String TAG = "LeaveList1";


    private String class_id;
    private String teacher_email;
    private String checkWay; //確認假單批改完後導向

    OnFragmentSelectedListener mCallback;//Fragment傳值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        if (args != null) {
            class_id = args.getString("info");
            teacher_email = args.getString("teacher_email");
            Log.d(TAG, "args: " + args.toString());
            checkWay = "底部欄";


        }

        Log.d(TAG, "class_id:" + class_id);//fragment傳值
        Log.d(TAG, "teacher_email:" + teacher_email);//fragment傳值
        Log.d(TAG, "checkWay:" + checkWay);//fragment傳值


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leavelist, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        FragmentTabHost tabHost = (FragmentTabHost) view.findViewById(android.R.id.tabhost);
        tabHost.setup(getActivity(), getFragmentManager(), R.id.realtabcontent);


        //1

        Bundle arg1 = new Bundle();
        arg1.putString("PassTeacher_email", teacher_email);
        arg1.putString("PassCheck_Way", checkWay);
        arg1.putString("PassList_Way", "未審核");

        tabHost.addTab(tabHost.newTabSpec("Checking")
                        .setIndicator("未審核"),
                Fragment_LeaveListN_View.class,
                arg1);
        Log.d(TAG,"TEST LOG RUN Times  " + arg1.toString());

        //2

        Bundle arg2 = new Bundle();
        arg2.putString("PassTeacher_email", teacher_email);
        arg2.putString("PassCheck_Way", checkWay);
        arg2.putString("PassList_Way", "准假");

        tabHost.addTab(tabHost.newTabSpec("Check0")
                        .setIndicator("准假"),
                Fragment_LeaveListN_View.class,
                arg2);
        //3

        Bundle arg3 = new Bundle();
        arg3.putString("PassTeacher_email", teacher_email);
        arg3.putString("PassCheck_Way", checkWay);
        arg3.putString("PassList_Way", "不准假");

        tabHost.addTab(tabHost.newTabSpec("Check1")
                        .setIndicator("不准假"),
                Fragment_LeaveListN_View.class,
                arg3);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }


}
