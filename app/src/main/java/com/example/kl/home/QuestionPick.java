package com.example.kl.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kl.home.Model.Bonus;
import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Question;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class QuestionPick extends AppCompatActivity {
    private final String TAG = "QuestionPick";
    private FirebaseFirestore db;
    private ArrayList<String> student_id;
    private int random;
    private String classId;
    private String type;
    private Performance performance;
    private Bonus bonus;
    private TextView tvStudentDepartment;
    private TextView tvStudent_id;
    private TextView tvStudentName;
    private ImageView ivStudentPhoto;
    private Button cvNextone;
    private CardView cvCorrectAnswer;
    private Student student;
    private ImageView backIBtn;
    ArrayList<String> A = new ArrayList<>();
    ArrayList<String> B = new ArrayList<>();
    ArrayList<String> C = new ArrayList<>();
    ArrayList<String> D = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pick);

        //init DB
        db = FirebaseFirestore.getInstance();

        //init Intent Bundle
        Intent Intent = getIntent(); /* 取得傳入的 Intent 物件 */
        Bundle bundle = Intent.getExtras();
        classId = bundle.getString("classId");
        type = bundle.getString("type");
        Log.d(TAG,"tpe : "+type);

        //init xml
        tvStudentDepartment = findViewById(R.id.text_student_department);
        tvStudent_id = findViewById(R.id.text_student_id);
        tvStudentName = findViewById(R.id.text_student_name);
        ivStudentPhoto = findViewById(R.id.img_student_photo);
        cvNextone = findViewById(R.id.card_nextone);
        cvCorrectAnswer = findViewById(R.id.card_correct_answer);
        backIBtn =findViewById(R.id.backIBtn);
        backIBtn.setOnClickListener(v -> finish());

        if(classId!=null){
            setData();
        }

    }

    private void setData() {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRefGroup = db.collection("Class").document(classId)
                .collection("Question").document("question");
        docRefGroup.get().addOnSuccessListener(documentSnapshot -> {
            Question question = documentSnapshot.toObject(Question.class);
            A = question.getA();
            B = question.getB();
            C = question.getC();
            D = question.getD();

            if(type.equals("A")){
                random = (int) (Math.random() * A.size());
                db.collection("Student")
                        .whereEqualTo("student_id", A.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        student = document.toObject(Student.class);

                                        setView(student);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }else if(type.equals("B")){
                random = (int) (Math.random() * B.size());
                db.collection("Student")
                        .whereEqualTo("student_id", B.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        student = document.toObject(Student.class);

                                        setView(student);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }else if(type.equals("C")){
                random = (int) (Math.random() * C.size());
                db.collection("Student")
                        .whereEqualTo("student_id", C.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        student = document.toObject(Student.class);

                                        setView(student);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }else {
                random = (int) (Math.random() * D.size());
                db.collection("Student")
                        .whereEqualTo("student_id", D.get(random))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        student = document.toObject(Student.class);

                                        setView(student);
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
    }

    private void setView(Student student) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("student_photo");

        String photoUrl = student.getStudent_id();
        StorageReference path = storageReference.child(photoUrl);

        tvStudentDepartment.setText(student.getStudent_department());
        tvStudent_id.setText(student.getStudent_id());
        tvStudentName.setText(student.getStudent_name());
        Glide.with(this.getApplicationContext())
                .load(path)
                .into(ivStudentPhoto);
        cvNextone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setData();

            }
        });
    }
}
