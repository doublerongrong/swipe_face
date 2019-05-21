package com.example.kl.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Adapter.GroupPageAdapter;
import com.example.kl.home.Model.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupPage extends AppCompatActivity {
    private GroupPageAdapter groupPageAdapter; //Adapter
    private RecyclerView groupRecycleView; // RecycleView
    private FirebaseFirestore db;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<Group> groupList; // for RecycleView
    public String classYear; // 年度
    String className; // 課程名
    String class_Id; // classId
    String classId; //classDocId
    public TextView tvClassName;//xml上的年度課程欄位
    ImageButton backIBtn;
    private Integer classNum;
    private String TAG = "GroupPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"ActivityState: onCreate");
        setContentView(R.layout.group_page);
        //init db
        db = FirebaseFirestore.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        //init Adapter
        groupList = new ArrayList<>();
        groupPageAdapter = new GroupPageAdapter(this,groupList);

        //init RecycleView
        groupRecycleView = findViewById(R.id.grouplist);
        groupRecycleView.setHasFixedSize(true);
        LinearLayoutManager mgr = new LinearLayoutManager(this);
        groupRecycleView.setLayoutManager(mgr);
        groupRecycleView.setAdapter(groupPageAdapter);


        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        class_Id = bundle.getString("class_Id");
        classYear = bundle.getString("classYear");
        className = bundle.getString("className");
        classNum = bundle.getInt("classStuNum");
        classId = bundle.getString("classId");
        Log.d(TAG, "classId\t: " + classId+"\tclassYear:\t"+classYear+"\tclassName:\t"+className+"\tclassStuNum\t"+classNum.toString());

        //init xml
        tvClassName = findViewById(R.id.title_class);
        tvClassName.setText(className);
        View btPickGroup = findViewById(R.id.fabPickGroup);
        btPickGroup.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundleGroupPick = new Bundle();
            intent.setClass(GroupPage.this,GroupRandomPick.class);
            bundleGroupPick.putString("classId",classId);
            intent.putExtras(bundleGroupPick);
            startActivity(intent);
        });
        backIBtn = findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());
//        ibBackIBtn = findViewById(R.id.backIBtn);
//        ibBackIBtn.setOnClickListener(v -> finish());

        //AllGroup Method
        if (class_Id != null) {
            Log.d(TAG,"setAllLave");
            setAllGroup();
            groupPageAdapter.notifyDataSetChanged();
        }
    }

    void refreshItems() {
        // Load items
        // ...
        int size = groupList.size();
        groupList.clear();
        groupPageAdapter.notifyItemRangeRemoved(0,size);

        if(groupList.size()==0){
            db.collection("Class")
                    .document(classId)
                    .collection("Group")
                    .orderBy("group_num")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String groupId = document.getId();//group docId
                                Log.d(TAG,groupId);
                                Group group = document.toObject(Group.class).withId(groupId);
                                groupList.add(group);
                            }
                            int sizeadd = groupList.size();
                            groupPageAdapter.notifyItemRangeInserted(0,sizeadd);

                            onItemsLoadComplete();


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    });
        }

        // Load complete
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"ActivityState: onResume");
//        setAllGroup();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"ActivityState: onPause");
//        groupList.clear();
//        groupPageAdapter.notifyItemRangeRemoved(0,groupList.size());

    }

    private void setAllGroup() {
//        db.collection("Class")
//                .document(classId)
//                .collection("Group")
//                .orderBy("group_num")
//                .addSnapshotListener((documentSnapshots, e) -> {
//                    if (e != null) {
//                        Log.d(TAG, "Error :" + e.getMessage());
//                    }
//                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
//                        if (doc.getType() == DocumentChange.Type.ADDED ) {
//                            String groupId = doc.getDocument().getId();//group docId
//                            Log.d(TAG,groupId);
//                            Group group = doc.getDocument().toObject(Group.class).withId(groupId);
//                            groupList.add(group);
//                            groupPageAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });

        db.collection("Class")
                .document(classId)
                .collection("Group")
                .orderBy("group_num")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                            String groupId = document.getId();//group docId
                            Log.d(TAG,groupId);
                            Group group = document.toObject(Group.class).withId(groupId);
                            groupList.add(group);
                        }
                        groupPageAdapter.notifyDataSetChanged();


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        groupPageAdapter.setOnTransPageClickListener((groupLeader, groupBonus, groupId) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(GroupPage.this,GroupDetail.class);
            bundle.putString("class_Id",class_Id);
            bundle.putInt("groupBonus",groupBonus);
            bundle.putString("groupLeader",groupLeader);
            bundle.putString("classId",classId);
            bundle.putString("groupId",groupId);
            Log.d(TAG,"setOnTransPageClickListener : "+class_Id+groupBonus+groupLeader+classId+groupId);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }
}