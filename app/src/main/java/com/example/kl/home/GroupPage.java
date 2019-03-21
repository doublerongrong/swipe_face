package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.kl.home.Adapter.ClassListAdapter;
import com.example.kl.home.Adapter.GroupListAdapter;
import com.example.kl.home.Adapter.LeaveListAdapter;
import com.example.kl.home.Model.Group;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupPage extends AppCompatActivity {
    private GroupListAdapter groupListAdapter;
    private RecyclerView groupRecycleView;
    private FirebaseFirestore db;
    private List<Group> groupList;
    public String classYear,className,class_Id,classId;
    public TextView tvClassName;
    private Integer classNum;
    private String TAG = "GroupPage";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page);
        //init db
        db = FirebaseFirestore.getInstance();

        //init Adapter
        groupList = new ArrayList<>();
        groupListAdapter = new GroupListAdapter(this,groupList);

        //init RecycleView
        groupRecycleView = findViewById(R.id.grouplist);
        groupRecycleView.setHasFixedSize(true);
        GridLayoutManager mgr=new GridLayoutManager(this,2);//GridLayout 3列
        groupRecycleView.setLayoutManager(mgr);
        groupRecycleView.setAdapter(groupListAdapter);

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        class_Id = bundle.getString("class_Id");
        classYear = bundle.getString("classYear");
        className = bundle.getString("className");
        classNum = bundle.getInt("classStuNum");
        classId = bundle.getString("classId");

        //init xml
        tvClassName = findViewById(R.id.textViewClassName);
        tvClassName.setText(classYear+" "+className);

        //測class_id傳入
        if (class_Id != null) {
            Log.d(TAG,"setAllLave");
            setAllGroup();
        }

    }

    private void setAllGroup() {
        db.collection("Class").document(classId).collection("Group").addSnapshotListener((documentSnapshots, e) -> {
            if (e != null) {
                Log.d(TAG, "Error :" + e.getMessage());
            }
            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                if (doc.getType() == DocumentChange.Type.ADDED ) {
                    String groupId = doc.getDocument().getId();
                    Log.d(TAG+"85行",groupId);
                    Group group = doc.getDocument().toObject(Group.class).withId(groupId);
                    groupList.add(group);
                    groupListAdapter.notifyDataSetChanged();
                }
            }
        });
        groupListAdapter.setOnTransPageClickListener((studentIdArra, groupLeader, groupBonus,groupId, group) -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(GroupPage.this,GroupDetail.class);
            bundle.putString("class_Id",class_Id);
            bundle.putString("groupBonus",groupBonus);
            bundle.putString("groupLeader",groupLeader);
            bundle.putString("classId",classId);
            bundle.putString("groupId",groupId);
            Log.d(TAG,class_Id+groupBonus+groupLeader+classId+groupId);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }
}
