package com.example.kl.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kl.home.Adapter.RollCallListAdapter;
import com.example.kl.home.Model.RollCallList;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_absence extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "Fragment_absence";
    private RecyclerView mMainList;
    private RollCallListAdapter rollCallListAdapter;
    private List<RollCallList> rollCallList;
    private String classId,docId;
    private List<String>absenceList,attendList,lateList;
    private int checkedItem = 0;
    private int studentIndex = 0;
    private List<String>absenceId;
    private View view1;
    protected boolean isCreated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        classId = bundle.getString("class_id");
        docId = bundle.getString("classDoc_id");

        Log.i("absenceClass_id",classId);
        Log.i("absenceClassDoc_id",docId);
        isCreated = true;
        return inflater.inflate(R.layout.fragment_absence, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rollCallList = new ArrayList<>();
        absenceList = new ArrayList<>();
        absenceId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        view1 = view;

    }

    //dialog修改狀態
    public void singleClick(View v,String student_id,String rollcallId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("修改出席狀況：");
        String[] cities = {"出席", "缺席", "遲到"};

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
                    if (!attendList.contains(student_id)) {
                        attendList.add(student_id);
                    }
                    if (absenceList.contains(student_id)) {
                        for (int i = 0; i < absenceList.size(); i++) {
                            if (absenceList.get(i).equals(student_id)){
                                absenceList.remove(i);
                                i--;
                            }
                        }
                    }
                    if (lateList.contains(student_id)) {
                        for (int i = 0; i < lateList.size(); i++) {
                            if (lateList.get(i).equals(student_id)){
                                lateList.remove(i);
                                i--;
                            }
                        }
                    }

                } else if (checkedItem == 1) {
                    Log.d(TAG, "TEST DIALOG" + "缺席");
                    if (!absenceList.contains(student_id)) {
                        absenceList.add(student_id);
                    }
                    if (attendList.contains(student_id)) {
                        for (int i = 0; i < attendList.size(); i++) {
                            if (attendList.get(i).equals(student_id)){
                                attendList.remove(i);
                                i--;
                            }
                        }
                    }
                    if (lateList.contains(student_id)) {
                        for (int i = 0; i < lateList.size(); i++) {
                            if (lateList.get(i).equals(student_id)){
                                lateList.remove(i);
                                i--;
                            }
                        }
                    }
                } else if (checkedItem == 2) {
                    Log.d(TAG, "TEST DIALOG" + "遲到");
                    if (!lateList.contains(student_id)) {
                        lateList.add(student_id);
                    }
                    if (attendList.contains(student_id)) {
                        for (int i = 0; i < attendList.size(); i++) {
                            if (attendList.get(i).equals(student_id)){
                                attendList.remove(i);
                                i--;
                            }
                        }
                    }
                    if (absenceList.contains(student_id)) {
                        for (int i = 0; i < absenceList.size(); i++) {
                            if (absenceList.get(i).equals(student_id)){
                                absenceList.remove(i);
                                i--;
                            }
                        }
                    }
                }
                Map<String,Object> changeState = new HashMap<>();
                changeState.put("rollcall_attend",attendList);
                changeState.put("rollcall_absence",absenceList);
                changeState.put("rollcall_late",lateList);
                db.collection("Rollcall").document(rollcallId).update(changeState).addOnSuccessListener(task -> {
                    rollCallList.clear();
                    absenceId.clear();
                    DocumentReference docRef = db.collection("Rollcall").document(docId);
                    docRef.get().addOnSuccessListener(documentSnapshot -> {
                        attendList = (ArrayList) documentSnapshot.get("rollcall_attend");
                        lateList = (ArrayList) documentSnapshot.get("rollcall_late");
                        absenceList = (ArrayList) documentSnapshot.get("rollcall_absence");
                        Log.i("absenceagain", absenceList.toString());

                        for (int i = 0; i < absenceList.size(); i++) {
                            Query queryStudent = db.collection("Student").whereEqualTo("student_id", absenceList.get(i));
                            queryStudent.get().addOnCompleteListener(task1 -> {
                                QuerySnapshot querySnapshot1 = task1.isSuccessful() ? task1.getResult() : null;
                                Log.i("queryagain", "work");
                                for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                                    RollCallList StudentList = new RollCallList(documentSnapshot2.getString("student_id"),
                                            documentSnapshot2.getString("student_department"), documentSnapshot2.getString("student_name"));
                                    absenceId.add(documentSnapshot2.getString("student_id"));
                                    rollCallList.add(StudentList);
                                    Log.i("rollcallagain",rollCallList.toString());
                                    Log.i("studentList",StudentList.toString());
                                    RollCallListAdapter rollCallListAdapter = new RollCallListAdapter(getActivity().getApplicationContext(),rollCallList,"缺席");
                                    rollCallListAdapter.notifyDataSetChanged();
                                    mMainList = (RecyclerView)getView().findViewById(R.id.rollacll_list2);
                                    mMainList.setHasFixedSize(true);
                                    mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    mMainList.setAdapter(rollCallListAdapter);
                                    Log.i("setAdapter","work");
                                    rollCallListAdapter.setOnClickMyButton(new RollCallListAdapter.onClickMyButton() {
                                        @Override
                                        public void myButton(int id) {
                                            studentIndex = id;
                                            Toast.makeText(getActivity(),Integer.toString(id),Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    rollCallListAdapter.setOnTransPageClickListener(new RollCallListAdapter.transPageListener() {
                                        @Override
                                        public void onTransPageClick() {
                                            singleClick(getView(),absenceId.get(studentIndex),docId);
                                        }

                                    });//Fragment換頁
                                }

                            });
                        }

                        if (!rollCallList.isEmpty()){

                        }
                    });

                });
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
            rollCallList.clear();
            absenceId.clear();
            DocumentReference docRef = db.collection("Rollcall").document(docId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                absenceList = (ArrayList)documentSnapshot.get("rollcall_absence");
                attendList = (ArrayList)documentSnapshot.get("rollcall_attend");
                lateList = (ArrayList)documentSnapshot.get("rollcall_late");
                Log.i("absence",absenceList.toString());

                for (int i=0;i<absenceList.size();i++){
                    Query queryStudent = db.collection("Student").whereEqualTo("student_id",absenceList.get(i));
                    queryStudent.get().addOnCompleteListener(task -> {
                        QuerySnapshot querySnapshot2 = task.isSuccessful() ? task.getResult() : null;
                        for (DocumentSnapshot documentSnapshot2 : querySnapshot2.getDocuments()){
                            RollCallList StudentList = new RollCallList(documentSnapshot2.getString("student_id"),
                                    documentSnapshot2.getString("student_department"),documentSnapshot2.getString("student_name"));
                            absenceId.add(documentSnapshot2.getString("student_id"));
                            rollCallList.add(StudentList);
                            RollCallListAdapter rollCallListAdapter = new RollCallListAdapter(getActivity().getApplicationContext(),rollCallList,"缺席");
                            mMainList = (RecyclerView)getView().findViewById(R.id.rollacll_list2);
                            mMainList.setHasFixedSize(true);
                            mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            mMainList.setAdapter(rollCallListAdapter);
                            Log.i("setAdapter","work");
                            rollCallListAdapter.setOnClickMyButton(new RollCallListAdapter.onClickMyButton() {
                                @Override
                                public void myButton(int id) {
                                    studentIndex = id;
                                    Toast.makeText(getActivity(),Integer.toString(id),Toast.LENGTH_SHORT).show();
                                }
                            });
                            rollCallListAdapter.setOnTransPageClickListener(new RollCallListAdapter.transPageListener() {
                                @Override
                                public void onTransPageClick() {
                                    singleClick(view1,absenceId.get(studentIndex),docId);
                                }

                            });//Fragment換頁

                        }
                    });


                }
                Log.i("rollcall",rollCallList.toString());


            });

        }
    }
}