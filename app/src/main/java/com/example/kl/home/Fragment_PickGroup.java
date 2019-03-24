package com.example.kl.home;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kl.home.Model.Bonus;
import com.example.kl.home.Model.Class;

import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fragment_PickGroup extends Fragment {
    private FirebaseFirestore db;
    private String TAG = "####ChienTestBy////Fragment_PickGroup";
    private int random;

    private String classId,class_Id;
    private Performance performance;

    private ArrayList<String> student_id;
    private Class aClass;
    private Group group;

    private TextView tvGroupNum,tvGroupLeader,tvDepartAndGrade;
    private CardView card_nextone;
    private CardView card_correct_answer;

    private int count ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId = args.getString("classId");
        class_Id = args.getString("class_Id");
        Log.d(TAG, "classId:" + classId);//fragment傳值

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmect_pick_group, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvGroupNum = view.findViewById(R.id.tvgroupnum);
        tvGroupLeader = view.findViewById(R.id.tvgroupleader);
        tvDepartAndGrade = view.findViewById(R.id.tvdepartandgrade);


        card_nextone = view.findViewById(R.id.card_nextone);
        card_correct_answer = view.findViewById(R.id.card_correct_answer);
        setRandomPick();
    }

    private void setRandomPick() {
        db = FirebaseFirestore.getInstance();


//        DocumentReference docRef = db.collection("Class").document(classId);
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                aClass = documentSnapshot.toObject(Class.class);
//                List<String> groupLeaderRandom = aClass.getGroup_leader();
//                random = (int) (Math.random() * groupLeaderRandom.size());
//
//
//                Log.d(TAG, "random :" + random);
//
//                db.collection("Class")
//                        .document(classId).collection("Group")
//                        .whereEqualTo("group_leader", student_id.get(random))
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                        group = document.toObject(Group.class);
//
//                                        setView(group);
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//            }
//        });

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                aClass = documentSnapshot.toObject(Class.class);
                List<String> groupLeaderRandom = aClass.getGroup_leader();
                random = (int) (Math.random() * groupLeaderRandom.size());


                Log.d(TAG, "random size:" + groupLeaderRandom.size());
                Log.d(TAG, "random :" + random);

                db.collection("Class").document(classId).collection("Group")
                        .whereEqualTo("group_leader", groupLeaderRandom.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        group = document.toObject(Group.class);

//                                        setView(student, aClass);
                                    }
                                        setView(group);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    private void setView(Group group) {

        tvGroupLeader.setText(group.getGroup_leader());
        tvGroupNum.setText(group.getGroup_num());
        tvDepartAndGrade.setText("還沒");

        card_correct_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPoint(group);
                setRandomPick();

            }
        });

        card_nextone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandomPick();

            }
        });
    }
    private void setPoint(Group group) {
        db.collection("Class")
                .document(class_Id)
                .collection("Group")
                .whereEqualTo("group_leader",group.getGroup_leader())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}
