package com.example.kl.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CreateClassSt1 extends AppCompatActivity {
    private EditText editTextclassName;
    private Spinner spinnerClassYear;
    private Button nextStepBtn;
    private ImageButton backIBtn;

    private String[] classyear = {"107 下", "108 上", "108 下"};
    private ArrayAdapter<String> listAdapter;//學年

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creatclass_st1);

        editTextclassName = (EditText) findViewById(R.id.editTextClassName);
        spinnerClassYear = (Spinner) findViewById(R.id.spinner_classyear);
        nextStepBtn = (Button) findViewById(R.id.nextStepButton);
        backIBtn = (ImageButton) findViewById(R.id.backIBtn);

        listAdapter = new ArrayAdapter<String>(this, R.layout.classyear_spinner, classyear);

        spinnerClassYear.setAdapter(listAdapter);

        backIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        nextStepBtn.setOnClickListener(v -> {

            String classname = editTextclassName.getText().toString().trim();
            String classyear = spinnerClassYear.getSelectedItem().toString();

            Intent intent = new Intent();
            intent.setClass(CreateClassSt1.this, CreateClassSt2.class);
            Bundle bundle = new Bundle();
            bundle.putString("classnameB", classname);
            bundle.putString("classyearB", classyear);
            intent.putExtras(bundle);
            startActivity(intent);

        });
    }

}
