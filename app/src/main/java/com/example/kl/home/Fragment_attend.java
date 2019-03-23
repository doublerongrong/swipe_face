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

import com.example.kl.home.Adapter.RollCallListAdapter;
import com.example.kl.home.Model.RollCallList;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_attend extends Fragment {

    public Fragment_attend(){

    }
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mMainList;
    private RollCallListAdapter rollCallListAdapter;
    private List<RollCallList> rollCallList;
    private String classId;
    private List<String>attendList,lateList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        //classId = bundle.getString("class_id");
         //Log.i("classid:",classId);
        return inflater.inflate(R.layout.fragment_attend, container, false);


    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rollCallList = new ArrayList<>();
        attendList = new ArrayList<>();
        lateList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        Query queryId = db.collection("Rollcall").whereEqualTo("class_id","Cf1f876435d");
        queryId.get().addOnCompleteListener(task1 -> {
            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Log.i("documentUrl", documentSnapshot.getId());
                attendList = (ArrayList) documentSnapshot.get("rollcall_attend");
                lateList = (ArrayList) documentSnapshot.get("rollcall_late");
                Log.i("attend",attendList.toString());
                Log.i("late",lateList.toString());
            }

            for (int i=0;i<attendList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",attendList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                    Log.i("query","work");
                    for (DocumentSnapshot documentSnapshot : querySnapshot1.getDocuments()){
                        RollCallList StudentList = new RollCallList(documentSnapshot.getString("student_id"),
                                documentSnapshot.getString("student_department"),documentSnapshot.getString("student_name"));

                        rollCallList.add(StudentList);
                    }

                });
            }
            Log.i("rollcall",rollCallList.toString());

            for (int i=0;i<lateList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",lateList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot2 = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot : querySnapshot2.getDocuments()){
                        RollCallList StudentList = new RollCallList(documentSnapshot.getString("student_id"),
                                documentSnapshot.getString("student_department"),documentSnapshot.getString("student_name"));
                        rollCallList.add(StudentList);
                    }
                });
            }
            Log.i("rollcall",rollCallList.toString());
        });



        RollCallListAdapter rollCallListAdapter = new RollCallListAdapter(getActivity().getApplicationContext(),rollCallList,"出席");
        mMainList = (RecyclerView)getView().findViewById(R.id.rollcall_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(rollCallListAdapter);






    }

}
