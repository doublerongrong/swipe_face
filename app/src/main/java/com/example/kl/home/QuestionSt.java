package com.example.kl.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QuestionSt extends AppCompatActivity {
    private final String TAG = "QuestionSt";
    private FirebaseFirestore db;
    private RadioButton rbQuestionA;
    private RadioButton rbQuestionB;
    private RadioButton rbQuestionC;
    private RadioButton rbQuestionD;
    private EditText etQuestionMin;
    private CardView cvNextStep;
    private int selection = -1;
    private String classId;
    private ImageButton backIBtn;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    ArrayList<String> A = new ArrayList<>();
    ArrayList<String> B = new ArrayList<>();
    ArrayList<String> C = new ArrayList<>();
    ArrayList<String> D = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_st);

        //init db
        db = FirebaseFirestore.getInstance();

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");

        //init xml
        rbQuestionA = findViewById(R.id.question_a);
        rbQuestionB = findViewById(R.id.question_b);
        rbQuestionC = findViewById(R.id.question_c);
        rbQuestionD = findViewById(R.id.question_d);
        etQuestionMin = findViewById(R.id.question_min);
        cvNextStep = findViewById(R.id.nextStepButton);
        backIBtn =findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());
        cvNextStep.setOnClickListener(v -> {
            nextStep();
        });

    }

    private void nextStep() {
        String endTime = etQuestionMin.getText().toString().trim();
        Log.d(TAG,"selection : "+selection);
        if(selection != -1 && !"".equals(endTime)){
            Log.d(TAG,"QuestionSetting");
            String Answer = null;
            switch (selection){
                case 1 :
                    Answer = "A";
                    break;
                case 2 :
                    Answer = "B";
                    break;
                case 3 :
                    Answer = "C";
                    break;
                case 4 :
                    Answer = "D";
                    break;
            }
            Date nowDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);
            calendar.add(Calendar.MINUTE,Integer.valueOf(endTime));
            Date setDate = calendar.getTime();
            Map<String, Object> questionSet = new HashMap<>();
            questionSet.put("create_time", setDate);
            questionSet.put("Answer", Answer);
            questionSet.put("A", A);
            questionSet.put("B", B);
            questionSet.put("C", C);
            questionSet.put("D", D);

            db.collection("Class")
                    .document(classId)
                    .collection("Question")
                    .document("question")
                    .set(questionSet);

            Map<String, Object> classQuestionSet = new HashMap<>();
            classQuestionSet.put("question_state", true);

            db.collection("Class")
                    .document(classId)
                    .update(classQuestionSet);

            Intent intent = new Intent();
            intent.setClass(QuestionSt.this, QuestionWait.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putString("classId", classId);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "請確認是否填寫", Toast.LENGTH_LONG).show();

        }
    }

    public void onSelect(View view){
        switch(view.getId()){
            case R.id.question_a:
                selection = 1;
                Log.d(TAG,"selection : "+selection);
                break;
            case R.id.question_b:
                selection = 2;
                Log.d(TAG,"selection : "+selection);
                break;
            case R.id.question_c:
                selection = 3;
                Log.d(TAG,"selection : "+selection);
                break;
            case R.id.question_d:
                selection = 4;
                Log.d(TAG,"selection : "+selection);
                break;
        }
    }
}
