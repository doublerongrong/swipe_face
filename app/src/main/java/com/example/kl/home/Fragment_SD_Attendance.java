package com.example.kl.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Adapter.Detail_AttendListAdapter;
import com.example.kl.home.Model.Attendance;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Rollcall;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_SD_Attendance extends Fragment {

    private static final String TAG = "Fragment_SD_Attend";

    private String performanceId;
    private String studentId;
    private String student_id;
    private String class_id;

    private int lateMinus;
    private int absenteeMinus;
    private int AttendPoints;
    private TextView textViewAttendPoints;
    OnFragmentSelectedListener mCallback;//Fragment傳值

    private List<String> attendList, absenceList, lateList;//更改rollcall狀態

    private Attendance attendance;
    private List<String> rollcallList, stateList;

    private Detail_AttendListAdapter attendanceListAdapter;
    private List<Attendance> attendanceList;
    private RecyclerView attendance_list;

    private FirebaseFirestore mFirestore;
    public Context context;
    private int checkedItem = 0;
    private int classIndex = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arg = new Bundle();//fragment傳值
        arg = getArguments();//fragment傳值
        studentId = arg.getString("PassStudentId");
        student_id = arg.getString("PassStudent_Id");
        class_id = arg.getString("PassClass_Id");

        absenceList = new ArrayList<>();
        attendList = new ArrayList<>();
        lateList = new ArrayList<>();
        rollcallList = new ArrayList<>();
        stateList = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__sd__attendance, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();

        attendanceList = new ArrayList<>();
        attendanceListAdapter = new Detail_AttendListAdapter(getActivity().getApplicationContext(), attendanceList);

        textViewAttendPoints = (TextView) view.findViewById(R.id.textViewAttendPoints);
        attendance_list = (RecyclerView) view.findViewById(R.id.student_detail_Attendlist);
        attendance_list.setHasFixedSize(true);
        attendance_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        attendance_list.setAdapter(attendanceListAdapter);
        setScore(class_id, student_id);

        ListAttendance(class_id, view);

    }


    private void setAttendance(Rollcall rollcall, String rollcallId) {
        attendance = new Attendance();
        attendance.setStudent_id(student_id);
        attendance.setAttendance_time(rollcall.getRollcall_time());
        attendance.setRollcallId(rollcallId);

        if (rollcall.getRollcall_absence().contains(student_id)) {
            attendance.setAttendance_status("缺席");

            stateList.add("缺席");
        } else if (rollcall.getRollcall_attend().contains(student_id)) {
            attendance.setAttendance_status("出席");

            stateList.add("出席");
        } else if (rollcall.getRollcall_late().contains(student_id)) {
            attendance.setAttendance_status("遲到");

            stateList.add("遲到");
        } else if (rollcall.getRollcall_casual().contains(student_id)) {
            attendance.setAttendance_status("請假");
        } else if (rollcall.getRollcall_funeral().contains(student_id)) {
            attendance.setAttendance_status("請假");
        } else if (rollcall.getRollcall_offical().contains(student_id)) {
            attendance.setAttendance_status("請假");
        } else if (rollcall.getRollcall_sick().contains(student_id)) {
            attendance.setAttendance_status("請假");
        }

    }

    private void setScore(String class_id, String student_id) {
        mFirestore.collection("Performance").
                whereEqualTo("class_id", class_id).
                whereEqualTo("student_id", student_id).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {

                            Log.d(TAG, "Error :" + e.getMessage());
                        }
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                performanceId = doc.getDocument().getId();
                                Performance performance = doc.getDocument().toObject(Performance.class);
                                AttendPoints = performance.getPerformance_totalAttendance();
                                textViewAttendPoints.setText(Integer.toString(AttendPoints));//改成更變後，放list後
                            }
                        }

                    }
                });
    }

    public void ListAttendance(String class_id, View view) {
        mFirestore.collection("Class").whereEqualTo("class_id", class_id).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Error :" + e.getMessage());
                        }
                        Class aClass = documentSnapshots.getDocuments().get(0).toObject(Class.class);
                        lateMinus = aClass.getClass_lateminus();
                        absenteeMinus = aClass.getClass_absenteeminus();

                        Log.d(TAG, "in class " + class_id);
                        Log.d(TAG, "in class " + Integer.toString(lateMinus));
                        Log.d(TAG, "in class " + Integer.toString(absenteeMinus));
                        mFirestore.collection("Rollcall").

                                whereEqualTo("class_id", class_id).

                                addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(QuerySnapshot
                                                                documentSnapshots, FirebaseFirestoreException e) {
                                        if (e != null) {

                                            Log.d(TAG, "Error :" + e.getMessage());
                                        }
                                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                                String rollcallId = doc.getDocument().getId();
                                                rollcallList.add(rollcallId);
                                                Log.d(TAG, "docId  " + rollcallId);
                                                Rollcall rollcall = doc.getDocument().toObject(Rollcall.class);
                                                setAttendance(rollcall, rollcallId);
                                                attendanceList.add(attendance);
                                                attendanceListAdapter.notifyDataSetChanged();

                                            }
                                        }
                                    }
                                });
                    }

                });

        attendanceListAdapter.setOnClickMyButton(new Detail_AttendListAdapter.onClickMyButton() {
            @Override
            public void myButton(int id) {
                classIndex = id;
                Toast.makeText(getActivity(), Integer.toString(id), Toast.LENGTH_SHORT).show();
            }
        });
        attendanceListAdapter.setOnTransPageClickListener(new Detail_AttendListAdapter.transPageListener() {
            @Override
            public void onTransPageClick() {
                Log.d(TAG, "onTransPageClickTEST  " + student_id);
                Log.d(TAG, "onTransPageClickTEST  " + rollcallList.get(classIndex));
                Log.d(TAG, "onTransPageClickTEST  " + rollcallList);
                Log.d(TAG, "onTransPageClickTEST  " + stateList);

                singleClick(view, rollcallList.get(classIndex), stateList.get(classIndex));

            }

        });//Fragment換頁
    }

    public void singleClick(View v, String rollcallId, String state) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(getResources().getString(R.string.updateState));
        String[] cities = {"出席", "缺席", "遲到"};
        if (state.equals("出席")) {
            checkedItem = 0;
        } else if (state.equals("缺席")) {
            checkedItem = 1;
        } else {
            checkedItem = 2;
        }

        builder.setSingleChoiceItems(cities, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem = which;
            }
        });
        //设置正面按钮
        builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkedItem == 0) {
                    Log.d(TAG, "TEST DIALOG" + "出席");
                    Log.d(TAG, "TEST DIALOG" + rollcallId + student_id);
                    changePoints(state, "出席", v);
                    addAttend(rollcallId, student_id);

                } else if (checkedItem == 1) {
                    Log.d(TAG, "TEST DIALOG" + "缺席");
                    Log.d(TAG, "TEST DIALOG" + rollcallId + student_id);
                    addAbsense(rollcallId, student_id);
                    changePoints(state, "缺席", v);
                } else if (checkedItem == 2) {
                    Log.d(TAG, "TEST DIALOG" + "遲到");
                    Log.d(TAG, "TEST DIALOG" + rollcallId + student_id);
                    addLate(rollcallId, student_id);
                    changePoints(state, "遲到", v);
                }

            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addAttend(String rollcallId, String student_id) {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "TEST DB" + rollcallId);
                        Log.d(TAG, "TEST DB" + student_id);
                        attendList = (ArrayList) document.get("rollcall_attend");
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        lateList = (ArrayList) document.get("rollcall_late");
                        Log.d(TAG, "TEST DB" + attendList);
                        if (!attendList.contains(student_id)) {

                            attendList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        if (lateList.contains(student_id)) {
                            for (int i = 0; i < lateList.size(); i++) {
                                if (lateList.get(i).equals(student_id)) {
                                    lateList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_attend", attendList);
                        attend.put("rollcall_absence", absenceList);
                        attend.put("rollcall_late", lateList);
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

    public void addAbsense(String rollcallId, String student_id) {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        attendList = (ArrayList) document.get("rollcall_attend");
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        lateList = (ArrayList) document.get("rollcall_late");
                        if (!absenceList.contains(student_id)) {

                            absenceList.add(student_id);
                        }
                        if (attendList.contains(student_id)) {
                            for (int i = 0; i < attendList.size(); i++) {
                                if (attendList.get(i).equals(student_id)) {
                                    attendList.remove(i);
                                    i--;
                                }
                            }
                        }
                        if (lateList.contains(student_id)) {
                            for (int i = 0; i < lateList.size(); i++) {
                                if (lateList.get(i).equals(student_id)) {
                                    lateList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_attend", attendList);
                        attend.put("rollcall_absence", absenceList);
                        attend.put("rollcall_late", lateList);
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

    public void addLate(String rollcallId, String student_id) {
        DocumentReference docRef = mFirestore.collection("Rollcall").document(rollcallId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        attendList = (ArrayList) document.get("rollcall_attend");
                        absenceList = (ArrayList) document.get("rollcall_absence");
                        lateList = (ArrayList) document.get("rollcall_late");
                        if (!lateList.contains(student_id)) {

                            lateList.add(student_id);
                        }
                        if (absenceList.contains(student_id)) {
                            for (int i = 0; i < absenceList.size(); i++) {
                                if (absenceList.get(i).equals(student_id)) {
                                    absenceList.remove(i);
                                    i--;
                                }
                            }
                        }
                        if (attendList.contains(student_id)) {
                            for (int i = 0; i < attendList.size(); i++) {
                                if (attendList.get(i).equals(student_id)) {
                                    attendList.remove(i);
                                    i--;
                                }
                            }
                        }
                        Map<String, Object> attend = new HashMap<>();
                        attend.put("rollcall_attend", attendList);
                        attend.put("rollcall_absence", absenceList);
                        attend.put("rollcall_late", lateList);
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

    public void changePoints(String Oristatus, String check, View view) {
        if (Oristatus.equals("出席") && check.equals("缺席")) {
            AttendPoints -= absenteeMinus;

            Log.d(TAG, "改分數:  " + "出席變缺席");

        } else if (Oristatus.equals("出席") && check.equals("遲到")) {
            AttendPoints -= lateMinus;

            Log.d(TAG, "改分數:  " + "出席變遲到");
        } else if (Oristatus.equals("遲到") && check.equals("出席")) {
            AttendPoints += lateMinus;

            Log.d(TAG, "改分數:  " + "遲到變出席");
        } else if (Oristatus.equals("遲到") && check.equals("缺席")) {
            AttendPoints = AttendPoints + lateMinus - absenteeMinus;

            Log.d(TAG, "改分數:  " + "遲到變缺席");
        } else if (Oristatus.equals("缺席") && check.equals("遲到")) {
            AttendPoints = AttendPoints + absenteeMinus - lateMinus;

            Log.d(TAG, "改分數:  " + "缺席變遲到");
        } else if (Oristatus.equals("缺席") && check.equals("出席")) {
            AttendPoints += absenteeMinus;

            Log.d(TAG, "改分數:  " + "缺席變出席");
        }
        DocumentReference ChangePointRef = mFirestore.collection("Performance").document(performanceId);
        ChangePointRef
                .update("performance_totalAttendance", AttendPoints)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setScore(class_id, student_id);
                        attendanceList.clear();
                        ListAttendance(class_id, view);
                        stateList.clear();
                        rollcallList.clear();
                    }
                });


    }
}