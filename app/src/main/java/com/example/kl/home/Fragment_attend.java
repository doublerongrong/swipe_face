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
import android.widget.Button;
import android.widget.Toast;

import com.example.kl.home.Adapter.Detail_AttendListAdapter;
import com.example.kl.home.Adapter.RollCallListAdapter;
import com.example.kl.home.Adapter.RollCallListAdapter3;
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

public class Fragment_attend extends Fragment {

    public Fragment_attend(){

    }
    private static final String TAG = "Fragment_attend";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerViewAttend,recyclerViewLate;
    private RollCallListAdapter rollCallListAdapterAttend;
    private RollCallListAdapter3 rollCallListAdapterLate;
    private List<Student> rollCallListAttend,rollCallListLate;
    private String classId,docId;
    private ArrayList<String> attendList,lateList,absenceList;
    private List<String>attendId,lateId;
    private int checkedItem = 0;
    private View view1;
    private int visible = 0;
    protected boolean isCreated = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        classId = bundle.getString("class_id");
        docId = bundle.getString("classDoc_id");
        Log.i("attendClass_id",classId);
        Log.i("attendClassDoc_id",docId);
        isCreated = true;

        return inflater.inflate(R.layout.fragment_attend, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"Fragment_attend: onCreate");
        rollCallListAttend = new ArrayList<>();
        rollCallListLate = new ArrayList<>();
        attendList = new ArrayList<>();
        lateList = new ArrayList<>();
        absenceList = new ArrayList<>();
        attendId = new ArrayList<>();
        lateId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        rollCallListAdapterLate = new RollCallListAdapter3(getActivity().getApplicationContext(),rollCallListLate,"遲到");
        rollCallListAdapterAttend = new RollCallListAdapter(getActivity().getApplicationContext(),rollCallListAttend,"出席");

        view1 = view;

        rollCallListAdapterAttend.setOnTransPageClickListener((student) -> {
            singleClick(view,student,docId);
        });
        rollCallListAdapterLate.setOnTransPageClickListener((student) -> {
            singleClick(view,student,docId);
        });
        if (visible == 0){
            //set Data
            setData();
            visible = 1;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"Fragment_attend: onPause");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"Fragment_attend: onResume");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"Fragment_attend: onDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"Fragment_attend: onStop");

    }

    private void setData() {
        DocumentReference docRef = db.collection("Rollcall").document(docId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            Rollcall rollcall = new Rollcall();
            rollcall = documentSnapshot.toObject(Rollcall.class);
            attendList = rollcall.getRollcall_attend();
            lateList = rollcall.getRollcall_late();
            absenceList = rollcall.getRollcall_absence();
            Log.d(TAG,"attend: "+attendList.toString());
            Log.d(TAG,"late: "+lateList.toString());

            //set AttendData
            for (int i=0;i<attendList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",attendList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()){
                        Student StudentList = new Student();
                        StudentList = documentSnapshot2.toObject(Student.class);
                        rollCallListAttend.add(StudentList);

                        rollCallListAdapterAttend.notifyDataSetChanged();
                        recyclerViewAttend = getView().findViewById(R.id.rollcall_list);
                        recyclerViewAttend.setHasFixedSize(true);
                        recyclerViewAttend.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewAttend.setAdapter(rollCallListAdapterAttend);
                    }

                });
            }

            //set LateData
            for (int i=0;i<lateList.size();i++){
                Query queryStudent = db.collection("Student").whereEqualTo("student_id",lateList.get(i));
                queryStudent.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot2 = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot2 : querySnapshot2.getDocuments()){
                        Student StudentList = new Student();
                        StudentList = documentSnapshot2.toObject(Student.class);
                        lateId.add(documentSnapshot2.getString("student_id"));
                        rollCallListLate.add(StudentList);

                        rollCallListAdapterLate.notifyDataSetChanged();
                        recyclerViewLate = getView().findViewById(R.id.rollcall_list2);
                        recyclerViewLate.setHasFixedSize(true);
                        recyclerViewLate.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewLate.setAdapter(rollCallListAdapterLate);
                    }
                });
            }
        });
        visible = 1;
    }


    //dialog修改狀態
    public void singleClick(View v,Student student,String rollcallId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("修改出席狀況");
        String[] cities = {"出席", "缺席", "遲到"};
        if (attendId.contains(student.getStudent_id())){
            checkedItem = 0;
        }
        if (lateId.contains(student.getStudent_id())){
            checkedItem = 2;
        }

        builder.setSingleChoiceItems(cities, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkedItem = which;
            }
        });
        //设置正面按钮
        builder.setPositiveButton("確認", (dialog, which) -> {
            //按下出席
            if (checkedItem == 0) {
                if (!attendList.contains(student.getStudent_id())) {
                    attendList.add(student.getStudent_id());
                    rollCallListAttend.add(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();
                }
                if (absenceList.contains(student.getStudent_id())) {
                    absenceList.remove(student.getStudent_id());
                }
                if (lateList.contains(student.getStudent_id())) {
                    lateList.remove(student.getStudent_id());
                    rollCallListLate.remove(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();                }

            }
            //缺席
            else if (checkedItem == 1) {
                if (!absenceList.contains(student.getStudent_id())) {
                    absenceList.add(student.getStudent_id());
                }
                if (attendList.contains(student.getStudent_id())) {
                    attendList.remove(student.getStudent_id());
                    rollCallListAttend.remove(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();                }
                if (lateList.contains(student.getStudent_id())) {
                    lateList.remove(student.getStudent_id());
                    rollCallListLate.remove(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();                }
            } else if (checkedItem == 2) {
                if (!lateList.contains(student.getStudent_id())) {
                    lateList.add(student.getStudent_id());
                    rollCallListLate.add(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();                }
                if (attendList.contains(student.getStudent_id())) {
                    attendList.remove(student.getStudent_id());
                    rollCallListAttend.remove(student);
                    rollCallListAdapterAttend.notifyDataSetChanged();
                    rollCallListAdapterLate.notifyDataSetChanged();                }
                if (absenceList.contains(student.getStudent_id())) {
                    absenceList.remove(student.getStudent_id());
                }
            }
            Map<String,Object> changeState = new HashMap<>();
            changeState.put("rollcall_attend",attendList);
            changeState.put("rollcall_absence",absenceList);
            changeState.put("rollcall_late",lateList);
            db.collection("Rollcall").document(rollcallId).update(changeState)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG,"uploadTaskIsFinish");
                            }
                        }
                    });
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
            Log.d(TAG,"Fragment_attend: !isCreated");
            return;
        }

        if (isVisibleToUser) {
            Log.d(TAG,"Fragment_attend: isVisibleToUser");
            setData();
            rollCallListAdapterAttend.notifyDataSetChanged();
            rollCallListAdapterLate.notifyDataSetChanged();
        }
        else{
            Log.d(TAG,"Fragment_attend: isNotVisibleToUser");
            rollCallListAttend.clear();
            rollCallListLate.clear();
            attendId.clear();
            lateId.clear();
            rollCallListAdapterAttend.notifyDataSetChanged();
            rollCallListAdapterLate.notifyDataSetChanged();
        }
    }
}
