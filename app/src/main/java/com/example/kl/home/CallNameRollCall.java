package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallNameRollCall extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView mMainList;
    private RollCallAdapter rollCallAdapter;
    private List<RollCall> rollCallList;
    private CardView attend, absence;
    private String classId;
    private List<String> studentId ;
    private List<String> attendList, absenceList;
    int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callname_rollcall);


        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        Log.i("classid:",classId);
        studentId = new ArrayList<>();
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();



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
                            studentId.add(querySnapshot.getString("student_id"));

                            rollCallList.add(rollCall);
                        }
                        rollCallAdapter = new RollCallAdapter(CallNameRollCall.this, rollCallList);
                        mMainList.setAdapter(rollCallAdapter);

                        attend = (CardView)findViewById(R.id.card_attendance);
                        attend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mMainList != null && mMainList.getChildCount() > 0) {
                                    try {
                                        currentPosition = ((RecyclerView.LayoutParams) mMainList.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                                        Log.i("currentPosition---->" ,String.valueOf(currentPosition));
                                    } catch (Exception e) {

                                    }
                                }
                                //---------------------
                                //作者：左上角的天空
                                //来源：CSDN
                                //原文：https://blog.csdn.net/pszwll/article/details/82780150
                                //版权声明：本文为博主原创文章，转载请附上博文链接！



                                //查詢符合使用者學號的docId並寫入圖片的網址
                                Query query = db.collection("Rollcall").whereEqualTo("class_id",classId);
                                query.get().addOnCompleteListener(task1 -> {
                                    QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult(): null;

                                    for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                                        Log.i("documentUrl",documentSnapshot.getId());
                                        String docId = documentSnapshot.getId();
                                        attendList = (ArrayList)documentSnapshot.get("rollcall_attend");
                                        absenceList = (ArrayList)documentSnapshot.get("rollcall_absence");
                                        if (!attendList.contains(studentId.get(currentPosition))){

                                            attendList.add(studentId.get(currentPosition));
                                        }
                                        if (absenceList.contains(studentId.get(currentPosition))){
                                            for (int i=0;i<absenceList.size();i++){
                                                if (absenceList.get(i).equals(studentId.get(currentPosition))){
                                                    absenceList.remove(i);
                                                    i--;
                                                }
                                            }
                                        }

                                        Log.i("attendList",attendList.toString());
                                        Map<String, Object> attend = new HashMap<>();
                                        attend.put("rollcall_attend",attendList);
                                        attend.put("rollcall_absence",absenceList);
                                        db.collection("Rollcall").document(docId).update(attend);


                                    }
                                });

                            }

                        });

                        absence = (CardView)findViewById(R.id.card_absence);
                        absence.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mMainList != null && mMainList.getChildCount() > 0) {
                                    try {
                                        currentPosition = ((RecyclerView.LayoutParams) mMainList.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                                        Log.i("currentPosition---->" ,String.valueOf(currentPosition));
                                    } catch (Exception e) {

                                    }
                                }
                                //---------------------
                                //作者：左上角的天空
                                //来源：CSDN
                                //原文：https://blog.csdn.net/pszwll/article/details/82780150
                                //版权声明：本文为博主原创文章，转载请附上博文链接！



                                //查詢符合使用者學號的docId並寫入圖片的網址
                                Query query = db.collection("Rollcall").whereEqualTo("class_id",classId);
                                query.get().addOnCompleteListener(task1 -> {
                                    QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult(): null;

                                    for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                                        Log.i("documentUrl",documentSnapshot.getId());
                                        String docId = documentSnapshot.getId();
                                        absenceList = (ArrayList)documentSnapshot.get("rollcall_absence");
                                        attendList = (ArrayList)documentSnapshot.get("rollcall_attend");
                                        if (!absenceList.contains(studentId.get(currentPosition))){

                                            absenceList.add(studentId.get(currentPosition));
                                        }
                                        if (attendList.contains(studentId.get(currentPosition))){
                                            for (int i=0;i<attendList.size();i++){
                                                if (attendList.get(i).equals(studentId.get(currentPosition))){
                                                    attendList.remove(i);
                                                    i--;
                                                }
                                            }
                                        }
                                        Log.i("absenceList",absenceList.toString());
                                        Map<String, Object> absence = new HashMap<>();
                                        absence.put("rollcall_absence",absenceList);
                                        absence.put("rollcall_attend",attendList);
                                        db.collection("Rollcall").document(docId).update(absence);


                                    }
                                });

                            }

                        });


                    }
                });

    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }
}
