package com.example.kl.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kl.home.Adapter.Detail_AttendListAdapter;
import com.example.kl.home.Adapter.ClassListAdapter;
import com.example.kl.home.Adapter.RollCallAdapter;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.RollCallStudent;
import com.example.kl.home.Model.Rollcall;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallNameRollCall extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ImageView img_pgbar;
    private AnimationDrawable ad;
    private static final String TAG = "CallNameRollCall";
    private RecyclerView mMainList;
    private RollCallAdapter rollCallAdapter;
    private List<RollCallStudent> rollCallList;
    private Button attend, absence;
    private Button  finishBtn;
    private ImageButton returnBtn;
    private String classId,classDoc,performanceId,AttendPointString;
    private List<String> studentId,classMember ;
    private List<String> attendList, absenceList,casualList,funeralList,lateList,officalList,sickList;
    int currentPosition = 0;
    int count = 0;
    private int AttendPoints,absenteeMinus;
    private String docId ;
    private Date time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callname_rollcall);


        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        classDoc = bundle.getString("class_doc");
        Log.i("classid:",classId);
        Log.i("classdoc:",classDoc);
        studentId = new ArrayList<>();
        classMember = new ArrayList<>();
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();
        casualList = new ArrayList<>();
        funeralList = new ArrayList<>();
        lateList = new ArrayList<>();
        officalList = new ArrayList<>();
        sickList = new ArrayList<>();



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
        Query query = db.collection("Class").whereEqualTo("class_id",classId);
                query.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot = task.isSuccessful() ? task.getResult(): null;
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                            classMember = (ArrayList)documentSnapshot.get("student_id");
                        }
                    for (int i=0;i<classMember.size();i++){
                        Query query1 = db.collection("Student").whereEqualTo("student_id",classMember.get(i));
                        query1.get().addOnCompleteListener(task1 -> {
                            QuerySnapshot querySnapshot1 = task1.isSuccessful() ? task1.getResult(): null;
                            for(DocumentSnapshot documentSnapshot : querySnapshot1.getDocuments()){
                                RollCallStudent rollCallStudent = new RollCallStudent(documentSnapshot.getString("student_name"),
                                        documentSnapshot.getString("student_id"),
                                        documentSnapshot.getString("student_department"),documentSnapshot.getString("image_url"));
                                studentId.add(documentSnapshot.get("student_id").toString());
                                rollCallList.add(rollCallStudent);
                            }
                            rollCallAdapter = new RollCallAdapter(CallNameRollCall.this,rollCallList);
                            mMainList.setAdapter(rollCallAdapter);

                            attend = (Button) findViewById(R.id.card_attendance);
                            attend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(CallNameRollCall.this,"出席",Toast.LENGTH_SHORT).show();
                                    count += 1;
                                    if (mMainList != null && mMainList.getChildCount() > 0) {
                                        try {
                                            currentPosition = ((RecyclerView.LayoutParams) mMainList.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                                            Log.i("currentPosition---->", String.valueOf(currentPosition));
                                        } catch (Exception e) {

                                        }
                                    }
                                    if (!attendList.contains(studentId.get(currentPosition))) {

                                        attendList.add(studentId.get(currentPosition));
                                    }
                                    if (absenceList.contains(studentId.get(currentPosition))) {
                                        for (int i = 0; i < absenceList.size(); i++) {
                                            if (absenceList.get(i).equals(studentId.get(currentPosition))) {
                                                absenceList.remove(i);
                                                i--;
                                            }
                                        }
                                    }
                                    Log.i("attendList", attendList.toString());
                                    if (count == 1) {
                                        time = new Date();
                                        Map<String, Object> attend = new HashMap<>();
                                        attend.put("class_id", classId);
                                        attend.put("rollcall_attend", attendList);
                                        attend.put("rollcall_absence", absenceList);
                                        attend.put("rollcall_casual", casualList);
                                        attend.put("rollcall_funeral", funeralList);
                                        attend.put("rollcall_late", lateList);
                                        attend.put("rollcall_offical", officalList);
                                        attend.put("rollcall_sick", sickList);
                                        attend.put("rollcall_time",time );
                                        db.collection("Rollcall").add(attend).addOnCompleteListener(task2 -> {
                                            mMainList.smoothScrollToPosition(currentPosition + 1);
                                        });
                                    } else {
                                        Query query = db.collection("Rollcall").whereEqualTo("rollcall_time", time);
                                        query.get().addOnCompleteListener(task1 -> {
                                            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

                                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                                docId = documentSnapshot.getId();
                                            }
                                            Map<String, Object> attend = new HashMap<>();
                                            attend.put("rollcall_attend", attendList);
                                            attend.put("rollcall_absence", absenceList);
                                            db.collection("Rollcall").document(docId).update(attend).addOnCompleteListener(task2 -> {
                                                mMainList.smoothScrollToPosition(currentPosition + 1);
                                                if (currentPosition == (classMember.size() - 1)){
                                                    Toast.makeText(getApplicationContext(),"這已經是最後一位同學囉",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });

                                    }
                                    //---------------------
                                    //作者：左上角的天空
                                    //来源：CSDN
                                    //原文：https://blog.csdn.net/pszwll/article/details/82780150
                                    //版权声明：本文为博主原创文章，转载请附上博文链接！

                                }
                            });
                            absence = (Button) findViewById(R.id.card_absence);
                            absence.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(CallNameRollCall.this,"缺席",Toast.LENGTH_SHORT).show();
                                    count += 1;
                                    if (mMainList != null && mMainList.getChildCount() > 0) {
                                        try {
                                            currentPosition = ((RecyclerView.LayoutParams) mMainList.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                                            Log.i("currentPosition---->", String.valueOf(currentPosition));
                                        } catch (Exception e) {

                                        }
                                    }
                                    if (!absenceList.contains(studentId.get(currentPosition))) {

                                        absenceList.add(studentId.get(currentPosition));
                                    }
                                    if (attendList.contains(studentId.get(currentPosition))) {
                                        for (int i = 0; i < attendList.size(); i++) {
                                            if (attendList.get(i).equals(studentId.get(currentPosition))) {
                                                attendList.remove(i);
                                                i--;
                                            }
                                        }
                                    }
                                    Log.i("absenceList", absenceList.toString());
                                    if (count == 1) {
                                        time = new Date();
                                        Map<String, Object> absence = new HashMap<>();
                                        absence.put("class_id", classId);
                                        absence.put("rollcall_attend", attendList);
                                        absence.put("rollcall_absence", absenceList);
                                        absence.put("rollcall_casual", casualList);
                                        absence.put("rollcall_funeral", funeralList);
                                        absence.put("rollcall_late", lateList);
                                        absence.put("rollcall_offical", officalList);
                                        absence.put("rollcall_sick", sickList);
                                        absence.put("rollcall_time", time);
                                        db.collection("Rollcall").add(absence).addOnCompleteListener(task2 -> {
                                            mMainList.smoothScrollToPosition(currentPosition + 1);
                                        });
                                    } else {
                                        Query query = db.collection("Rollcall").whereEqualTo("rollcall_time", time);
                                        query.get().addOnCompleteListener(task1 -> {
                                            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

                                            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                                                docId = documentSnapshot.getId();
                                            }
                                            Map<String, Object> absence = new HashMap<>();
                                            absence.put("rollcall_attend", attendList);
                                            absence.put("rollcall_absence", absenceList);
                                            db.collection("Rollcall").document(docId).update(absence).addOnCompleteListener(task2 -> {
                                                mMainList.smoothScrollToPosition(currentPosition + 1);
                                                if (currentPosition == (classMember.size() - 1)){
                                                    Toast.makeText(getApplicationContext(),"這已經是最後一位同學囉",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        });
                                    }
                                }
                            });
                        });
                    }
                });


        finishBtn = (Button) findViewById(R.id.finishButton);
        finishBtn.setOnClickListener(view -> {
            //讀取dialog
            LayoutInflater lf = (LayoutInflater) CallNameRollCall.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_callname_rollcall,null);
            img_pgbar = (ImageView)vg.findViewById(R.id.img_pgbar);
            ad = (AnimationDrawable)img_pgbar.getDrawable();
            ad.start();
            android.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(CallNameRollCall.this);
            builder1.setView(vg);
            AlertDialog dialog = builder1.create();
            dialog.show();
            for (int i=0;i<classMember.size();i++){
                if (!absenceList.contains(classMember.get(i)) && !attendList.contains(classMember.get(i))){
                    attendList.add(classMember.get(i));
                }
            }
            Query query1 = db.collection("Rollcall").whereEqualTo("rollcall_time", time);
            query1.get().addOnCompleteListener(task1 -> {
                QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    docId = documentSnapshot.getId();
                }
                Map<String, Object> attend = new HashMap<>();
                attend.put("rollcall_attend", attendList);
                db.collection("Rollcall").document(docId).update(attend).addOnCompleteListener(task -> {
                    Map<String, Object> rollcall = new HashMap<>();
                    rollcall.put("rollcall_docId", docId);
                    db.collection("Class").document(classDoc).update(rollcall).addOnCompleteListener(task2 -> {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), RollcallResult.class);
                        intent.putExtra("class_id", classId);
                        intent.putExtra("class_doc", classDoc);
                        intent.putExtra("classDoc_id", docId);
                        intent.putExtra("request", "1");
                        startActivity(intent);
                        finish();
                    });

                });
            });

        });

        returnBtn = (ImageButton) findViewById(R.id.backIBtn);
        returnBtn.setOnClickListener(view -> {
            finish();
        });


    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }


}
