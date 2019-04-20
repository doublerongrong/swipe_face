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
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Adapter.GroupDetailSettingAdapter;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupDetailSetting extends AppCompatActivity {
    final String TAG = "GroupDetailSetting";
    GroupDetailSettingAdapter groupDetailSettingAdapter;
    RecyclerView groupDetailSettingRecycleView;
    FirebaseFirestore db;
    List<Student> studentList;
    Integer groupBonus;
    String groupLeader;
    String classId;
    String class_Id;
    String groupId;
    Integer groupNumHigh;
    Integer groupNumLow;
    Integer groupNum;
    LinearLayout linerLGroupDetailSetPlus;
    Button btGroupDetailSet;
    TextView tvGroupInfo;
    String groupDetailAndSetting ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail_setting);

        // init DB
        db = FirebaseFirestore.getInstance();

        //init function
        GroupNumberForCh groupNumberForCh = new GroupNumberForCh();

        //init Intent Bundle
        Intent Intent = getIntent();
        Bundle bundle = Intent.getExtras();
        groupBonus = bundle.getInt("groupBonus");
        groupLeader = bundle.getString("groupLeader");
        classId = bundle.getString("classId");
        class_Id = bundle.getString("class_Id");
        groupId = bundle.getString("groupId");
        studentList = bundle.getParcelableArrayList("studentList");
        for (int i = studentList.size() - 1; i >= 0; i--) {
            Student item = studentList.get(i);
            Log.d(TAG, "\t63行" + item.getStudent_id());
        }

        //query DB
        if(!classId.isEmpty()) {
            DocumentReference docRef = db.collection("Class").document(classId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                Class aClass = documentSnapshot.toObject(Class.class);
                groupNum = aClass.getGroup_num();
                groupNumHigh = aClass.getGroup_numHigh();
                groupNumLow = aClass.getGroup_numLow();
                tvGroupInfo = findViewById(R.id.textViewGroupInfo);
                tvGroupInfo.setText(groupNumberForCh.transNum(groupNum)+"\t\t人數\t"+groupNumLow+"~"+groupNumHigh);
            });
        }

        // init xml
        linerLGroupDetailSetPlus = findViewById(R.id.groupDetailSetPlus);
        linerLGroupDetailSetPlus.setOnClickListener(v -> {
            customClick(v);
            groupDetailSettingAdapter.notifyDataSetChanged();
        });
        btGroupDetailSet = findViewById(R.id.groupDetailSetting);
        btGroupDetailSet.setOnClickListener(v -> {
            stepFinish();
        });

        //init Adapter
//        studentList = new ArrayList<>();
        groupDetailSettingAdapter = new GroupDetailSettingAdapter(this, studentList, groupLeader);

        //init RecycleView
        groupDetailSettingRecycleView = findViewById(R.id.groupDetailSettingRecycleView);
        groupDetailSettingRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupDetailSettingRecycleView.setLayoutManager(mgr);
        groupDetailSettingRecycleView.setAdapter(groupDetailSettingAdapter);


        groupDetailSettingAdapter.setOnTransPageClickListener((student_id, student) -> {
            for (int i = 0 ; i < studentList.size() ; i++) {
                Student item = studentList.get(i);
                Log.d(TAG,"item : "+item.getStudent_id());
                if(student_id.equals(item.getStudent_id())){
                    studentList.remove(item);
                }
            }
            groupDetailSettingAdapter.notifyDataSetChanged();
        });
    }

    private void stepFinish() {
        if(studentList.size() >= groupNumLow && studentList.size() <= groupNumHigh){
            ArrayList<String> newStudentList = new ArrayList<>();
            for (int i = 0; i < studentList.size(); i++) {
                Log.d(TAG, "stepFinish +" + studentList.get(i).getStudent_id());
                newStudentList.add(studentList.get(i).getStudent_id());
            }
            Map<String, Object> group = new HashMap<>();
            group.put("student_id", newStudentList);

            db.collection("Class")
                    .document(classId).collection("Group")
                    .document(groupId).update(group)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        groupDetailSettingAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            groupDetailSettingAdapter.notifyDataSetChanged();


            Intent intent = new Intent();
            intent.setClass(GroupDetailSetting.this, GroupDetail.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle1 = new Bundle();
            bundle1.putInt("groupBonus", groupBonus);
            bundle1.putString("groupLeader", groupLeader);
            bundle1.putString("classId", classId);
            bundle1.putString("class_Id", class_Id);
            bundle1.putString("groupId", groupId);

            intent.putExtras(bundle1);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(GroupDetailSetting.this, "人數不符合規定",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void customClick(View v) {
        LayoutInflater lf = (LayoutInflater) GroupDetailSetting.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_group_detail_setting, null);
        ArrayList<String> checkNumberList = new ArrayList<>();
        for(int i =0 ;i<studentList.size();i++){
            checkNumberList.add(studentList.get(i).getStudent_id());
        }
        final EditText etShow = vg.findViewById(R.id.et_name);
        new AlertDialog.Builder(GroupDetailSetting.this)
                .setView(vg)
                .setPositiveButton("確定", (dialog, which) -> {
                    String updateName = etShow.getText().toString();
                    if (updateName.isEmpty()) {
                        Toast.makeText(GroupDetailSetting.this, "請輸入學號", Toast.LENGTH_SHORT).show();

                    }
                    else if(checkNumberList.contains(updateName)){
                        Toast.makeText(GroupDetailSetting.this, "學號已存在", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        DocumentReference docRef = db.collection("Class")
                                .document(classId);
                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                assert document != null;
                                if (document.exists()) {
                                    Class aClass = document.toObject(Class.class);
                                    List<String> studentListStr = aClass.getStudent_id();
                                    //如果輸入學號不存在於課程裡
                                    if (!studentListStr.contains(updateName)) {
                                        Toast.makeText(GroupDetailSetting.this, "該學號不存在於課程裡",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.collection("Student").whereEqualTo("student_id", updateName)
                                                .addSnapshotListener((documentSnapshots, e) -> {
                                                    if (e != null) {
                                                        Log.d(TAG, "Error :" + e.getMessage());
                                                    }

                                                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                                            Student aStudent = doc.getDocument().toObject(Student.class).withId(updateName);
                                                            Log.d(TAG, aStudent.getStudent_id());
                                                            studentList.add(aStudent);
                                                            Log.d(TAG, "aStudent_id" + studentList);

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
                })
                .setNegativeButton("取消", null).show();

        groupDetailSettingAdapter.notifyDataSetChanged();
    }


}
