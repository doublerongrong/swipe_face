package com.example.kl.home;

import android.app.AlertDialog;
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
import android.widget.Toast;

import com.example.kl.home.Adapter.RollCallListAdapter;
import com.example.kl.home.Adapter.RollCallListAdapter2;
import com.example.kl.home.Model.RollCallList;
import com.example.kl.home.Model.Rollcall;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private RecyclerView recyclerViewAbsence;
    private RollCallListAdapter2 rollCallListAdapterAbsence;
    private List<Student> rollCallListAbsence;
    private String classId,docId;
    private List<String>absenceList,attendList,lateList;
    private int checkedItem = 1;
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

        rollCallListAbsence = new ArrayList<>();
        absenceList = new ArrayList<>();
        absenceId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        view1 = view;

        rollCallListAdapterAbsence = new RollCallListAdapter2(getActivity().getApplicationContext(),rollCallListAbsence,"缺席");


        rollCallListAdapterAbsence.setOnTransPageClickListener((student) -> {
            singleClick(view,student,docId);
            rollCallListAdapterAbsence.notifyDataSetChanged();
        });

    }

    //dialog修改狀態
    public void singleClick(View v,Student student,String rollcallId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("修改出席狀況：");
        String[] cities = {"出席", "缺席", "遲到"};
        checkedItem=1;
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
                //按下出席
                if (checkedItem == 0) {
                    if (!attendList.contains(student.getStudent_id())) {
                        attendList.add(student.getStudent_id());

                    }
                    if (absenceList.contains(student.getStudent_id())) {
                        absenceList.remove(student.getStudent_id());
                        rollCallListAbsence.remove(student);
                        rollCallListAdapterAbsence.notifyDataSetChanged();

                    }
                    if (lateList.contains(student.getStudent_id())) {
                        lateList.remove(student.getStudent_id());
                    }

                }
                //按下缺席
                else if (checkedItem == 1) {
                    if (!absenceList.contains(student.getStudent_id())) {
                        absenceList.add(student.getStudent_id());
                        rollCallListAbsence.add(student);
                        rollCallListAdapterAbsence.notifyDataSetChanged();
                    }
                    if (attendList.contains(student.getStudent_id())) {
                        attendList.remove(student.getStudent_id());

                    }
                    if (lateList.contains(student.getStudent_id())) {
                        lateList.remove(student.getStudent_id());
                    }
                }
                //按下遲到
                else if (checkedItem == 2) {
                    if (!lateList.contains(student.getStudent_id())) {
                        lateList.add(student.getStudent_id());
                    }
                    if (attendList.contains(student.getStudent_id())) {
                       attendList.remove(student.getStudent_id());
                    }
                    if (absenceList.contains(student.getStudent_id())) {
                       absenceList.remove(student.getStudent_id());
                        rollCallListAbsence.remove(student);
                        rollCallListAdapterAbsence.notifyDataSetChanged();
                    }
                }
                Map<String,Object> changeState = new HashMap<>();
                changeState.put("rollcall_attend",attendList);
                changeState.put("rollcall_absence",absenceList);
                changeState.put("rollcall_late",lateList);
                db.collection("Rollcall").document(rollcallId).update(changeState)
                        .addOnCompleteListener(task -> Log.d(TAG,"uploadAbsenceFinish"));
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
            setData();
            rollCallListAdapterAbsence.notifyDataSetChanged();
        }else{
            rollCallListAbsence.clear();
            absenceId.clear();
            rollCallListAdapterAbsence.notifyDataSetChanged();
        }
    }

    private void setData() {
        DocumentReference docRef = db.collection("Rollcall").document(docId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Rollcall rollcall = new Rollcall();
            rollcall = documentSnapshot.toObject(Rollcall.class);
            attendList = rollcall.getRollcall_attend();
            lateList = rollcall.getRollcall_late();
            absenceList = rollcall.getRollcall_absence();
            Log.i("absence",absenceList.toString());

            for (int i=0;i<absenceList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",absenceList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot2 = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot2 : querySnapshot2.getDocuments()){
                        Student StudentList = new Student();
                        StudentList = documentSnapshot2.toObject(Student.class);
                        absenceId.add(documentSnapshot2.getString("student_id"));
                        rollCallListAbsence.add(StudentList);
                        recyclerViewAbsence = getView().findViewById(R.id.rollacll_list2);
                        recyclerViewAbsence.setHasFixedSize(true);
                        recyclerViewAbsence.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewAbsence.setAdapter(rollCallListAdapterAbsence);
                    }
                });
            }
        });
    }
}