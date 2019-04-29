package com.example.kl.home;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Question;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuestionWait extends AppCompatActivity {
    private final String TAG = "QuestionWait";
    private FirebaseFirestore db;
    private TextView tvCountDownTime;
    private String classId;
    private String endTimeToString;
    private ImageButton backIBtn;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_wait);

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");

        //init db
        if(classId!=null){
            db = FirebaseFirestore.getInstance();
            DocumentReference docRefGroup = db.collection("Class").document(classId)
                    .collection("Question").document("question");
            docRefGroup.get().addOnSuccessListener(documentSnapshot -> {
                Question question = documentSnapshot.toObject(Question.class);
                Date create_time = question.getCreate_time();
                Date nowTime = new Date();
                Long utCreate_Time=create_time.getTime();
                Long utNowTime=nowTime.getTime();
                Long sec=(utCreate_Time-utNowTime)/1000;//秒差
                Log.d(TAG,sec.toString());
                String countDownTime = sec.toString();
                Log.d(TAG,"countDownTime : "+countDownTime);
                new CountDownTimer(sec*1000,1000){

                    @Override
                    public void onFinish() {
                        Intent intentToAnalysis = new Intent();
                        intentToAnalysis.setClass(QuestionWait.this, QuestionAnalysis.class);
                        intentToAnalysis.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Bundle bundleToAnalysis = new Bundle();
                        bundleToAnalysis.putString("classId", classId);
                        intentToAnalysis.putExtras(bundleToAnalysis);
                        startActivity(intentToAnalysis);
                        finish();
                    }

                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCountDownTime.setText(millisUntilFinished/1000/60+"\t\t分\t\t"+millisUntilFinished/1000%60+"\t\t秒");
                    }

                }.start();
            });

        }


        //init xml
        tvCountDownTime = findViewById(R.id.countdowntime);
        backIBtn =findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());


    }
}
