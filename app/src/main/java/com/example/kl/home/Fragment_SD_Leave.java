package com.example.kl.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kl.home.Adapter.Detail_LeaveListAdapter;
import com.example.kl.home.Model.Leave;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Fragment_SD_Leave extends Fragment {
    private static final String TAG = "Fragment_SD_Performance";

    private String studentId;
    private String student_id;
    private String class_id;
    private String leaveId;

    private FirebaseFirestore mFirestore;

    private RecyclerView mMainList;
    private Detail_LeaveListAdapter leaveListAdapter;
    private List<Leave> leaveList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arg = new Bundle();//fragment傳值
        arg = getArguments();//fragment傳值
        studentId = arg.getString("PassStudentId");
        student_id = arg.getString("PassStudent_Id");
        class_id = arg.getString("PassClass_Id");
        Log.d(TAG, "TEST FSD " + studentId);
        Log.d(TAG, "TEST FSD " + student_id);
        Log.d(TAG, "TEST FSD " + class_id);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__sd__leave, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();

        leaveList = new ArrayList<>();
        leaveListAdapter = new Detail_LeaveListAdapter(view.getContext(), leaveList);

        mMainList = (RecyclerView) view.findViewById(R.id.student_detail_Leavelist);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(leaveListAdapter);

        setleaveInfor(student_id, class_id);

    }

    private void setleaveInfor(String student_id, String class_id){
        mFirestore.collection("Leave")
                .whereEqualTo("student_id", student_id)
                .whereEqualTo("class_id", class_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "error" + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "here");
                        leaveId = doc.getDocument().getId();
                        Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveId);

                        //listBonus(student_id, class_id, perforDocId);
                        leaveList.add(leave);

                        leaveListAdapter.notifyDataSetChanged();

                    }

                }
            }

        });
    }


}
