package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Model.Class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Activity_ScoreSetting extends AppCompatActivity {
    private TextView textViewClassName;
    private TextView textViewtotalPoints;
    private TextView textViewlateMinus;
    private TextView textViewabsenteeMinus;
    private TextView textViewEWtimes;
    private TextView textViewEWpoints;
    private TextView textViewanswerBouns;
    private TextView textViewrandomAnswerBonus;
    private Button editBtn;
    private ImageButton imageCancel;

    private String classId;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private String TAG = "FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoresetting);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");

        textViewClassName = (TextView) findViewById(R.id.textViewClassName);
        textViewtotalPoints = (TextView) findViewById(R.id.editTexttotalPoints);
        textViewlateMinus = (TextView) findViewById(R.id.editTextlateMinus);
        textViewabsenteeMinus = (TextView) findViewById(R.id.editTextAbsenteeMinus);
        textViewEWtimes = (TextView) findViewById(R.id.editTextEWTimes);
        textViewEWpoints = (TextView) findViewById(R.id.editTextEWPoints);
        textViewanswerBouns = (TextView) findViewById(R.id.editTextAnswerBonus);
        textViewrandomAnswerBonus = (TextView) findViewById(R.id.editTextRDBonus);
        editBtn = (Button) findViewById(R.id.ButtonEdit);
        imageCancel = (ImageButton) findViewById(R.id.imageCancel);

        DocumentReference docRef = mFirestore.collection("Class").document(classId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Class setClass = document.toObject(Class.class);
                        textViewClassName.setText(setClass.getClass_name());
                        textViewtotalPoints.setText(setClass.getClass_totalpoints().toString());
                        textViewlateMinus.setText(setClass.getClass_lateminus().toString());
                        textViewabsenteeMinus.setText(setClass.getClass_absenteeminus().toString());
                        textViewEWtimes.setText(setClass.getClass_ewtimes().toString());
                        textViewEWpoints.setText(setClass.getClass_ewpoints().toString());
                        textViewanswerBouns.setText(setClass.getClass_answerbonus().toString());
                        textViewrandomAnswerBonus.setText(setClass.getClass_rdanswerbonus().toString());

                        imageCancel.setOnClickListener(v -> {

                            finish();


                        });

                        editBtn.setOnClickListener(v -> {
                            Intent intent = new Intent();
                            intent.setClass(Activity_ScoreSetting.this, Activity_ScoreSetting_Edit.class);
                            Bundle bundle7 = new Bundle();
                            bundle7.putString("classId", classId);
                            intent.putExtras(bundle7);
                            startActivity(intent);


                        });

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
