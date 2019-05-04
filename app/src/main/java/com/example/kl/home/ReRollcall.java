package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kl.home.Model.Class;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ReRollcall extends AppCompatActivity {

    private String classId, classDoc, rollcallDocId, docId;
    private String perId,perf;
    String name, id, email, department, school;
    private ImageButton backBtn;
    private Button finishBtn, photoBtn;
    private int REQUEST_CODE_CHOOSE = 9;
    public List<String> result, classMember;
    private List<String> attendList, absenceList,lateList,oriAttend,oriAbsence,oriLate;
    String url = "http://172.20.10.8:8080/ProjectApi/api/FaceApi/RetrievePhoto";
    OkHttpClient client = new OkHttpClient();
    private static Context mContext;
    ResponseBody responseBody;
    String responseData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int count = 0;
    int score,j,k,l,n;
    private int lateMinus,absenteeMinus;
    private Date time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_rollcall);

        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        classDoc = bundle.getString("class_doc");
        rollcallDocId = bundle.getString("classDoc_id");
        Log.i("classid:", classId);
        Log.i("classdoc:", classDoc);
        Log.i("rollcallDocId", rollcallDocId);

        finishBtn = (Button) findViewById(R.id.buttonFinish);
        photoBtn = (Button) findViewById(R.id.buttonPhoto);
        backBtn = (ImageButton)findViewById(R.id.backIBtn);
        classMember = new ArrayList<>();
        result = new ArrayList<>();
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();
        lateList = new ArrayList<>();
        oriAttend = new ArrayList<>();
        oriAbsence = new ArrayList<>();
        oriLate = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        //獲取老師設定分數
        db.collection("Class").whereEqualTo("class_id", classId).
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d("RollcallResult", "Error :" + e.getMessage());
                        }
                        Class aClass = documentSnapshots.getDocuments().get(0).toObject(Class.class);
                        lateMinus = aClass.getClass_lateminus();
                        absenteeMinus = aClass.getClass_absenteeminus();
                    }
                });

        Query query = db.collection("Class").whereEqualTo("class_id", classId);
        query.get().addOnCompleteListener(task -> {
            QuerySnapshot querySnapshot = task.isSuccessful() ? task.getResult() : null;
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                classMember = (ArrayList) documentSnapshot.get("student_id");
            }
        });

        DocumentReference docRef2 = db.collection("Rollcall").document(rollcallDocId);
        docRef2.get().addOnSuccessListener(documentSnapshot -> {
            oriLate = (ArrayList) documentSnapshot.get("rollcall_late");
            oriAbsence = (ArrayList) documentSnapshot.get("rollcall_absence");
            oriAttend = (ArrayList) documentSnapshot.get("rollcall_attend");
            lateList = oriLate;
            absenceList = oriAbsence;
            attendList = oriAttend;
        });


        photoBtn.setOnClickListener(v -> {
            Matisse.from(ReRollcall.this)
                    .choose(MimeType.ofAll())//图片类型
                    .countable(false)//true:选中后显示数字;false:选中后显示对号
                    .maxSelectable(9)//可选的最大数
                    .capture(true)//选择照片时，是否显示拍照
                    .captureStrategy(new CaptureStrategy(true, "com.example.kl.home.fileprovider"))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                    .imageEngine(new MyGlideEngine())//图片加载引擎
                    .theme(R.style.Matisse_Zhihu)
                    .forResult(REQUEST_CODE_CHOOSE = 9);//REQUEST_CODE_CHOOSE自定
            Log.i("Create Android", "Test選圖");
        });

        finishBtn.setOnClickListener(view -> {
            if(!lateList.isEmpty()){
                for ( int i = 0; i < lateList.size(); i++) {
                    j = i;
                    Query query1 = db.collection("Performance")
                            .whereEqualTo("class_id", classId)
                            .whereEqualTo("student_id", lateList.get(j));
                    query1.get().addOnCompleteListener(task -> {
                        QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                        Log.i("query", "work");
                        for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                            perf = documentSnapshot2.get("performance_totalAttendance").toString();
                            perId = documentSnapshot2.getId();
                            score = Integer.parseInt(perf);
                            if (oriAttend.contains(lateList.get(j))){
                                score -= lateMinus;
                            }
                            if(oriLate.contains(lateList.get(j))){
                                score = score;
                            }
                            if(oriAbsence.contains(lateList.get(j))) {
                                score -= (lateMinus - absenteeMinus);
                            }
                            DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                            ChangePointRef.update("performance_totalAttendance", score);
                        }
                    });
                }
            }

            if(!absenceList.isEmpty()){
                for ( int i = 0; i < absenceList.size(); i++) {
                    k = i;
                    Query query1 = db.collection("Performance")
                            .whereEqualTo("class_id", classId)
                            .whereEqualTo("student_id", absenceList.get(k));
                    query1.get().addOnCompleteListener(task -> {
                        QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                        Log.i("query123", "work");
                        for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                            perf = documentSnapshot2.get("performance_totalAttendance").toString();
                            Log.i("perf",perf);
                            perId = documentSnapshot2.getId();
                            score = Integer.parseInt(perf);
                            if (oriAttend.contains(absenceList.get(k))){
                                Log.i("absence","work1");
                                score -= absenteeMinus;
                            }
                            if (oriLate.contains(absenceList.get(k))){
                                Log.i("absence","work2");
                                score -= (absenteeMinus-lateMinus);
                            }
                            if (oriAbsence.contains(absenceList.get(k))) {
                                Log.i("absence","work3");
                                score = score;
                            }
                            DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                            ChangePointRef.update("performance_totalAttendance", score);
                        }
                    });
                }
            }
            if(!attendList.isEmpty()){
                for ( int i = 0; i < attendList.size(); i++) {
                    l = i;
                    Query query1 = db.collection("Performance")
                            .whereEqualTo("class_id", classId)
                            .whereEqualTo("student_id", attendList.get(l));
                    query1.get().addOnCompleteListener(task -> {
                        QuerySnapshot querySnapshot1 = task.isSuccessful() ? task.getResult() : null;
                        Log.i("query", "work");
                        for (DocumentSnapshot documentSnapshot2 : querySnapshot1.getDocuments()) {
                            perf = documentSnapshot2.get("performance_totalAttendance").toString();
                            perId = documentSnapshot2.getId();
                            score = Integer.parseInt(perf);
                            if (oriAttend.contains(attendList.get(l))){
                                score = score;
                            }
                            if (oriLate.contains(attendList.get(l))){
                                score += lateMinus;
                            }
                            if (oriAbsence.contains(attendList.get(l))) {
                                score += absenteeMinus;
                            }
                            DocumentReference ChangePointRef = db.collection("Performance").document(perId);
                            ChangePointRef.update("performance_totalAttendance", score);
                        }
                    });
                }
            }
            Map<String, Object> attend = new HashMap<>();
            attend.put("rollcall_absence", absenceList);
            attend.put("rollcall_attend",attendList);
            attend.put("rollcall_late",lateList);
            db.collection("Rollcall").document(rollcallDocId).update(attend).addOnCompleteListener(task -> {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), RollcallResult.class);
                intent.putExtra("class_id", classId);
                intent.putExtra("class_doc", classDoc);
                intent.putExtra("classDoc_id", rollcallDocId);
                startActivity(intent);
                finish();
            });

        });

        backBtn.setOnClickListener(view -> {
            finish();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK && requestCode == 9) {
            Log.i("Create Android", "Test4");
            Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
            Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
            Log.e("Matisse", "Use the selected photos with original: " + String.valueOf(Matisse.obtainOriginalState(data)));
            result = Matisse.obtainPathResult(data);
            retrieveFile(result);
            Log.i("Create Android", "Test5");

        }
    }

    public void retrieveFile(List<String> img) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//setType一定要Multipart
        for (int i = 0; i < img.size(); i++) {//用迴圈去RUN多選照片
            File file = new File(img.get(i));
            if (file != null) {
                builder.addFormDataPart("photos", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            }//前面是para  中間是抓圖片名字 後面是創一個要求
        }

        MultipartBody requestBody = builder.build();//建立要求

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("Create Android", "Test失敗");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("Create Android", "Test成功");
                parseJsonWithJsonObject(response);

            }

        });

    }

    private void parseJsonWithJsonObject(Response response) throws IOException {
        responseBody = response.body();
        responseData = responseBody.string();
        try {
            JSONArray jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                /*Hero hero = new Hero(
                        obj.getString("student_name"),
                        obj.getString("student_id"),
                        obj.getString("student_email"),
                        obj.getString("student_department"),
                        obj.getString("student_school")
                );*/
                name = obj.getString("student_name");
                id = obj.getString("student_id");
                email = obj.getString("student_email");
                department = obj.getString("student_department");
                school = obj.getString("student_school");

                Log.i("name", name);
                Log.i("id", id);
                Log.i("email", email);
                Log.i("department", department);
                Log.i("school", school);

                if (classMember.contains(id)) {
                    if (!attendList.contains(id) && absenceList.contains(id)) {
                        lateList.add(id);
                    }
                    if (absenceList.contains(id)) {
                        for (int o = 0; o < absenceList.size(); o++) {
                            if (absenceList.get(o).equals(id)){
                                absenceList.remove(o);
                                o--;
                            }
                        }
                    }
                }

                //ToastUtils.show(getmContext(),"名字:"+name+"\n"+"學號: "+id+"\n"+"email:"+email+"\n"+"系所:"+department+"\n"+"學校:"+school);
                //heroList.add(hero);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Context getmContext() {
        return mContext;
    }
}
