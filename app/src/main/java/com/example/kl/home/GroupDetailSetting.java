package com.example.kl.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.kl.home.Adapter.GroupDetailSettingAdapter;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailSetting extends AppCompatActivity {
    GroupDetailSettingAdapter groupDetailSettingAdapter;
    RecyclerView groupDetailSettingRecycleView;
    FirebaseFirestore db;
    List<Student> studentList;
    String groupBonus, groupLeader, classId, class_Id, groupId, updateName;
    final String TAG = "GroupDetailSetting";
    LinearLayout linerLGroupDetailSetPlus;
    Button btGroupDetailSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_setting);
        // init DB
        db = FirebaseFirestore.getInstance();

        //init Intent Bundle
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
        linerLGroupDetailSetPlus = findViewById(R.id.groupDetailSetPlus);
        linerLGroupDetailSetPlus.setOnClickListener(v -> {
            customClick(v);

        });
        btGroupDetailSet = findViewById(R.id.groupDetailSetting);
        btGroupDetailSet.setOnClickListener(v -> finish());

        //init Adapter
        studentList = new ArrayList<>();
        groupDetailSettingAdapter = new GroupDetailSettingAdapter(this, studentList, groupLeader);

        //init RecycleView
        groupDetailSettingRecycleView = findViewById(R.id.groupDetailSettingRecycleView);
        groupDetailSettingRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupDetailSettingRecycleView.setLayoutManager(mgr);
        groupDetailSettingRecycleView.setAdapter(groupDetailSettingAdapter);

        if (class_Id != null) {
            Log.d(TAG, "setAllLave");
            setAllGroupDetailSetting();
        }
    }

    private void setAllGroupDetailSetting() {
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
                                    groupDetailSettingAdapter.notifyDataSetChanged();
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

    public void customClick(View v) {
        LayoutInflater lf = (LayoutInflater) GroupDetailSetting.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_group_detail_setting, null);
        final EditText etShow =  vg.findViewById(R.id.et_name);
        new AlertDialog.Builder(GroupDetailSetting.this)
                .setView(vg)
                .setPositiveButton("確定", (dialog, which) -> {
                            String updateName = etShow.getText().toString();
                            if(updateName.isEmpty()){
                                Toast.makeText(GroupDetailSetting.this, "請輸入學號" , Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else{
                                Map<String, Object> group = new HashMap<>();
                                group.put("student_id", FieldValue.arrayUnion(updateName));

                                db.collection("Class")
                                        .document(classId).collection("Group")
                                        .document(groupId).update(group)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                            groupDetailSettingAdapter.notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));

                                Log.d(TAG,"updateName\t\t"+updateName);
                            }
                })
                .setNegativeButton("取消", null).show();

        groupDetailSettingAdapter.notifyDataSetChanged();
    }
}
