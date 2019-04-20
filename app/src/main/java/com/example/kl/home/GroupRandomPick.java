package com.example.kl.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupRandomPick extends AppCompatActivity {
    private String classId;
    String groupId;
    private FirebaseFirestore db;
    private String TAG = "GroupRandomPick";
    Integer random;
    Integer groupBFBonus;
    Integer groupAFBonus;

    private Class aClass;
    private Group group;
    private Student student;

    private TextView tvGroupNum;
    TextView tvGroupLeader;
    TextView tvDepartAndGrade;
    private CardView card_nextone;
    private CardView card_correct_answer;
    GroupNumberForCh groupNumberForCh = new GroupNumberForCh();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_random_pick);

        //init Intent Bundle
        Intent Intent = getIntent();
        Bundle bundle = Intent.getExtras();
        if (bundle != null) {
            classId = bundle.getString("classId");
        }

        //init xml
        tvGroupNum = findViewById(R.id.tvgroupnum);
        tvGroupLeader = findViewById(R.id.tvgroupleader);
        tvDepartAndGrade = findViewById(R.id.tvdepartandgrade);
        card_nextone =findViewById(R.id.card_nextone);
        card_correct_answer = findViewById(R.id.card_correct_answer);

        //init method
        setRandomPick();

        //init function
    }

    private void setRandomPick() {
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            aClass = documentSnapshot.toObject(Class.class);
            List<String> groupLeaderRandom = null;
            if (aClass != null) {
                groupLeaderRandom = aClass.getGroup_leader();
                if (groupLeaderRandom != null) {
                    random = (int) (Math.random() * groupLeaderRandom.size());
                }
            }



            if (groupLeaderRandom != null) {
                Log.d(TAG, "random size:" + groupLeaderRandom.size());
            }
            Log.d(TAG, "random :" + random);

            if (groupLeaderRandom != null) {
                db.collection("Class").document(classId).collection("Group")
                        .whereEqualTo("group_leader", groupLeaderRandom.get(random))
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    group = document.toObject(Group.class);
                                    groupId = document.getId();
//                                        setView(student, aClass);
                                }
                                db.collection("Student")
                                        .whereEqualTo("student_id", group.getGroup_leader())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                                        student = document.toObject(Student.class);
                                                    }
                                                } else {
                                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                                }
                                                tvGroupLeader.setText(student.getStudent_name());
                                                tvGroupNum.setText(groupNumberForCh.transNum(group.getGroup_num()));
                                                tvDepartAndGrade.setText(student.getStudent_department());
                                            }
                                        });
                                card_correct_answer.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        groupBFBonus = (Integer.valueOf(group.getGroup_bonus()));
                                        groupAFBonus = groupBFBonus +1;
                                        setPoint(groupId,groupAFBonus);
                                        setRandomPick();

                                    }
                                });

                                card_nextone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        setRandomPick();

                                    }
                                });
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        });
            }
        });
    }

    private void setPoint(String groupId,Integer groupAFBonus) {
        Map<String, Object> group = new HashMap<>();
        group.put("group_bonus", groupAFBonus);

        db.collection("Class")
                .document(classId).collection("Group")
                .document(groupId).update(group)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

}
