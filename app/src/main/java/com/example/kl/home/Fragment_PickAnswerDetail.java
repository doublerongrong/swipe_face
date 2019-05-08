package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kl.home.Model.Bonus;
import com.example.kl.home.Model.Class;

import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;


public class Fragment_PickAnswerDetail extends Fragment {

    private FirebaseFirestore db;
    private String TAG = "Fragment_PickAnswerDetail";

    private int random;
    private String classId;
    private String type;
    private String student_name;
    private Class aClass;
    private Student student;
    private Performance performance;
    private Bonus bonus;

    private ArrayList<String> student_id;


    private TextView text_student_department;
    private TextView text_student_id;
    private TextView text_student_name;
    private ImageView img_student_photo;
    private Integer class_rdanswerbonus;
    private Button btNextone;
    private Button btCorrectAnswer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId = args.getString("classId");
        type = args.getString("type");
        Log.d(TAG, "classId:" + classId);//fragment傳值
//        Toast.makeText(getContext(), "現在課程資料庫代碼是:" + classId, Toast.LENGTH_LONG).show();
//        Toast.makeText(getContext(), "現在的模式是:" + type, Toast.LENGTH_LONG).show();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_answer_detail, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //init db
        db = FirebaseFirestore.getInstance();

        //init xml
        text_student_department = view.findViewById(R.id.text_student_department);
        text_student_id = view.findViewById(R.id.text_student_id);
        text_student_name = view.findViewById(R.id.text_student_name);
        img_student_photo = view.findViewById(R.id.img_student_photo);
        btNextone = view.findViewById(R.id.btNextone);
        btCorrectAnswer = view.findViewById(R.id.btCorrectAnswer);
//        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Leave_photo");

        //query bonus
        DocumentReference drQueryrdanswerbonus = db.collection("Class").document(classId);
        drQueryrdanswerbonus.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot dsQueryrdanswerbonus = task.getResult();
                if (dsQueryrdanswerbonus.exists()) {
                    class_rdanswerbonus = dsQueryrdanswerbonus.toObject(Class.class).getClass_rdanswerbonus();
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });


        switch (type) {
            case "random_pick":
                Log.d(TAG, "random_pick:");//fragment傳值
                setRandomPick();

                break;

            case "low_attendence":

                Log.d(TAG, "low_attendence:");
                setLowAttendence();
                break;

            case "low_interaction":
                Log.d(TAG, "low_interaction:");
                setLowInteraction();
                break;


        }


    }

    private void setRandomPick() {
        aClass = new Class();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                aClass = documentSnapshot.toObject(Class.class);
                student_id = aClass.getStudent_id();
                random = (int) (Math.random() * student_id.size());


                Log.d(TAG, "random size:" + student_id.size());
                Log.d(TAG, "random :" + random);

                db.collection("Student")
                        .whereEqualTo("student_id", student_id.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        student = document.toObject(Student.class);

                                        setView(student, aClass);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    private void setLowAttendence() {
        aClass = new Class();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                aClass = documentSnapshot.toObject(Class.class);
                Log.d(TAG, "limit0: " + aClass.getStudent_id().size());
                int limit;
                if(aClass.getStudent_id().size() >3){
                    limit = (int) Math.round(((aClass.getStudent_id().size()) * 3.) / 10);
                }
                else{
                    limit = aClass.getStudent_id().size();
                }
                //避免學生人數太少


                Log.d(TAG, "limit1: " + ((aClass.getStudent_id().size()) * 3.)/ 10);
                Log.d(TAG, "limit2: " + limit);

                student_id = new ArrayList<>();
                Log.d(TAG, "in lowAttnd : " + aClass.getClass_id());
                db.collection("Performance")
                        .whereEqualTo("class_id", aClass.getClass_id())
                        .orderBy("performance_totalAttendance")
                        .limit(limit)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        student_id.add(document.get("student_id").toString());
                                        Log.d(TAG, "in LowAtt for: " + student_id);
                                    }
                                    random = (int) (Math.random() * student_id.size());
                                    Log.d(TAG, "random size:" + student_id.size());
                                    Log.d(TAG, "random :" + random);

                                    db.collection("Student")
                                            .whereEqualTo("student_id", student_id.get(random))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            student = document.toObject(Student.class);

                                                            setView(student, aClass);
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }


        });
    }

    private void setLowInteraction() {
        aClass = new Class();
        db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                aClass = documentSnapshot.toObject(Class.class);
                Log.d(TAG, "limit0: " + aClass.getStudent_id().size());
                int limit;
                if(aClass.getStudent_id().size() >3){
                    limit = (int) Math.round(((aClass.getStudent_id().size()) * 3.) / 10);
                }
                else{
                    limit = aClass.getStudent_id().size();
                }
                //避免學生人數太少
                Log.d(TAG, "limit1: " + ((aClass.getStudent_id().size()) * 3. )/ 10);
                Log.d(TAG, "limit2: " + limit);

                student_id = new ArrayList<>();
                Log.d(TAG, "in lowAttnd : " + aClass.getClass_id());
                db.collection("Performance")
                        .whereEqualTo("class_id", aClass.getClass_id())
                        .orderBy("performance_totalBonus")
                        .limit(limit)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        student_id.add(document.get("student_id").toString());
                                        Log.d(TAG, "in LowAtt for: " + student_id);
                                    }
                                    random = (int) (Math.random() * student_id.size());
                                    Log.d(TAG, "random size:" + student_id.size());
                                    Log.d(TAG, "random :" + random);

                                    db.collection("Student")
                                            .whereEqualTo("student_id", student_id.get(random))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                                            student = document.toObject(Student.class);

                                                            setView(student, aClass);
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }


        });
    }


    private void setView(Student student, Class aClass) {

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("student_photo");

        String photoUrl = student.getStudent_id();
        StorageReference path = storageReference.child(photoUrl);

        text_student_department.setText(student.getStudent_department());
        text_student_id.setText(student.getStudent_id());
        text_student_name.setText(student.getStudent_name());
        Glide.with(getActivity().getApplicationContext())
                .load(path)
                .into(img_student_photo);

        btCorrectAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setPoint(student, aClass);
                setNextone(type);

            }
        });

        btNextone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setNextone(type);

            }
        });
    }

    private void setPoint(Student student, Class aClass) {
        performance = new Performance();
        bonus = new Bonus();
        Date date = new Date();
        bonus.setBonus_reason("點人答題");
        bonus.setBonus_time(date);
        bonus.setClass_id(aClass.getClass_id());
        bonus.setStudent_id(student.getStudent_id());
        Log.d(TAG, "here class_id:" + aClass.getClass_id());
        Log.d(TAG, "here student_id:" + student.getStudent_id());
        db.collection("Performance")
                .whereEqualTo("class_id", aClass.getClass_id())
                .whereEqualTo("student_id", student.getStudent_id())
                .get()
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "Flag1");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Flag2");
//                            task.getResult().getDocuments().get(0);
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String PerformanceId = document.getId();
                            Log.d(TAG, "PerformanceId:" + PerformanceId);

                            performance = document.toObject(Performance.class);
                            performance.setPerformance_totalBonus(performance.getPerformance_totalBonus() + class_rdanswerbonus);
                            db.collection("Performance").document(PerformanceId).set(performance);
                            Log.d(TAG, "come here");

                            db.collection("/Performance/" + PerformanceId + "/Bonus")
                                    .add(bonus)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getContext(), "該學生已獲得"+class_rdanswerbonus+"分", Toast.LENGTH_LONG).show();
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });


                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

    }

    private void setNextone(String type) {

//        card_nextone

        switch (type) {
            case "random_pick":
                setRandomPick();
                Log.d(TAG, "setNextone:random_pick:");//fragment傳值


                break;

            case "low_attendence":
                Log.d(TAG, "setNextone:low_attendence:");
                setLowAttendence();
                break;

            case "low_interaction":
                Log.d(TAG, "setNextone:low_interaction:");
                setLowInteraction();
                break;


        }

    }


}