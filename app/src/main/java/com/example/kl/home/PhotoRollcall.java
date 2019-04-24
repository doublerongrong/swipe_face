package com.example.kl.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.kl.home.Model.RollCallStudent;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.example.kl.home.RollcallResult.getmContext;
@RuntimePermissions
public class PhotoRollcall extends AppCompatActivity {

    private String classId,classDoc,docId;
    String name,id,email,department,school;
    private Button finishBtn,photoBtn;
    private int REQUEST_CODE_CHOOSE = 9;
    public List<String> result,classMember;
    private List<String> attendList, absenceList,casualList,funeralList,lateList,officalList,sickList;
    String url = "http://192.168.43.172:8080/ProjectApi/api/FaceApi/RetrievePhoto";
    OkHttpClient client = new OkHttpClient();
    private static Context mContext;
    ResponseBody responseBody;
    String responseData;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int count = 0;
    private Date time ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_rollcall);


        PhotoRollcallPermissionsDispatcher.permissionWithPermissionCheck(this);
        Bundle bundle = this.getIntent().getExtras();
        classId = bundle.getString("class_id");
        classDoc = bundle.getString("class_doc");
        Log.i("classid:",classId);
        Log.i("classdoc:",classDoc);

        finishBtn = (Button)findViewById(R.id.buttonFinish);
        photoBtn = (Button)findViewById(R.id.buttonPhoto);
        classMember = new ArrayList<>();
        result = new ArrayList<>();
        attendList = new ArrayList<>();
        absenceList = new ArrayList<>();
        casualList = new ArrayList<>();
        funeralList = new ArrayList<>();
        lateList = new ArrayList<>();
        officalList = new ArrayList<>();
        sickList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Class").whereEqualTo("class_id",classId);
        query.get().addOnCompleteListener(task -> {
                    QuerySnapshot querySnapshot = task.isSuccessful() ? task.getResult() : null;
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        classMember = (ArrayList) documentSnapshot.get("student_id");
                    }
        });

        photoBtn.setOnClickListener(v ->{
            Matisse.from(PhotoRollcall.this)
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
            for (int i=0;i<classMember.size();i++){
                if (!absenceList.contains(classMember.get(i)) && !attendList.contains(classMember.get(i))){
                    absenceList.add(classMember.get(i));
                }
            }
            Query query1 = db.collection("Rollcall").whereEqualTo("rollcall_time", time);
            query1.get().addOnCompleteListener(task1 -> {
                QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

                for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                    docId = documentSnapshot.getId();
                }
                Map<String, Object> attend = new HashMap<>();
                attend.put("rollcall_absence", absenceList);
                db.collection("Rollcall").document(docId).update(attend).addOnCompleteListener(task -> {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), RollcallResult.class);
                    intent.putExtra("class_id", classId);
                    intent.putExtra("class_doc",classDoc);
                    intent.putExtra("classDoc_id",docId);
                    intent.putExtra("request","1");
                    startActivity(intent);
                    finish();
                });
            });

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
        for (int i = 0; i <img.size() ; i++) {//用迴圈去RUN多選照片
            File file=new File(img.get(i));
            if (file !=null) {
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
        responseBody =response.body();
        responseData = responseBody.string();
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject obj=jsonArray.getJSONObject(i);
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
                department =obj.getString("student_department");
                school = obj.getString("student_school");

                Log.i("name",name);
                Log.i("id",id);
                Log.i("email",email);
                Log.i("department",department);
                Log.i("school",school);

                if (classMember.contains(id)) {
                    if (!attendList.contains(id)) {
                        attendList.add(id);
                        Log.i("attend",id);
                        //ToastUtils.show(getmContext(), "辨識成功 !");
                    }
                }else{
                    if (!absenceList.contains(id)) {
                        absenceList.add(id);
                        //ToastUtils.show(getmContext(), "辨識失敗 !");
                    }
                }

                //ToastUtils.show(getmContext(),"名字:"+name+"\n"+"學號: "+id+"\n"+"email:"+email+"\n"+"系所:"+department+"\n"+"學校:"+school);
                //heroList.add(hero);
            }
            if (count == 0) {
                time = new Date();
                Map<String, Object> attend = new HashMap<>();
                attend.put("class_id", classId);
                attend.put("rollcall_attend", attendList);
                attend.put("rollcall_absence", absenceList);
                attend.put("rollcall_casual", casualList);
                attend.put("rollcall_funeral", funeralList);
                attend.put("rollcall_late", lateList);
                attend.put("rollcall_offical", officalList);
                attend.put("rollcall_sick", sickList);
                attend.put("rollcall_time", time);
                db.collection("Rollcall").add(attend);
                count += 1;
                //adapter = new HeroAdapter(heroList, getmContext());
                //get_recyclerview().setAdapter(adapter);
            }else{
                Query query = db.collection("Rollcall").whereEqualTo("rollcall_time", time);
                query.get().addOnCompleteListener(task1 -> {
                    QuerySnapshot querySnapshot = task1.isSuccessful() ? task1.getResult() : null;

                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        docId = documentSnapshot.getId();
                    }
                    Map<String, Object> attend = new HashMap<>();
                    attend.put("rollcall_attend", attendList);
                    attend.put("rollcall_absence", absenceList);
                    db.collection("Rollcall").document(docId).update(attend);
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Context getmContext(){
        return mContext;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PhotoRollcallPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permission() {
    }
}
