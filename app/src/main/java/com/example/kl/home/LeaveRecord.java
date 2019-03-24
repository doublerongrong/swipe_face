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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;

public class LeaveRecord extends AppCompatActivity {

    private static final String TAG = "Ｆirelog";

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
                        String leaveUpdloadDate = myFmt2.format(leave.getLeave_uploaddate());

                        class_name.setText(leave.getClass_name());
                        leave_reason.setText(leave.getLeave_reason());
                        leave_check.setText(leave.getLeave_check());
                        student_name.setText(leave.getStudent_name());
                        leave_date.setText(leave.getLeave_date());
                        leave_uploaddate.setText(leaveUpdloadDate);
                        leave_content.setText(leave.getLeave_content());
                        String photoUrl = leave.getLeave_photoUrl();

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

}