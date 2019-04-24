package com.example.kl.home;




import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.design.widget.TabLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.RollCallList;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RollcallResult extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String request,classId,docId,classDocId;
    private Button finishBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Fragment_attend fragmentAttend = new Fragment_attend();
    private Fragment_absence fragmentAbsence = new Fragment_absence();
    private  Fragment_ClassDetail fragmentClassDetail = new Fragment_ClassDetail();
    private static Context mContext;
    private List<String> attendList,lateList,absenceList,oriAttend,oriLate,oriAbsence;
    private int lateMinus,absenteeMinus;
    private String perId,perf;
    int score,j,k,l,n;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall_result);

        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        docId = bundle.getString("classDoc_id");
        classDocId = bundle.getString("class_doc");
        if (bundle.getString("request") != null){
            request = bundle.getString("request");
        }
        Log.i("classid",classId);

        mContext = getApplicationContext();

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        finishBtn = (Button)findViewById(R.id.finishButton);
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();
        lateList = new ArrayList<>();
        oriAttend = new ArrayList<>();
        oriLate = new ArrayList<>();
        oriAbsence = new ArrayList<>();

        //獲取老師設定分數
        db.collection("Class").whereEqualTo("class_id", classId).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("RollcallResult", "Error :" + e.getMessage());
                        }
                        Class aClass = documentSnapshots.getDocuments().get(0).toObject(Class.class);
                        lateMinus = aClass.getClass_lateminus();
                        absenteeMinus = aClass.getClass_absenteeminus();
                    }
                });

        // 第一次扣分與設定初始狀態

        DocumentReference docRef2 = db.collection("Rollcall").document(docId);
        docRef2.get().addOnSuccessListener(documentSnapshot -> {
            oriLate = (ArrayList) documentSnapshot.get("rollcall_late");
            oriAbsence = (ArrayList) documentSnapshot.get("rollcall_absence");
            oriAttend = (ArrayList) documentSnapshot.get("rollcall_attend");

            if (request != null){
                if(!oriAbsence.isEmpty()){
                    for ( int i = 0; i < oriAbsence.size(); i++) {
                        n = i;
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", oriAbsence.get(n));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query123", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                Log.i("perf",perf);
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                score -= absenteeMinus;
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                            }
                        });
                    }
                }
            }
        });




        //註冊監聽
        viewPager.addOnPageChangeListener(this);
        tabLayout.addOnTabSelectedListener(this);

        finishBtn.setOnClickListener(view -> {
            DocumentReference docRef = db.collection("Rollcall").document(docId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                lateList = (ArrayList) documentSnapshot.get("rollcall_late");
                absenceList = (ArrayList) documentSnapshot.get("rollcall_absence");
                attendList = (ArrayList)documentSnapshot.get("rollcall_attend");

                Log.i("late",lateList.toString());
                Log.i("absence",absenceList.toString());


                if(!lateList.isEmpty()){
                    for ( int i = 0; i < lateList.size(); i++) {
                        j = i;
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", lateList.get(j));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                if (oriAttend.contains(lateList.get(j))){
                                    score -= lateMinus;
                                }
                                if(oriLate.contains(lateList.get(j))){
                                    score = score;
                                }
                                if(oriAbsence.contains(lateList.get(j))) {
                                    score -= (lateMinus - absenteeMinus);
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                            }
                        });
                    }
                }

                if(!absenceList.isEmpty()){
                    for ( int i = 0; i < absenceList.size(); i++) {
                        k = i;
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", absenceList.get(k));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query123", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                Log.i("perf",perf);
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                if (oriAttend.contains(absenceList.get(k))){
                                    Log.i("absence","work1");
                                    score -= absenteeMinus;
                                }
                                if (oriLate.contains(absenceList.get(k))){
                                    Log.i("absence","work2");
                                    score -= (absenteeMinus-lateMinus);
                                }
                                if (oriAbsence.contains(absenceList.get(k))) {
                                    Log.i("absence","work3");
                                    score = score;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                            }
                        });
                    }
                }
                if(!attendList.isEmpty()){
                    for ( int i = 0; i < attendList.size(); i++) {
                        l = i;
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", attendList.get(l));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                if (oriAttend.contains(attendList.get(l))){
                                    score = score;
                                }
                                if (oriLate.contains(attendList.get(l))){
                                    score += lateMinus;
                                }
                                if (oriAbsence.contains(attendList.get(l))) {
                                    score += absenteeMinus;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                            }
                        });
                    }
                }
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                intent.putExtra("class_id", classId);
                intent.putExtra("classDoc_id", classDocId);
                intent.putExtra("rollcall_id", docId);
                intent.putExtra("request", 2);
                startActivity(intent);
                finish();
            });
        });






        //添加適配器，在viewPager裡引入Fragment
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        Log.d("classIdddd",classId);
                        Log.d("docIdddddd",docId);
                        Bundle args = new Bundle();
                        args.putString("class_id", classId);
                        args.putString("classDoc_id",docId);
                        fragmentAttend.setArguments(args);

                        return fragmentAttend;
                    case 1:

                        Bundle args2 = new Bundle();
                        args2.putString("class_id", classId);
                        args2.putString("classDoc_id",docId);
                        fragmentAbsence.setArguments(args2);

                        return fragmentAbsence;

                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });


    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //TabLayout裡的TabItem被選中的時候觸發
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //viewPager滑動之後顯示觸發
        tabLayout.getTabAt(position).select();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static Context getmContext(){
        return mContext;
    }
}
