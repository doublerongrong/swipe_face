package com.example.kl.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kl.home.Adapter.GroupListDetailAdapter;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupDetail extends AppCompatActivity {
    GroupListDetailAdapter groupListDetailAdapter;
    RecyclerView groupDetailRecycleView;
    FirebaseFirestore db;
    List<Student> studentList;
    String class_Id, groupLeader, groupBonus, groupId, classId;
    TextView tvGroupDetailBonus;
    Button btGroupDetailSetting;
    String TAG = "GroupDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        // init DB
        db = FirebaseFirestore.getInstance();

        // Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        groupBonus = bundle.getString("groupBonus");
        groupLeader = bundle.getString("groupLeader");
        classId = bundle.getString("classId");
        class_Id = bundle.getString("class_Id");
        groupId = bundle.getString("groupId");
        Log.d(TAG, "\ngroupBonus\t" + groupBonus + "\ngroupLeader\t" + groupLeader + "\nclassId\t" +
                classId + "\nclass_id\t" + class_Id + "\ngroupId\t" + groupId);
        // init xml
        tvGroupDetailBonus = findViewById(R.id.textViewGroupBonus);
        tvGroupDetailBonus.setText("回答次數：\t\t" + groupBonus);
        btGroupDetailSetting = findViewById(R.id.groupDetailSetting);
        btGroupDetailSetting.setOnClickListener(v -> {
            Intent intent = new Intent();
            bundle.putString("class_Id", class_Id);
            bundle.putString("groupBonus", groupBonus);
            bundle.putString("groupLeader", groupLeader);
            bundle.putString("classId", classId);
            bundle.putString("groupId", groupId);
            intent.putExtras(bundle);
            intent.setClass(GroupDetail.this, GroupDetailSetting.class);
            startActivity(intent);
        });

        //init Adapter
        studentList = new ArrayList<>();
        groupListDetailAdapter = new GroupListDetailAdapter(this, studentList, groupLeader);

        //init RecycleView
        groupDetailRecycleView = findViewById(R.id.groupListDetail);
        groupDetailRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupDetailRecycleView.setLayoutManager(mgr);
        groupDetailRecycleView.setAdapter(groupListDetailAdapter);

        if (class_Id != null) {
            Log.d(TAG, "setAllLave");
            setAllGroupDetail();
        }
    }

    private void setAllGroupDetail() {
        DocumentReference docRef = db.collection("Class")
                .document(classId)
                .collection("Group")
                .document(groupId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Group group = document.toObject(Group.class);
                    List<String> groupStudentListStr = group.getStudent_id();
                    for (String student : groupStudentListStr) {
                        db.collection("Student").whereEqualTo("student_id", student).addSnapshotListener((documentSnapshots, e) -> {
                            if (e != null) {
                                Log.d(TAG, "Error :" + e.getMessage());
                            }

                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String studentId = doc.getDocument().getId();
                                    Student aStudent = doc.getDocument().toObject(Student.class).withId(studentId);
                                    studentList.add(aStudent);
                                    groupListDetailAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }
}
