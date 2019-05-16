package com.example.kl.home;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Fragment_User_InforSetting extends Fragment {

    private static final String TAG = "Fragment_User_InforS";

    private FirebaseFirestore mFirestore;
    OnFragmentSelectedListener mCallback;//Fragment傳值

    private String teacher_email;
    private String teacherId;

    private EditText editTeacherName, editTeacherEmail, editTeacherOffice;
    private EditText editOTW1, editOTH11, editOTM11, editOTH12, editOTM12;
    private EditText editOTW2, editOTH21, editOTM21, editOTH22, editOTM22;
    private EditText editOTW3, editOTH31, editOTM31, editOTH32, editOTM32;
    private Button checkBtn;
    private Button backIBtn;
    private ImageView img_pgbar;
    private AnimationDrawable ad;


    private String editTeacherNameStr, editTeacherEmailStr, editTeacherOfficeStr;
    private String editOTW1Str, editOTH11Str, editOTM11Str, editOTH12Str, editOTM12Str;
    private String editOTW2Str, editOTH21Str, editOTM21Str, editOTH22Str, editOTM22Str;
    private String editOTW3Str, editOTH31Str, editOTM31Str, editOTH32Str, editOTM32Str;

    private ArrayList<String> updateOfficeTime = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle args;
        args = getArguments();
        teacher_email = args.getString("teacher_email");
        Log.d(TAG, "teacher_email :  " + teacher_email);
        return inflater.inflate(R.layout.fragment_user_inforsetting, container, false);

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mFirestore = FirebaseFirestore.getInstance();

        editTeacherName = (EditText) view.findViewById(R.id.editTeacherName);
        editTeacherEmail = (EditText) view.findViewById(R.id.editTeacherEmail);
        editTeacherOffice = (EditText) view.findViewById(R.id.editTeacherOffice);
        editOTW1 = (EditText) view.findViewById(R.id.editOTW1);
        editOTH11 = (EditText) view.findViewById(R.id.editOTH11);
        editOTM11 = (EditText) view.findViewById(R.id.editOTM11);
        editOTH12 = (EditText) view.findViewById(R.id.editOTH12);
        editOTM12 = (EditText) view.findViewById(R.id.editOTM12);

        editOTW2 = (EditText) view.findViewById(R.id.editOTW2);
        editOTH21 = (EditText) view.findViewById(R.id.editOTH21);
        editOTM21 = (EditText) view.findViewById(R.id.editOTM21);
        editOTH22 = (EditText) view.findViewById(R.id.editOTH22);
        editOTM22 = (EditText) view.findViewById(R.id.editOTM22);

        editOTW3 = (EditText) view.findViewById(R.id.editOTW3);
        editOTH31 = (EditText) view.findViewById(R.id.editOTH31);
        editOTM31 = (EditText) view.findViewById(R.id.editOTM31);
        editOTH32 = (EditText) view.findViewById(R.id.editOTH32);
        editOTM32 = (EditText) view.findViewById(R.id.editOTM32);

        checkBtn = (Button) view.findViewById(R.id.checkBtn);
        backIBtn = (Button) view.findViewById(R.id.backIBtn);

        getTeacherId();

        backIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateInfor(teacherId);

                mCallback.onFragmentSelected(teacherId, "toUserInfor");//fragment傳值

            }
        });


        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateInfor(teacherId);

                mCallback.onFragmentSelected(teacherId, "toUserInfor");//fragment傳值

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
                    //讀取dialog
                    LayoutInflater lf = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_user_inforsetting,null);
                    img_pgbar = (ImageView) vg.findViewById(R.id.img_pgbar);
                    ad = (AnimationDrawable)img_pgbar.getDrawable();
                    ad.start();
                    android.app.AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity().getApplicationContext());
                    builder1.setView(vg);
                    AlertDialog dialog = builder1.create();
                    dialog.show();
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
                    mFirestore.collection("Teacher").document(teacherId).update(attend).addOnCompleteListener(task1 -> {
                        dialog.dismiss();
                    });



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
