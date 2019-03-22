package com.example.kl.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Activity_StudentDetail extends AppCompatActivity {

    private static final String TAG = "SDLOG";
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private ImageView imageViewStudent;
    private TextView textViewStudentName;
    private TextView textViewStudentId;
    private TextView textViewStudentDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);


        Bundle bundle = getIntent().getExtras();
        String studentId = bundle.getString("PassStudentId");
        String student_id = bundle.getString("PassStudent_id");
        Student student = (Student) getIntent().getSerializableExtra("PassStudent");
        String class_id = bundle.getString("PassClass_id");
        Log.d(TAG,"TEST B" + bundle);

        imageViewStudent = (ImageView) findViewById(R.id.imageViewStudent);
        textViewStudentName = (TextView) findViewById(R.id.textViewStudentName);
        textViewStudentId = (TextView) findViewById(R.id.textViewStudentId);
        textViewStudentDepartment = (TextView) findViewById(R.id.textViewStudentDepartment);

        setStudentInfor(studentId);//設定學生資料

        FragmentTabHost tabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);

        tabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //1
        Bundle arg = new Bundle();
        arg.putString("PassStudentId", studentId);
        arg.putString("PassStudent_Id", student_id);
        arg.putString("PassClass_Id", class_id);

        tabHost.addTab(tabHost.newTabSpec("Performance")
                        .setIndicator("課堂表現"),
                Fragment_SD_Performance.class,
                arg);
        //2
        tabHost.addTab(tabHost.newTabSpec("Attendance")
                        .setIndicator("出席狀況"),
                Fragment_SD_Attendance.class,
                arg);
        //3
        tabHost.addTab(tabHost.newTabSpec("Leave")
                        .setIndicator("請假紀錄"),
                Fragment_SD_Leave.class,
                arg);


    }

    private void setStudentInfor(String studentId){
        DocumentReference docRef = mFirestore.collection("Student").document(studentId);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("student_photo");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Student setStudent = document.toObject(Student.class);

                        String photoUrl = setStudent.getStudent_id();
                        StorageReference path = storageReference.child(photoUrl);
                        Log.d("TEST",path.toString());
                        Glide.with(Activity_StudentDetail.this)
                                .load(path)
                                .into(imageViewStudent);
                        textViewStudentName.setText(setStudent.getStudent_name());
                        textViewStudentId.setText(setStudent.getStudent_id());
                        textViewStudentDepartment.setText(setStudent.getStudent_department());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
