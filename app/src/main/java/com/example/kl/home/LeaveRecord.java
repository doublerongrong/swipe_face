package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kl.home.Model.Leave;
import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Rollcall;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.RollcallId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveRecord extends AppCompatActivity {

    private static final String TAG = "Leavelog";

    private TextView class_name;
    private TextView leave_reason;
    private TextView leave_check;
    private TextView student_name;
    private TextView leave_date;
    private TextView leave_uploaddate;
    private TextView leave_content;
    private ImageView leave_photo;
    private Button agreeBtn;
    private Button disagreeBtn;

    private FirebaseFirestore mFirestore;
    private StorageReference mStorageRef;

    private String performanceId;// 調整出席分數
    private String rollcallId; // 調整List位置
    private int absenseMinus; //加回缺席分數
    private String class_id;
    private String student_id;
    private String leave_dateStr;
    private int currentScore;
    private List<String> casualList, funeralList, officalList, sickList, absenceList;
    private String leaveReasonStr; //此區為調整分數及出席用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaverecord);

        class_name = (TextView) findViewById(R.id.textClassName);
        leave_reason = (TextView) findViewById(R.id.textLeaveReason);
        leave_check = (TextView) findViewById(R.id.textLeaveCheck);
        student_name = (TextView) findViewById(R.id.textStudentName);
        leave_date = (TextView) findViewById(R.id.textLeaveDate);
        leave_uploaddate = (TextView) findViewById(R.id.textLeaveUploadDate);
        leave_content = (TextView) findViewById(R.id.textLeaveContent);
        leave_photo = (ImageView) findViewById(R.id.imageViewLeavePhotot);
        agreeBtn = (Button) findViewById(R.id.BtnLeaveCheck1);
        disagreeBtn = (Button) findViewById(R.id.BtnLeaveCheck0);

        casualList = new ArrayList<>();
        funeralList = new ArrayList<>();
        officalList = new ArrayList<>();
        sickList = new ArrayList<>();
        absenceList = new ArrayList<>();

        mFirestore = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Leave_photo");
        Leave leave = new Leave();

        Bundle bundle = getIntent().getExtras();
        String leaveId = bundle.getString("id");
        String ChangePage = bundle.getString("ChangePage");


        DocumentReference docRef = mFirestore.collection("Leave").document(leaveId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Leave leave = document.toObject(Leave.class);

                        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy/MM/dd");

                        class_id = leave.getClass_id();
                        student_id = leave.getStudent_id();
                        leave_dateStr = leave.getLeave_date();


                        String leaveUpdloadDate = myFmt2.format(leave.getLeave_uploaddate());

                        class_name.setText(leave.getClass_name());
                        leave_reason.setText(leave.getLeave_reason());
                        leave_check.setText(leave.getLeave_check());
                        student_name.setText(leave.getStudent_name());
                        leave_date.setText(leave.getLeave_date());
                        leave_uploaddate.setText(leaveUpdloadDate);
                        leave_content.setText(leave.getLeave_content());
                        String photoUrl = leave.getLeave_photoUrl();

                        leaveReasonStr = leave.getLeave_reason();//改陣列
                        getRollcallId();
                        getPerformanceId();
                        getScore();

                        Log.d(TAG, "After Function p : " + performanceId);
                        Log.d(TAG, "After Function r : " + rollcallId);
                        Log.d(TAG, "After Function m : " + absenseMinus);


                        StorageReference path = storageReference.child(photoUrl);
                        Log.d("TEST", path.toString());
                        Glide.with(LeaveRecord.this)
                                .load(path)
                                .into(leave_photo);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        agreeBtn.setOnClickListener(v -> {

            DocumentReference leaveCheckRef = mFirestore.collection("Leave").document(leaveId);
            leaveCheckRef
                    .update("leave_check", "准假")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            Log.d(TAG, "After Click p " + performanceId);
                            Log.d(TAG, "After Click r " + rollcallId);
                            Log.d(TAG, "After Click m " + absenseMinus);
                            changeList();

                            Intent intent = new Intent();
                            if (ChangePage.equals("Detail")) {
                                finish();
                            } else {
                                intent.setClass(LeaveRecord.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        });
        disagreeBtn.setOnClickListener(c -> {
            DocumentReference leaveCheckRef = mFirestore.collection("Leave").document(leaveId);
            leaveCheckRef
                    .update("leave_check", "不准假")
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                            Intent intent = new Intent();
                            if (ChangePage.equals("Detail")) {
                                finish();
                            } else {
                                intent.setClass(LeaveRecord.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

        });

    }

    public void getScore() {
        Query query = mFirestore.collection("Class").whereEqualTo("class_id", class_id);
        query.get().addOnCompleteListener(task1 -> {
            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Class aClass = documentSnapshot.toObject(Class.class);
                absenseMinus = aClass.getClass_absenteeminus();
                Log.d(TAG, "In Function m " + Integer.toString(absenseMinus));
            }
        });
    }

    public void setScore() {
        DocumentReference docRef = mFirestore.collection("Performance").document(performanceId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Performance performance = document.toObject(Performance.class);
                        currentScore = performance.getPerformance_totalAttendance();
                        //currentScore = document.get("performance_totalAttendance");
                        currentScore += absenseMinus;
                        Log.d(TAG, "Function Change r : " + rollcallId);
                        Log.d(TAG, "Function Change p : " + performanceId);
                        Log.d(TAG, "Function Change c : " + Integer.toString(currentScore));

                        Map<String, Object> attend = new HashMap<>();
                        attend.put("performance_totalAttendance", currentScore);
                        mFirestore.collection("Performance").document(performanceId).update(attend);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }//加回缺席分數

    public void getRollcallId() {
        Query query = mFirestore.collection("Rollcall").
                whereEqualTo("class_id", class_id);
        query.get().addOnCompleteListener(task1 -> {
            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

            SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy/MM/dd");
            Log.d(TAG, "In Function getRollcall 請假時間 :  " + leave_dateStr);
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

                Rollcall rollcall = documentSnapshot.toObject(Rollcall.class);
                rollcallId = documentSnapshot.getId();
                String checkDate = myFmt2.format(rollcall.getRollcall_time());

                Log.d(TAG, "In Function getRollcall 點名時間" + checkDate);
                if (checkDate.equals(leave_dateStr)) {
                    rollcallId = documentSnapshot.getId();
                    Log.d(TAG, "In Function getRollcall id :" + rollcallId);
                }
            }
        });
    }

    public void getPerformanceId() {
        Query query = mFirestore.collection("Performance").
                whereEqualTo("class_id", class_id).
                whereEqualTo("student_id", student_id);
        query.get().addOnCompleteListener(task1 -> {
            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {

                performanceId = documentSnapshot.getId();
                Log.d(TAG, "In Function getPerformance id: " + performanceId);
            }
        });
    }

    public void addCasual() {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "TEST DB" + rollcallId);
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        casualList = (ArrayList) document.get("rollcall_casual");
                        if (!casualList.contains(student_id)) {

                            casualList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_casual", casualList);
                        attend.put("rollcall_absence", absenceList);
                        mFirestore.collection("Rollcall").document(rollcallId).update(attend);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addFuneral() {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "TEST DB" + rollcallId);
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        funeralList = (ArrayList) document.get("rollcall_funeral");
                        if (!funeralList.contains(student_id)) {

                            funeralList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_funeral", funeralList);
                        attend.put("rollcall_absence", absenceList);
                        mFirestore.collection("Rollcall").document(rollcallId).update(attend);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addOffical() {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "TEST DB" + rollcallId);
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        officalList = (ArrayList) document.get("rollcall_officall");
                        if (!officalList.contains(student_id)) {

                            officalList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_officall", officalList);
                        attend.put("rollcall_absence", absenceList);
                        mFirestore.collection("Rollcall").document(rollcallId).update(attend);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void addSick() {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "TEST DB" + rollcallId);
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        sickList = (ArrayList) document.get("rollcall_sickl");
                        if (!sickList.contains(student_id)) {

                            sickList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_sick", sickList);
                        attend.put("rollcall_absence", absenceList);
                        mFirestore.collection("Rollcall").document(rollcallId).update(attend);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void changeList() {
        if (leaveReasonStr.equals("事假")) {
            Log.d(TAG, "事假確認");
            addCasual();
        } else if (leaveReasonStr.equals("喪假")) {
            Log.d(TAG, "喪假確認");
            addFuneral();
        } else if (leaveReasonStr.equals("公假")) {
            Log.d(TAG, "公假確認");
            addOffical();
        } else if (leaveReasonStr.equals("病假")) {
            Log.d(TAG, "病假確認");
            addSick();
        }
        setScore();
    }


}


