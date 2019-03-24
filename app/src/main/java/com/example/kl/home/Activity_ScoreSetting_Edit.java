package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kl.home.Model.Class;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Activity_ScoreSetting_Edit extends AppCompatActivity {
    private TextView textViewClassName;
    private EditText editTexttotalPoints;
    private EditText editTextlateMinus;
    private EditText editTextabsenteeMinus;
    private EditText editTextEWtimes;
    private EditText editTextEWpoints;
    private EditText editTextanswerBouns;
    private EditText editTextrandomAnswerBonus;
    private Button updateBtn;

    private String classId;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    private String TAG = "FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoresetting_edit);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");

        textViewClassName = (TextView) findViewById(R.id.textViewClassName);
        editTexttotalPoints = (EditText) findViewById(R.id.editTexttotalPoints);
        editTextlateMinus = (EditText) findViewById(R.id.editTextlateMinus);
        editTextabsenteeMinus = (EditText) findViewById(R.id.editTextAbsenteeMinus);
        editTextEWtimes = (EditText) findViewById(R.id.editTextEWTimes);
        editTextEWpoints = (EditText) findViewById(R.id.editTextEWPoints);
        editTextanswerBouns = (EditText) findViewById(R.id.editTextAnswerBonus);
        editTextrandomAnswerBonus = (EditText) findViewById(R.id.editTextRDBonus);
        updateBtn = (Button) findViewById(R.id.ButtonUpdate);

        DocumentReference docRef = mFirestore.collection("Class").document(classId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Class setClass = document.toObject(Class.class);
                        textViewClassName.setText(setClass.getClass_name());
                        editTexttotalPoints.setText(setClass.getClass_totalpoints().toString());
                        editTextlateMinus.setText(setClass.getClass_lateminus().toString());
                        editTextabsenteeMinus.setText(setClass.getClass_absenteeminus().toString());
                        editTextEWtimes.setText(setClass.getClass_ewtimes().toString());
                        editTextEWpoints.setText(setClass.getClass_ewtimes().toString());
                        editTextanswerBouns.setText(setClass.getClass_answerbonus().toString());
                        editTextrandomAnswerBonus.setText(setClass.getClass_rdanswerbonus().toString());

                        updateBtn.setOnClickListener(v -> {

                            updateScore();

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

    public void updateScore() {

        Integer totalpoints = Integer.parseInt(editTexttotalPoints.getText().toString().trim());
        Integer lateminus = Integer.parseInt(editTextlateMinus.getText().toString().trim());
        Integer absenteeminus = Integer.parseInt(editTextabsenteeMinus.getText().toString().trim());
        Integer EWtimes = Integer.parseInt(editTextEWtimes.getText().toString().trim());
        Integer EWpoints = Integer.parseInt(editTextEWpoints.getText().toString().trim());
        Integer answerbonus = Integer.parseInt(editTextrandomAnswerBonus.getText().toString().trim());
        Integer randomanserbonus = Integer.parseInt(editTextrandomAnswerBonus.getText().toString().trim());

        DocumentReference leaveCheckRef = mFirestore.collection("Class").document("gM3rxBwnztyaoFjxRv6O");
        leaveCheckRef
                .update("class_totalpoints", totalpoints, "class_lateminus", lateminus,
                        "class_absenteeminus", absenteeminus, "class_ewtimes", EWtimes, "class_ewpoints", EWpoints,
                        "class_answerbonus", answerbonus, "class_rdanswerbonus", randomanserbonus)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                        Intent intent = new Intent();
                        intent.setClass(Activity_ScoreSetting_Edit.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
