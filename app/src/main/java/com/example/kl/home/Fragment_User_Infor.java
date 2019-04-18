package com.example.kl.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Student;
import com.example.kl.home.Model.Teacher;
import com.example.kl.home.OnFragmentSelectedListener;
import com.example.kl.home.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Fragment_User_Infor extends Fragment {

    private static final String TAG = "Fragment_User_Infor";

    private FirebaseFirestore mFirestore;

    private TextView ViewTeacherName;
    private TextView ViewTeacherEmail;
    private TextView ViewTeacherOffice;
    private TextView ViewTeacherOT1;
    private TextView ViewTeacherOT2;
    private TextView ViewTeacherOT3;
    private Button editBtn;

    OnFragmentSelectedListener mCallback;//Fragment傳值

    private String teacher_email;
    private String teacherId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args ;
        args = getArguments();
        teacher_email = args.getString("teacher_email");
        Log.d(TAG,"teacher_email :  " + teacher_email);
        return inflater.inflate(R.layout.fragment_user_infor, container, false);

    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();

        ViewTeacherName = (TextView) view.findViewById(R.id.ViewTeacherName);
        ViewTeacherEmail = (TextView) view.findViewById(R.id.ViewTeacherEmail);
        ViewTeacherOffice = (TextView) view.findViewById(R.id.ViewTeacherOffice);
        ViewTeacherOT1 = (TextView) view.findViewById(R.id.ViewTeacherOT1);
        ViewTeacherOT2 = (TextView) view.findViewById(R.id.ViewTeacherOT2);
        ViewTeacherOT3 = (TextView) view.findViewById(R.id.ViewTeacherOT3);
        editBtn = (Button) view.findViewById(R.id.editBtn);

        setInfor();

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onFragmentSelected(teacherId, "toUserInforSetting");//fragment傳值
            }
        });







    }

    private void setInfor(){
        mFirestore.collection("Teacher").whereEqualTo("teacher_email", teacher_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                    Log.d(TAG, "Error :" + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        teacherId = doc.getDocument().getId();
                        Log.d(TAG,"TEST ID : " + teacherId);
                        printInfor(teacherId);
                    }
                }

            }
        });

    }

    private void printInfor(String teacherId){
        DocumentReference docRef = mFirestore.collection("Teacher").document(teacherId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Teacher aTeacher = document.toObject(Teacher.class);
                        ViewTeacherName.setText(aTeacher.getTeacher_name());
                        ViewTeacherEmail.setText(aTeacher.getTeacher_email());
                        ViewTeacherOffice.setText(aTeacher.getTeacher_office());

                        List<String> officeTime ;
                        officeTime = aTeacher.getTeacher_officetime();
                        Log.d(TAG,"office Time : " + officeTime);

                        if(officeTime.size() == 9){
                            String OT1 = "星期" + officeTime.get(0) + " " + officeTime.get(1) + " - " + officeTime.get(2);
                            ViewTeacherOT1.setText(OT1);
                            String OT2 = "星期" + officeTime.get(3) + " " + officeTime.get(4) + " - " + officeTime.get(5);
                            ViewTeacherOT2.setText(OT2);
                            String OT3 = "星期" + officeTime.get(6) + " " + officeTime.get(7) + " - " + officeTime.get(8);
                            ViewTeacherOT3.setText(OT3);
                        }
                        else if(officeTime.size() == 6){
                            String OT1 = "星期" + officeTime.get(0) + " " + officeTime.get(1) + " - " + officeTime.get(2);
                            ViewTeacherOT1.setText(OT1);
                            String OT2 = "星期" + officeTime.get(3) + " " + officeTime.get(4) + " - " + officeTime.get(5);
                            ViewTeacherOT2.setText(OT2);
                        }
                        else if(officeTime.size() == 3){
                            String OT1 = "星期" + officeTime.get(0) + " " + officeTime.get(1) + " - " + officeTime.get(2);
                            ViewTeacherOT1.setText(OT1);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }



}
