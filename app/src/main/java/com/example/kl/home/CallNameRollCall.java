package com.example.kl.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.kl.home.Adapter.ClassListAdapter;
import com.example.kl.home.Adapter.RollCallAdapter;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.RollCall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CallNameRollCall extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mMainList;
    private RollCallAdapter rollCallAdapter;
    private List<RollCall> rollCallList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callname_rollcall);


        db = FirebaseFirestore.getInstance();
        rollCallList = new ArrayList<>();
        mMainList = (RecyclerView) findViewById(R.id.rollcall);
        mMainList.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMainList.setLayoutManager(manager);
// 将SnapHelper attach 到RecyclrView
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mMainList);

        //作者：依然范特稀西
        //链接：https://juejin.im/post/58dd3d53da2f60005fbb0a6c
        //来源：掘金
        //著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。


        db.collection("Student")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot querySnapshot : task.getResult()) {
                            RollCall rollCall = new RollCall(querySnapshot.getString("student_name"),
                                    querySnapshot.getString("student_id"), querySnapshot.getString("student_school"),
                                    querySnapshot.getString("student_department"), querySnapshot.getString("student_email"),
                                    querySnapshot.getString("image_url"));
                            rollCallList.add(rollCall);
                        }
                        rollCallAdapter = new RollCallAdapter(CallNameRollCall.this, rollCallList);
                        mMainList.setAdapter(rollCallAdapter);


                    }
                });


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }
}
