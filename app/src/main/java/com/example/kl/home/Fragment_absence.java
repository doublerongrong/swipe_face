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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Fragment_absence extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mMainList;
    private RollCallListAdapter rollCallListAdapter;
    private List<RollCallList> rollCallList;
    private String classId,docId;
    private List<String>absenceList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        classId = bundle.getString("class_id");
        docId = bundle.getString("classDoc_id");

        Log.i("absenceClass_id",classId);
        Log.i("absenceClassDoc_id",docId);
        return inflater.inflate(R.layout.fragment_absence, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rollCallList = new ArrayList<>();
        absenceList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();


        DocumentReference docRef = db.collection("Rollcall").document(docId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            absenceList = (ArrayList)documentSnapshot.get("rollcall_absence");
            Log.i("absence",absenceList.toString());

            for (int i=0;i<absenceList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",absenceList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot2 = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot2 : querySnapshot2.getDocuments()){
                        RollCallList StudentList = new RollCallList(documentSnapshot2.getString("student_id"),
                                documentSnapshot2.getString("student_department"),documentSnapshot2.getString("student_name"));
                        rollCallList.add(StudentList);
                    }
                });
            }
            Log.i("rollcall",rollCallList.toString());


        });

        RollCallListAdapter rollCallListAdapter = new RollCallListAdapter(getActivity().getApplicationContext(),rollCallList,"缺席");
        mMainList = (RecyclerView)getView().findViewById(R.id.rollacll_list2);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(rollCallListAdapter);






    }
}