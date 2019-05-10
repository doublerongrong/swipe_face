package com.example.kl.home;




import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.RollCallList;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RollcallResult extends AppCompatActivity implements ViewPager.OnPageChangeListener,
        TabLayout.OnTabSelectedListener{
    private final String TAG = "RollcallResult";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String request,classId,docId,classDocId,className;
    private ImageButton finishBtn;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    OkHttpClient client = new OkHttpClient();

    private Fragment_attend fragmentAttend = new Fragment_attend();
    private Fragment_absence fragmentAbsence = new Fragment_absence();
    private  Fragment_ClassDetail fragmentClassDetail = new Fragment_ClassDetail();
    private static Context mContext;
    private List<String> attendList,lateList,absenceList,oriAttend,oriLate,oriAbsence;
    private List<String> scoreList,scoreList1,scoreList2,absTimeList,absTimeList1,absTimeList2;
    private int lateMinus,absenteeMinus;
    private String perId,perf,absenceTimes,attendEmail,absenceEmail,lateEmail;
    private String score_url = "http://"+ FlassSetting.ip+":8080/ProjectApi/api/Warning/points";
    private String absence_url = "http://"+ FlassSetting.ip+":8080/ProjectApi/api/Warning/times";
    int score,abTimes,ewpoint,ewatimes;
    private String attId,absId,latId;
    int b ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rollcall_result);

        Log.d(TAG,"score_url: absence_url "+score_url+absence_url);

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
        finishBtn = (ImageButton)findViewById(R.id.finishButton);
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();
        lateList = new ArrayList<>();
        scoreList1 = new ArrayList<>();
        scoreList = new ArrayList<>();
        scoreList2 = new ArrayList<>();
        absTimeList1 = new ArrayList<>();
        absTimeList = new ArrayList<>();
        absTimeList2 = new ArrayList<>();
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
                        className = aClass.getClass_name();
                        ewatimes = aClass.getClass_ewtimes();
                        ewpoint = aClass.getClass_ewpoints();
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
                    for ( int n = 0; n < oriAbsence.size(); n++) {
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", oriAbsence.get(n));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query123", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                Log.i("perf",perf);
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                abTimes += 1;
                                score -= absenteeMinus;
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                                ChangePointRef.update("performance_absenceTimes",abTimes);
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
                    for ( int j = 0; j < lateList.size(); j++) {
                        latId = lateList.get(j);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", lateList.get(j));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList1.add(perf);
                                absTimeList1.add(absenceTimes);
                                if (oriAttend.contains(latId)){
                                    score -= lateMinus;
                                }
                                if(oriLate.contains(latId)){
                                    score = score;
                                }
                                if(oriAbsence.contains(latId)) {
                                    score -= (lateMinus - absenteeMinus);
                                    abTimes -= 1;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                                ChangePointRef.update("performance_absenceTimes",abTimes);
                            }
                            if (absTimeList1.size() == lateList.size()) {
                                //寄缺席預警信
                                for (int a = 0; a < absTimeList1.size(); a++) {
                                    if (Integer.parseInt(absTimeList1.get(a)) >= ewatimes) {
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", lateList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                lateEmail = documentSnapshot3.getString("student_email");
                                                sendAbsenceWarningEmail(lateEmail, className);
                                            }
                                        });
                                    }
                                }
                                for (int a = 0; a < scoreList1.size(); a++) {
                                    //寄分數預警信
                                    if (Integer.parseInt(scoreList1.get(a)) < ewpoint) {
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", lateList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                lateEmail = documentSnapshot3.getString("student_email");
                                                sendPointsWarningEmail(lateEmail, className);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }

                if(!absenceList.isEmpty()){
                    for ( int k = 0; k < absenceList.size(); k++) {
                        absId = absenceList.get(k);
                        Log.i("absId",absId);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", absenceList.get(k));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query123", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                Log.i("perf",perf);
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList.add(perf);
                                absTimeList.add(absenceTimes);
                                if (oriAttend.contains(absId)){
                                    Log.i("absence","work1");
                                    score -= absenteeMinus;
                                    abTimes += 1;
                                }
                                if (oriLate.contains(absId)){
                                    Log.i("absence","work2");
                                    score -= (absenteeMinus-lateMinus);
                                    abTimes += 1;
                                }
                                if (oriAbsence.contains(absId)) {
                                    Log.i("absence","work3");
                                    score = score;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                Map<String,Object> performance = new HashMap<>();
                                performance.put("performance_totalAttendance", score);
                                performance.put("performance_absenceTimes",abTimes);
                                ChangePointRef.update(performance);
                            }
                            if (absTimeList.size() == absenceList.size()){
                                //寄缺席預警信
                                for (int a=0;a<absTimeList.size();a++){
                                    if (Integer.parseInt(absTimeList.get(a)) >= ewatimes){
                                        Log.i("stu_id",absenceList.get(a));
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id",absenceList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                absenceEmail = documentSnapshot3.getString("student_email");
                                                sendAbsenceWarningEmail(absenceEmail,className);
                                            }
                                        });
                                    }
                                }
                                for (int a=0;a<scoreList.size();a++){
                                    //寄分數預警信
                                    if (Integer.parseInt(scoreList.get(a)) < ewpoint){
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", absenceList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                absenceEmail = documentSnapshot3.getString("student_email");
                                                sendPointsWarningEmail(absenceEmail,className);
                                            }
                                        });
                                    }
                                }
                            }

                        });
                    }

                }
                if(!attendList.isEmpty()){
                    for ( int l = 0; l < attendList.size(); l++) {
                        attId = attendList.get(l);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", attendList.get(l));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList2.add(perf);
                                absTimeList2.add(absenceTimes);
                                if (oriAttend.contains(attId)){
                                    score = score;
                                }
                                if (oriLate.contains(attId)){
                                    score += lateMinus;
                                }
                                if (oriAbsence.contains(attId)) {
                                    score += absenteeMinus;
                                    abTimes -= 1;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                                ChangePointRef.update("performance_absenceTimes",abTimes);

                                if (absTimeList2.size() == attendList.size()) {
                                    //寄缺席預警信
                                    for (int a = 0; a < absTimeList2.size(); a++) {
                                        if (Integer.parseInt(absTimeList2.get(a)) >= ewatimes) {
                                            Query query1 = db.collection("Student")
                                                    .whereEqualTo("student_id", attendList.get(a));
                                            query1.get().addOnCompleteListener(task2 -> {
                                                QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                                Log.i("query123", "work");
                                                for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                    attendEmail = documentSnapshot3.getString("student_email");
                                                    sendAbsenceWarningEmail(attendEmail, className);
                                                }
                                            });
                                        }
                                    }
                                    for (int a = 0; a < scoreList2.size(); a++) {
                                        //寄分數預警信
                                        if (Integer.parseInt(scoreList2.get(a)) < ewpoint) {
                                            Query query1 = db.collection("Student")
                                                    .whereEqualTo("student_id", attendList.get(a));
                                            query1.get().addOnCompleteListener(task2 -> {
                                                QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                                Log.i("query123", "work");
                                                for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                    attendEmail = documentSnapshot3.getString("student_email");
                                                    sendPointsWarningEmail(attendEmail, className);
                                                }
                                            });
                                        }
                                    }
                                }
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

    public void sendAbsenceWarningEmail (String email,String className){

        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        String url = absence_url + "/" + email + "/" + className;
        Log.i("email",email);
        Request.Builder formBody = new Request.Builder().url(url).method("POST", reqbody).header("Content-Length", "0");

        //MultipartBody requestBody = builder.build();//建立要求
        client.newCall(formBody.build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Create Android", "Test失敗");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("Create Android", "Test成功");

            }

        });
    }

    public void sendPointsWarningEmail (String email,String className){

        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        String url = score_url + "/" + email + "/" + className;
        Request.Builder formBody = new Request.Builder().url(url).method("POST", reqbody).header("Content-Length", "0");
        //MultipartBody requestBody = builder.build();//建立要求

        client.newCall(formBody.build()).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Create Android", "Test失敗");

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("Create Android", "Test成功");

            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            DocumentReference docRef = db.collection("Rollcall").document(docId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                lateList = (ArrayList) documentSnapshot.get("rollcall_late");
                absenceList = (ArrayList) documentSnapshot.get("rollcall_absence");
                attendList = (ArrayList)documentSnapshot.get("rollcall_attend");

                Log.i("late",lateList.toString());
                Log.i("absence",absenceList.toString());


                if(!lateList.isEmpty()){
                    for ( int j = 0; j < lateList.size(); j++) {
                        latId = lateList.get(j);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", lateList.get(j));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList1.add(perf);
                                absTimeList1.add(absenceTimes);
                                if (oriAttend.contains(latId)){
                                    score -= lateMinus;
                                }
                                if(oriLate.contains(latId)){
                                    score = score;
                                }
                                if(oriAbsence.contains(latId)) {
                                    score -= (lateMinus - absenteeMinus);
                                    abTimes -= 1;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                                ChangePointRef.update("performance_absenceTimes",abTimes);
                            }
                            if (absTimeList1.size() == lateList.size()) {
                                //寄缺席預警信
                                for (int a = 0; a < absTimeList1.size(); a++) {
                                    if (Integer.parseInt(absTimeList1.get(a)) >= ewatimes) {
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", lateList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                lateEmail = documentSnapshot3.getString("student_email");
                                                sendAbsenceWarningEmail(lateEmail, className);
                                            }
                                        });
                                    }
                                }
                                for (int a = 0; a < scoreList1.size(); a++) {
                                    //寄分數預警信
                                    if (Integer.parseInt(scoreList1.get(a)) < ewpoint) {
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", lateList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                lateEmail = documentSnapshot3.getString("student_email");
                                                sendPointsWarningEmail(lateEmail, className);
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }
                }

                if(!absenceList.isEmpty()){
                    for ( int k = 0; k < absenceList.size(); k++) {
                        absId = absenceList.get(k);
                        Log.i("absId",absId);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", absenceList.get(k));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query123", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                Log.i("perf",perf);
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList.add(perf);
                                absTimeList.add(absenceTimes);
                                if (oriAttend.contains(absId)){
                                    Log.i("absence","work1");
                                    score -= absenteeMinus;
                                    abTimes += 1;
                                }
                                if (oriLate.contains(absId)){
                                    Log.i("absence","work2");
                                    score -= (absenteeMinus-lateMinus);
                                    abTimes += 1;
                                }
                                if (oriAbsence.contains(absId)) {
                                    Log.i("absence","work3");
                                    score = score;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                Map<String,Object> performance = new HashMap<>();
                                performance.put("performance_totalAttendance", score);
                                performance.put("performance_absenceTimes",abTimes);
                                ChangePointRef.update(performance);
                            }
                            if (absTimeList.size() == absenceList.size()){
                                //寄缺席預警信
                                for (int a=0;a<absTimeList.size();a++){
                                    if (Integer.parseInt(absTimeList.get(a)) >= ewatimes){
                                        Log.i("stu_id",absenceList.get(a));
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id",absenceList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                absenceEmail = documentSnapshot3.getString("student_email");
                                                sendAbsenceWarningEmail(absenceEmail,className);
                                            }
                                        });
                                    }
                                }
                                for (int a=0;a<scoreList.size();a++){
                                    //寄分數預警信
                                    if (Integer.parseInt(scoreList.get(a)) < ewpoint){
                                        Query query1 = db.collection("Student")
                                                .whereEqualTo("student_id", absenceList.get(a));
                                        query1.get().addOnCompleteListener(task2 -> {
                                            QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                            Log.i("query123", "work");
                                            for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                absenceEmail = documentSnapshot3.getString("student_email");
                                                sendPointsWarningEmail(absenceEmail,className);
                                            }
                                        });
                                    }
                                }
                            }

                        });
                    }

                }
                if(!attendList.isEmpty()){
                    for ( int l = 0; l < attendList.size(); l++) {
                        attId = attendList.get(l);
                        Query query = db.collection("Performance")
                                .whereEqualTo("class_id", classId)
                                .whereEqualTo("student_id", attendList.get(l));
                        query.get().addOnCompleteListener(task -> {
                            QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                            Log.i("query", "work");
                            for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                perf = documentSnapshot2.get("performance_totalAttendance").toString();
                                absenceTimes = documentSnapshot2.get("performance_absenceTimes").toString();
                                perId = documentSnapshot2.getId();
                                score = Integer.parseInt(perf);
                                abTimes = Integer.parseInt(absenceTimes);
                                scoreList2.add(perf);
                                absTimeList2.add(absenceTimes);
                                if (oriAttend.contains(attId)){
                                    score = score;
                                }
                                if (oriLate.contains(attId)){
                                    score += lateMinus;
                                }
                                if (oriAbsence.contains(attId)) {
                                    score += absenteeMinus;
                                    abTimes -= 1;
                                }
                                DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                                ChangePointRef.update("performance_totalAttendance", score);
                                ChangePointRef.update("performance_absenceTimes",abTimes);

                                if (absTimeList2.size() == attendList.size()) {
                                    //寄缺席預警信
                                    for (int a = 0; a < absTimeList2.size(); a++) {
                                        if (Integer.parseInt(absTimeList2.get(a)) >= ewatimes) {
                                            Query query1 = db.collection("Student")
                                                    .whereEqualTo("student_id", attendList.get(a));
                                            query1.get().addOnCompleteListener(task2 -> {
                                                QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                                Log.i("query123", "work");
                                                for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                    attendEmail = documentSnapshot3.getString("student_email");
                                                    sendAbsenceWarningEmail(attendEmail, className);
                                                }
                                            });
                                        }
                                    }
                                    for (int a = 0; a < scoreList2.size(); a++) {
                                        //寄分數預警信
                                        if (Integer.parseInt(scoreList2.get(a)) < ewpoint) {
                                            Query query1 = db.collection("Student")
                                                    .whereEqualTo("student_id", attendList.get(a));
                                            query1.get().addOnCompleteListener(task2 -> {
                                                QuerySnapshot querySnapshot2 = task2.isSuccessful() ? task2.getResult() : null;
                                                Log.i("query123", "work");
                                                for (DocumentSnapshot documentSnapshot3 : querySnapshot2.getDocuments()) {
                                                    attendEmail = documentSnapshot3.getString("student_email");
                                                    sendPointsWarningEmail(attendEmail, className);
                                                }
                                            });
                                        }
                                    }
                                }
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

        }
        return false;
    }

}
