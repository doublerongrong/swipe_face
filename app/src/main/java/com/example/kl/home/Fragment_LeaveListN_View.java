package com.example.kl.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kl.home.Adapter.Detail_LeaveListAdapter;
import com.example.kl.home.Adapter.LeaveListAdapter;
import com.example.kl.home.Adapter.LeaveListClassDetailAdapter;
import com.example.kl.home.Model.Leave;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Fragment_LeaveListN_View extends Fragment {

    private static final String TAG = "LeaveListN_View";
    String class_id, teacher_email;
    String check_way, list_way;

    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private LeaveListAdapter leaveListAdapter;
    private List<Leave> leaveList;
    private Bundle arg;
    private TextView tvNoData;

    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        arg = getArguments();//fragment傳值
        teacher_email = arg.getString("PassTeacher_email");
        check_way = arg.getString("PassCheck_Way");
        list_way = arg.getString("PassList_Way");
        Log.d(TAG, arg.toString());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leavelist_recyclerview, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onViewCreated");
        leaveList = new ArrayList<>();
        leaveListAdapter = new LeaveListAdapter(view.getContext(), leaveList);
        mFirestore = FirebaseFirestore.getInstance();
        tvNoData = view.findViewById(R.id.tvNoData);
        mMainList = view.findViewById(R.id.leave_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(leaveListAdapter);
        setTeacherLeave(list_way);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach");
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }

    public void setTeacherLeave(String listWay) {
        mFirestore.collection("Leave")
                .whereEqualTo("leave_check", listWay)
                .whereEqualTo("teacher_email", teacher_email)
                .addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                Log.d(TAG, "error00" + e.getMessage());
            }
            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                if (doc.getType() == DocumentChange.Type.ADDED) {
                    String leaveRecordId = doc.getDocument().getId();
                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveRecordId);
                    if (!leaveList.contains(leave)) {
                        Log.d(TAG, leave.toString());
                        leave.setCheckWay("底部欄");
                        leaveList.add(leave);
                        leaveListAdapter.notifyDataSetChanged();
                    }
                }
            }
            if (leaveList.isEmpty()) {
                mMainList.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            } else {
                mMainList.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDestroy");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart");

    }
}