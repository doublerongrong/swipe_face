package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.kl.home.Adapter.GroupDetailAdapter;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GroupDetail extends AppCompatActivity {
    GroupDetailAdapter groupDetailAdapter;
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
        Intent Intent = getIntent();
        Bundle bundle = Intent.getExtras();
        groupBonus = bundle.getString("groupBonus");//小組加分
        groupLeader = bundle.getString("groupLeader");//小組組長
        classId = bundle.getString("classId");//課程DocID
        class_Id = bundle.getString("class_Id");//課程ID
        groupId = bundle.getString("groupId");//小組DocID
        Log.d(TAG, String.format("\ngroupBonus\t%s\ngroupLeader\t%s\nclassId\t%s\nclass_id\t%s\ngroupId\t%s",
                groupBonus, groupLeader, classId, class_Id, groupId));

        // init xml
        tvGroupDetailBonus = findViewById(R.id.textViewGroupBonus);
        tvGroupDetailBonus.setText("回答次數：\t\t" + groupId/*應該是groupBonus*/);
        btGroupDetailSetting = findViewById(R.id.groupDetailSetting);
        btGroupDetailSetting.setOnClickListener(v -> {
            Intent intent = new Intent();
            bundle.putString("class_Id", class_Id);//課程Id
            bundle.putString("groupBonus", groupBonus);//小組加分
            bundle.putString("groupLeader", groupLeader);//小組組長
            bundle.putString("classId", classId);//課程docId
            bundle.putString("groupId", groupId);//小組DocId
            intent.putExtras(bundle);
            intent.setClass(GroupDetail.this, GroupDetailSetting.class);
            startActivity(intent);
            groupDetailAdapter.notifyDataSetChanged();
        });

        //init Adapter
        studentList = new ArrayList<>();
        groupDetailAdapter = new GroupDetailAdapter(this, studentList, groupLeader);

        //init RecycleView
        groupDetailRecycleView = findViewById(R.id.groupListDetail);
        groupDetailRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupDetailRecycleView.setLayoutManager(mgr);
        groupDetailRecycleView.setAdapter(groupDetailAdapter);

        if (class_Id != null) {
            Log.d(TAG, "setAllLave");
            setAllGroupDetail();
            groupDetailAdapter.notifyDataSetChanged();
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
                                    groupDetailAdapter.notifyDataSetChanged();
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
