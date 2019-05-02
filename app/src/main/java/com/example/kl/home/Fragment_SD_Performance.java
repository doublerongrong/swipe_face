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
import android.widget.TextView;

import com.example.kl.home.Adapter.Detail_BonusListAdapter;
import com.example.kl.home.Model.Bonus;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Performance;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Fragment_SD_Performance extends Fragment {

    private static final String TAG = "Fragment_SD_Performance";

    private String studentId;
    private String student_id;
    private String class_id;
    private String perforDocId;
    private TextView textViewTotalBonus;

    private FirebaseFirestore mFirestore;

    private RecyclerView mMainList;
    private Detail_BonusListAdapter bonusListAdapter;
    private List<Bonus> bonusList;

    private String answerBouns;
    private String RDanswerBonus;


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
        return inflater.inflate(R.layout.fragment__sd__performance, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();

        textViewTotalBonus = (TextView) view.findViewById(R.id.textViewTotalBonus);

        bonusList = new ArrayList<>();
        bonusListAdapter = new Detail_BonusListAdapter(view.getContext(), bonusList);

        mMainList = (RecyclerView) view.findViewById(R.id.student_detail_Bonuslist);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(bonusListAdapter);

        getPoints(class_id);
        setInfor(student_id, class_id);

    }

    private void setInfor(String student_id, String class_id) {
        mFirestore.collection("Performance")
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
                                        perforDocId = doc.getDocument().getId();
                                        Performance preformance = doc.getDocument().toObject(Performance.class);
                                        textViewTotalBonus.setText(Integer.toString(preformance.getPerformance_totalBonus()));

                                        listBonus(student_id, class_id, perforDocId);

                    }

                }
            }

        });
    }

    private void listBonus(String student_id, String class_id, String perforDocId){
        mFirestore.collection("Performance").document(perforDocId).collection("Bonus").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                if(e != null){
                    Log.d(TAG,"error" + e.getMessage());
                }

                for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Bonus bonus = doc.getDocument().toObject(Bonus.class);

                        bonus.setAnswerBonus(answerBouns);
                        bonus.setRDanswerBonus(RDanswerBonus);

                        bonusList.add(bonus);

                        bonusListAdapter.notifyDataSetChanged();
                    }

                }
            }
        });
    }

    private void getPoints( String class_id){
        mFirestore.collection("Class")
                .whereEqualTo("class_id", class_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "error" + e.getMessage());
                }
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "here");
                        Class aClass = doc.getDocument().toObject(Class.class);

                        answerBouns = Integer.toString(aClass.getClass_answerbonus());
                        RDanswerBonus = Integer.toString(aClass.getClass_rdanswerbonus());


                    }

                }
            }

        });
    }




}
