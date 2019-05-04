package com.example.kl.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
    String class_Id;
    String groupLeader;
    Integer groupBonus;
    String groupId;
    String classId;
    TextView tvGroupDetailBonus;
    TextView tvGroupDetailSetting;
    String TAG = "GroupDetail";
    String groupDetailAndSetting ;
    ImageButton ibBackIBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);

        // init DB
        db = FirebaseFirestore.getInstance();

        // Intent Bundle
        Intent Intent = getIntent();
        Bundle bundle = Intent.getExtras();
        groupBonus = bundle.getInt("groupBonus");//小組加分
        groupLeader = bundle.getString("groupLeader");//小組組長
        classId = bundle.getString("classId");//課程DocID
        class_Id = bundle.getString("class_Id");//課程ID
        groupId = bundle.getString("groupId");//小組DocID
        Log.d(TAG, String.format("\ngroupBonus\t%s\ngroupLeader\t%s\nclassId\t%s\nclass_id\t%s\ngroupId\t%s",
                groupBonus, groupLeader, classId, class_Id, groupId));

        // init xml
        tvGroupDetailBonus = findViewById(R.id.textViewGroupBonus);
        tvGroupDetailBonus.setText("回答次數：\t\t" + groupBonus + "\t\t次數");
        tvGroupDetailSetting = findViewById(R.id.groupDetailSetting);
        ibBackIBtn = findViewById(R.id.backIBtn);
        ibBackIBtn.setOnClickListener(v -> finish());

        //Button Click Intent to DetailSetting
        tvGroupDetailSetting.setOnClickListener(v -> {
            stepSetting();
        });

        //init Adapter
        studentList = new ArrayList<>();
        groupDetailAdapter = new GroupDetailAdapter(this, studentList);

        //init RecyclerView
        groupDetailRecycleView = findViewById(R.id.groupListDetail);
        groupDetailRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupDetailRecycleView.setLayoutManager(mgr);
        groupDetailRecycleView.setAdapter(groupDetailAdapter);
        //init RecyclerView Divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(groupDetailRecycleView.getContext(),
                mgr.getOrientation());
        groupDetailRecycleView.addItemDecoration(dividerItemDecoration);

//        if (class_Id != null) {
//            Log.d(TAG, "setAllLave");
//            setAllGroupDetail();
//            groupDetailAdapter.notifyDataSetChanged();
//        }

        //接Adapter的return
        groupDetailAdapter.setOnTransPageClickListener((studentId, student_id) -> {
            Log.d(TAG,"onTransPageClickTEST" + studentId);

            Intent intentToStu = new Intent();
            Bundle bundleToStu = new Bundle();
            intentToStu.setClass(GroupDetail.this, Activity_StudentDetail.class);
            Log.d(TAG,"TransPageInfo"+"student_id"+student_id+"studentId"+studentId+"student");
            bundleToStu.putString("PassStudentId", studentId);
            bundleToStu.putString("PassStudent_id", student_id);
            bundleToStu.putString("PassClass_id" ,class_Id);
            intentToStu.putExtras(bundleToStu);
            startActivity(intentToStu);



        });//Fragment換頁


    }

    //跳轉Activity時清空studentList
    @Override
    protected void onPause() {
        studentList.clear();
        super.onPause();
    }

    //回來時再次抓取
    @Override
    public void onResume() {
        setAllGroupDetail();
        super.onResume();  // Always call the superclass method first


    }
    private void stepSetting() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("class_Id", class_Id);//課程Id
        bundle.putInt("groupBonus", groupBonus);//小組加分
        bundle.putString("groupLeader", groupLeader);//小組組長
        bundle.putString("classId", classId);//課程docId
        bundle.putString("groupId", groupId);//小組DocId
        bundle.putParcelableArrayList("studentList", (ArrayList<? extends Parcelable>) studentList);
        intent.putExtras(bundle);
        intent.setClass(GroupDetail.this, GroupDetailSetting.class);
        startActivity(intent);
        groupDetailAdapter.notifyDataSetChanged();


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
                        db.collection("Student").whereEqualTo("student_id", student)
                                .addSnapshotListener((documentSnapshots, e) -> {
                            if (e != null) {
                            }
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String studentId = doc.getDocument().getId();
                                    Student aStudent = doc.getDocument().toObject(Student.class).withId(studentId);
//                                    for(int i =0; i<studentList.size() ;i++){
//
//                                    }
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
