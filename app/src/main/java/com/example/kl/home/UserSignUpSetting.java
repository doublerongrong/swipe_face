package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class UserSignUpSetting extends AppCompatActivity {

    private static final String TAG = "UserSignUpSetting";

    private FirebaseFirestore mFirestore;
    OnFragmentSelectedListener mCallback;//Fragment傳值

    private String teacher_email;
    private String teacherId;

    private EditText editTeacherName, editTeacherEmail, editTeacherOffice;
    private EditText editOTW1, editOTH11, editOTM11, editOTH12, editOTM12;
    private EditText editOTW2, editOTH21, editOTM21, editOTH22, editOTM22;
    private EditText editOTW3, editOTH31, editOTM31, editOTH32, editOTM32;
    private Button finishBtn;
    private Button skipBtn;


    private String editTeacherNameStr, editTeacherEmailStr, editTeacherOfficeStr;
    private String editOTW1Str, editOTH11Str, editOTM11Str, editOTH12Str, editOTM12Str;
    private String editOTW2Str, editOTH21Str, editOTM21Str, editOTH22Str, editOTM22Str;
    private String editOTW3Str, editOTH31Str, editOTM31Str, editOTH32Str, editOTM32Str;

    private ArrayList<String> updateOfficeTime = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up_setting);

        Bundle bundle = this.getIntent().getExtras();
        teacher_email = bundle.getString("teacherEmail");
        Log.d(TAG, "teacher_email :  " + teacher_email);

        mFirestore = FirebaseFirestore.getInstance();

        editTeacherName = (EditText) findViewById(R.id.editTeacherName);
        editTeacherEmail = (EditText) findViewById(R.id.editTeacherEmail);
        editTeacherOffice = (EditText) findViewById(R.id.editTeacherOffice);
        editOTW1 = (EditText) findViewById(R.id.editOTW1);
        editOTH11 = (EditText) findViewById(R.id.editOTH11);
        editOTM11 = (EditText) findViewById(R.id.editOTM11);
        editOTH12 = (EditText) findViewById(R.id.editOTH12);
        editOTM12 = (EditText) findViewById(R.id.editOTM12);

        editOTW2 = (EditText) findViewById(R.id.editOTW2);
        editOTH21 = (EditText) findViewById(R.id.editOTH21);
        editOTM21 = (EditText) findViewById(R.id.editOTM21);
        editOTH22 = (EditText) findViewById(R.id.editOTH22);
        editOTM22 = (EditText) findViewById(R.id.editOTM22);

        editOTW3 = (EditText) findViewById(R.id.editOTW3);
        editOTH31 = (EditText) findViewById(R.id.editOTH31);
        editOTM31 = (EditText) findViewById(R.id.editOTM31);
        editOTH32 = (EditText) findViewById(R.id.editOTH32);
        editOTM32 = (EditText) findViewById(R.id.editOTM32);

        finishBtn = (Button) findViewById(R.id.finishBtn);
        skipBtn = (Button) findViewById(R.id.skipBtn);

        getTeacherId();

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("teacher_email",teacher_email);
                i.setClass(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateInfor(teacherId);
                Intent i = new Intent();
                i.putExtra("teacher_email",teacher_email);
                i.setClass(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();

            }
        });


    }

    private void getTeacherId() {
        mFirestore.collection("Teacher").whereEqualTo("teacher_email", teacher_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                    Log.d(TAG, "Error :" + e.getMessage());
                }

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        teacherId = doc.getDocument().getId();
                        Log.d(TAG, "TEST ID : " + teacherId);
                        setInfor(teacherId);
                    }
                }

            }
        });
    }

    private void setInfor(String teacherId) {
        DocumentReference docRef = mFirestore.collection("Teacher").document(teacherId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Teacher aTeacher = document.toObject(Teacher.class);
                        editTeacherName.setText(aTeacher.getTeacher_name());
                        editTeacherEmail.setText(aTeacher.getTeacher_email());
                        editTeacherOffice.setText(aTeacher.getTeacher_office());

                        List<String> officeTime;
                        officeTime = aTeacher.getTeacher_officetime();
                        Log.d(TAG, "office Time : " + officeTime);

                        if (officeTime.size() == 9) {
                            editOTW1.setText(officeTime.get(0));
                            String[] splitFirst = officeTime.get(1).split(":");
                            editOTH11.setText(splitFirst[0]);
                            editOTM11.setText(splitFirst[1]);
                            String[] splitFirstS = officeTime.get(2).split(":");
                            editOTH12.setText(splitFirstS[0]);
                            editOTM12.setText(splitFirstS[1]);//第一組OfficeTime

                            editOTW2.setText(officeTime.get(3));
                            String[] splitSecond = officeTime.get(4).split(":");
                            editOTH21.setText(splitSecond[0]);
                            editOTM21.setText(splitSecond[1]);
                            String[] splitSecondS = officeTime.get(5).split(":");
                            editOTH22.setText(splitSecondS[0]);
                            editOTM22.setText(splitSecondS[1]);//第二組OfficeTime

                            editOTW3.setText(officeTime.get(6));
                            String[] splitThird = officeTime.get(7).split(":");
                            editOTH31.setText(splitThird[0]);
                            editOTM31.setText(splitThird[1]);
                            String[] splitThirdS = officeTime.get(8).split(":");
                            editOTH32.setText(splitThirdS[0]);
                            editOTM32.setText(splitThirdS[1]);//第三組OfficeTime
                        } else if (officeTime.size() == 6) {
                            editOTW1.setText(officeTime.get(0));
                            String[] splitFirst = officeTime.get(1).split(":");
                            editOTH11.setText(splitFirst[0]);
                            editOTM11.setText(splitFirst[1]);
                            String[] splitFirstS = officeTime.get(2).split(":");
                            editOTH12.setText(splitFirstS[0]);
                            editOTM12.setText(splitFirstS[1]);//第一組OfficeTime

                            editOTW2.setText(officeTime.get(3));
                            String[] splitSecond = officeTime.get(4).split(":");
                            editOTH21.setText(splitSecond[0]);
                            editOTM21.setText(splitSecond[1]);
                            String[] splitSecondS = officeTime.get(5).split(":");
                            editOTH22.setText(splitSecondS[0]);
                            editOTM22.setText(splitSecondS[1]);//第二組OfficeTime
                        } else if (officeTime.size() == 3) {
                            Log.d(TAG,"TEST 3 : 3");
                            Log.d(TAG,"TEST 3 : " + officeTime.get(0));
                            Log.d(TAG,"TEST 3 : " + officeTime.get(1));
                            Log.d(TAG,"TEST 3 : " + officeTime.get(2));
                            editOTW1.setText(officeTime.get(0));
                            String[] splitFirst = officeTime.get(1).split(":");
                            editOTH11.setText(splitFirst[0]);
                            editOTM11.setText(splitFirst[1]);
                            String[] splitFirstS = officeTime.get(2).split(":");
                            editOTH12.setText(splitFirstS[0]);
                            editOTM12.setText(splitFirstS[1]);//第一組OfficeTime

                        }

                    }
                }
            }
        });

    }

    private void upDateInfor(String teacherId) {
        DocumentReference docRef = mFirestore.collection("Teacher").document(teacherId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        Log.d(TAG, "TEST TeacherId :" + teacherId);
                        editTeacherNameStr = editTeacherName.getText().toString();
                        editTeacherEmailStr = editTeacherEmail.getText().toString();
                        editTeacherOfficeStr = editTeacherOffice.getText().toString();

                        editOTW1Str = editOTW1.getText().toString();
                        editOTH11Str = editOTH11.getText().toString();
                        editOTM11Str = editOTM11.getText().toString();
                        editOTH12Str = editOTH12.getText().toString();
                        editOTM12Str = editOTM12.getText().toString();//第一組 OfficeTime

                        if (editOTW1Str.length() > 0) {
                            updateOfficeTime.add(editOTW1Str);
                            updateOfficeTime.add(editOTH11Str + ":" + editOTM11Str);
                            updateOfficeTime.add(editOTH12Str + ":" + editOTM12Str);
                        }


                        editOTW2Str = editOTW2.getText().toString();
                        editOTH21Str = editOTH21.getText().toString();
                        editOTM21Str = editOTM21.getText().toString();
                        editOTH22Str = editOTH22.getText().toString();
                        editOTM22Str = editOTM22.getText().toString();//第二組 OfficeTime

                        if (editOTW2Str.length() > 0) {
                            updateOfficeTime.add(editOTW2Str);
                            updateOfficeTime.add(editOTH21Str + ":" + editOTM21Str);
                            updateOfficeTime.add(editOTH22Str + ":" + editOTM22Str);
                        }

                        editOTW3Str = editOTW3.getText().toString();
                        editOTH31Str = editOTH31.getText().toString();
                        editOTM31Str = editOTM31.getText().toString();
                        editOTH32Str = editOTH32.getText().toString();
                        editOTM32Str = editOTM32.getText().toString();//第三組 OfficeTime

                        if (editOTW3Str.length() > 0) {
                            updateOfficeTime.add(editOTW3Str);
                            updateOfficeTime.add(editOTH31Str + ":" + editOTM31Str);
                            updateOfficeTime.add(editOTH32Str + ":" + editOTM32Str);
                        }


                    }
                    Map<String, Object> attend = new HashMap<>();
                    attend.put("teacher_name", editTeacherNameStr);
                    attend.put("teacher_email", editTeacherEmailStr);
                    attend.put("teacher_office", editTeacherOfficeStr);
                    attend.put("teacher_officetime", updateOfficeTime);
                    mFirestore.collection("Teacher").document(teacherId).update(attend);



                }
            }
        });
    }

}
