package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateClassSt2 extends AppCompatActivity {
    private EditText editTexttotalPoints;
    private EditText editTextlateMinus;
    private EditText editTextabsenteeMinus;
    private EditText editTextEWtimes;
    private EditText editTextEWpoints;
    private EditText editTextanswerBouns;
    private EditText editTextrandomAnswerBonus;
    private Button sendoutBtn;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;
    private String TeacherEmail = "053792@mail.fju.edu.tw";
    private ArrayList<String> StudentList;
    private List<String> classList;
    private String class_id;

    private String TAG = "FLAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatclass_st2);

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        classList = new ArrayList<>();

        /*FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);*/
        StudentList = new ArrayList<>();


        editTexttotalPoints = (EditText) findViewById(R.id.editTexttotalPoints);
        editTextlateMinus = (EditText) findViewById(R.id.editTextlateMinus);
        editTextabsenteeMinus = (EditText) findViewById(R.id.editTextAbsenteeMinus);
        editTextEWtimes = (EditText) findViewById(R.id.editTextEWTimes);
        editTextEWpoints = (EditText) findViewById(R.id.editTextEWPoints);
        editTextanswerBouns = (EditText) findViewById(R.id.editTextAnswerBonus);
        editTextrandomAnswerBonus = (EditText) findViewById(R.id.editTextRDBonus);
        sendoutBtn = (Button) findViewById(R.id.ButtonSendout);
        sendoutBtn.setOnClickListener(v -> {

            create();

        });

    }

    private void create() {
        Bundle bundle = getIntent().getExtras();
        String classname = bundle.getString("classnameB");
        String classyear = bundle.getString("classyearB");

        class_id = "C";
        String classidOri = UUID.randomUUID().toString().replace("-", "");
        for(int i = 0; i < 10; i++){
            class_id += classidOri.charAt(i);
        }


        Integer totalpoints = Integer.parseInt(editTexttotalPoints.getText().toString().trim());
        Integer lateminus = Integer.parseInt(editTextlateMinus.getText().toString().trim());
        Integer absenteeminus = Integer.parseInt(editTextabsenteeMinus.getText().toString().trim());
        Integer EWtimes = Integer.parseInt(editTextEWtimes.getText().toString().trim());
        Integer EWpoints = Integer.parseInt(editTextEWpoints.getText().toString().trim());
        Integer answerbonus = Integer.parseInt(editTextrandomAnswerBonus.getText().toString().trim());
        Integer randomanserbonus = Integer.parseInt(editTextrandomAnswerBonus.getText().toString().trim());
        /*FirebaseUser user = mAuth.getCurrentUser();
        String teacherid = user.getEmail().toString();*/
        //抓老師id
        Map<String, Object> uploadMap = new HashMap<>();
        uploadMap.put("group_state",false);
        uploadMap.put("class_id", class_id);
        uploadMap.put("class_name", classname);
        uploadMap.put("class_year", classyear);
        uploadMap.put("teacher_email", TeacherEmail);
        uploadMap.put("class_totalpoints", totalpoints);
        uploadMap.put("class_lateminus", lateminus);
        uploadMap.put("class_absenteeminus", absenteeminus);
        uploadMap.put("class_ewtimes", EWtimes);
        uploadMap.put("class_ewpoints", EWpoints);
        uploadMap.put("class_answerbonus", answerbonus);
        uploadMap.put("class_rdanswerbonus", randomanserbonus);
        uploadMap.put("student_id", StudentList);
        uploadMap.put("group_state_go",false);
        Log.d(TAG, "TEST CREAT");
        mFirestore.collection("Class").add(uploadMap).addOnSuccessListener(a -> {


            Log.d(TAG, "TEST CREAT Success");
            setCalss(class_id, TeacherEmail);//要改抓user

            Intent intent = new Intent();
            intent.setClass(CreateClassSt2.this, MainActivity.class);
            startActivity(intent);
            finish();

            Toast.makeText(CreateClassSt2.this, "創建成功!", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            String error = e.getMessage();
            Toast.makeText(CreateClassSt2.this, "上傳失敗", Toast.LENGTH_SHORT).show();

        });
    }

    public void setCalss(String class_id, String teacher_email){
        Query query = mFirestore.collection("Teacher").whereEqualTo("teacher_email",teacher_email);
        query.get().addOnCompleteListener(task1 -> {
            QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult(): null;
            for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                Log.i("documentUrl",documentSnapshot.getId());
                String docId = documentSnapshot.getId();
                classList = (ArrayList)documentSnapshot.get("class_id");
                if (!classList.contains(class_id)){

                    classList.add(class_id);
                }

                Map<String, Object> attend = new HashMap<>();
                attend.put("class_id",classList);
               mFirestore.collection("Teacher").document(docId).update(attend);


            }
        });






    }
}
