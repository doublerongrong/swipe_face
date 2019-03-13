package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Fragment_PickAnswerDetail extends Fragment {
    private String classId;
    private String type;

    private String TAG ="PADetail";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId = args.getString("classId");
        type = args.getString("type");
        Log.d(TAG, "classId:" + classId);//fragment傳值
        Toast.makeText(getContext(), "現在課程資料庫代碼是:" + classId, Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(), "現在的模式是:" + type, Toast.LENGTH_LONG).show();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_answer_detail, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }
}
