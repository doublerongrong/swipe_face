package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.Performance;
import com.example.kl.home.Model.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.kl.home.BackHandlerHelper.handleBackPress;

import com.example.kl.home.Model.Class;

import java.util.Date;


public class Fragment_ClassDetail extends Fragment implements FragmentBackHandler {


    private String TAG = "ClassDetail";
    private String url = "http://192.168.0.108:8080/Export/StudentGrade/";
    private String classId,rollcallDocId;
    private Class aclass;
    private Class firestore_class;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GridLayout gridLayout;
    private TextView text_class_id;
    private TextView text_class_title;
    private String class_id;
    private String teacher_email = (FirebaseAuth.getInstance().getCurrentUser()).toString();


    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId = args.getString("info");

        if(args.getString("rollcall_id") != null){
            rollcallDocId = args.getString("rollcall_id");
            class_id = args.getString("class_id");
            Log.d(TAG,"rollcallId : "+rollcallDocId+"\tclass_id : "+class_id);
        }
        Log.d(TAG, "classId:" + classId);//fragment傳值
//        Toast.makeText(getContext(), "現在課程資料庫代碼是" + classId, Toast.LENGTH_LONG).show();
        db = FirebaseFirestore.getInstance();
        getTeacher_email(classId);


        return inflater.inflate(R.layout.fragment_fragment_class_detail, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "classId2:" + classId);
        text_class_title = (TextView) view.findViewById(R.id.text_class_title);
        gridLayout = (GridLayout) view.findViewById(R.id.grid_class_detail);

        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            class_id = documentSnapshot.toObject(Class.class).getClass_id();
            Log.d(TAG,"class_id : "+class_id);
        });


        setClass(new FirebaseCallback() {
            @Override
            public void onCallback(Class firestore_class) {

                text_class_title.setText(firestore_class.getClass_name());

                setSingleEvent(gridLayout, firestore_class);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //判斷分組時間和現在時間去改變group_state
//        Date date = new Date();
//        DocumentReference docRefGroup = db.collection("Class").document(classId);
//        docRefGroup.get().addOnSuccessListener(documentSnapshot -> {
//            Class classG = documentSnapshot.toObject(Class.class);
//            if(classG.getCreate_time().before(date)){
//                Map<String, Object> group = new HashMap<>();
//                group.put("group_state",true);
//
//                db.collection("Class")
//                        .document(classId)
//                        .update(group);
//            }});
    }

    private void setClass(FirebaseCallback firebaseCallback) {
        firestore_class = new Class();
        Log.d(TAG, "setClass class_id:" + classId);
        Task<DocumentSnapshot> documentSnapshotTask = db.collection("Class")
                .document(classId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        firestore_class = documentSnapshot.toObject(Class.class);
                        Log.d(TAG, "setClass here");
                        firebaseCallback.onCallback(firestore_class);
                    }
                });


    }

    @Override
    public boolean onBackPressed() {
        if (handleBackPress(this)) {
            //外理返回鍵
            return true;
        } else {
            // 如果不包含子Fragment
            // 或子Fragment沒有外理back需求
            // 可如直接 return false;
            // 註：如果Fragment/Activity 中可以使用ViewPager 代替 this
            //
            return handleBackPress(this);
        }
    }//fragment 返回鍵

    public interface FirebaseCallback {
        void onCallback(Class firestore_class);
    }//處理firestore非同步的問題 回調接頭

//    public interface OnFragmentSelectedListener {
//        public void onFragmentSelected(String info, String fragmentKey);
//    }//Fragment傳值

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }

    public void getTeacher_email(String classId){
        DocumentReference docRef = db.collection("Class").document(classId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Class aClass = document.toObject(Class.class);
                        teacher_email = aClass.getTeacher_email();
                        class_id = aClass.getClass_id();

                    }
                }
            }
        });
    }

    // we are setting onClickListener for each element 處理選項
    private void setSingleEvent(GridLayout gridLayout, Class firestore_class) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   Log.d(TAG,"index "+finalI);
                    switch (finalI) {
                        case 0:
                            //intent activity
                            if(rollcallDocId != null) {
                                Log.d(TAG,"case0: rollcallDocId: "+rollcallDocId);
                                Intent i = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("class_id", class_id);
                                bundle.putString("class_doc", classId);
                                bundle.putString("classDoc_id", rollcallDocId);
                                i.putExtras(bundle);
                                i.setClass(getActivity(), RollcallSelect.class);
                                startActivity(i);
                            }else {
                                DocumentReference docRef = db.collection("Class").document(classId);
                                docRef.get().addOnSuccessListener(documentSnapshot -> {
                                    Class classG = documentSnapshot.toObject(Class.class);
                                    class_id = classG.getClass_id();
                                    Intent i = new Intent();
                                    Bundle bundlecall = new Bundle();
                                    bundlecall.putString("class_id", class_id);
                                    bundlecall.putString("class_doc", classId);
                                    i.putExtras(bundlecall);
                                    i.setClass(getActivity(), RollcallSelect.class);
                                    startActivity(i);
                                });
                            }
                            break;
                        case 1:
                            //intent activity 今日出缺席
                            if(rollcallDocId != null){
                                Intent i = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("class_id",class_id);
                                bundle.putString("class_doc",classId);
                                bundle.putString("classDoc_id",rollcallDocId);
                                i.putExtras(bundle);
                                i.setClass(getActivity(),RollcallResult.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(getActivity(),"今天還沒點名喔",Toast.LENGTH_LONG).show();
                            }


                            break;
                        case 2:
                            //假單管理

//                            mCallback.onFragmentSelected(firestore_class.getClass_id(), "toLeaveManage");//fragment傳值
                            Intent i = new Intent();
                            Bundle bundleleave = new Bundle();
                            bundleleave.putString("info",class_id);
                            bundleleave.putString("teacher_email",teacher_email);
                            Log.d(TAG,"LeaveListN set bundle" + bundleleave.toString());
                            i.putExtras(bundleleave);
                            i.setClass(getActivity(),Fragment_LeaveListClassN.class);
                            startActivity(i);
                            break;
                        case 3:
                            //學生清單
                            mCallback.onFragmentSelected(classId, "toClassStudentList");//fragment傳值
                            break;
                        case 4:
                            //小組清單
                            DocumentReference docRefGroup = db.collection("Class").document(classId);
                            docRefGroup.get().addOnSuccessListener(documentSnapshot -> {
                                Class classG = documentSnapshot.toObject(Class.class);
                                if (!classG.isGroup_state()&&!classG.isGroup_state_go()) {//判斷是否分組
                                    Intent intentToCreateClassGroupSt1 = new Intent();
                                    intentToCreateClassGroupSt1.setClass(getActivity(), CreateClassGroupSt1.class);
                                    Bundle bundleToCreateClassGroupSt1 = new Bundle();
                                    bundleToCreateClassGroupSt1.putString("classId", classId);
                                    bundleToCreateClassGroupSt1.putString("classYear", classG.getClass_year());
                                    bundleToCreateClassGroupSt1.putString("className", classG.getClass_name());
                                    bundleToCreateClassGroupSt1.putInt("classStuNum", classG.getStudent_id().size());
                                    intentToCreateClassGroupSt1.putExtras(bundleToCreateClassGroupSt1);
                                    getActivity().startActivity(intentToCreateClassGroupSt1);
                                } else if (!classG.isGroup_state()&&classG.isGroup_state_go()){
                                    Intent intentToCreateClassGroupSt3 = new Intent();
                                    intentToCreateClassGroupSt3.setClass(getActivity(), CreateClassGroupSt3.class);
                                    Bundle bundleToCreateClassGroupSt3 = new Bundle();
                                    bundleToCreateClassGroupSt3.putString("classId", classId);
                                    bundleToCreateClassGroupSt3.putString("classYear", classG.getClass_year());
                                    bundleToCreateClassGroupSt3.putString("className", classG.getClass_name());
                                    bundleToCreateClassGroupSt3.putInt("classStuNum", classG.getStudent_id().size());
                                    intentToCreateClassGroupSt3.putExtras(bundleToCreateClassGroupSt3);
                                    getActivity().startActivity(intentToCreateClassGroupSt3);
                                }
                                else {
                                    Intent intentToGroupPage = new Intent();
                                    intentToGroupPage.setClass(getActivity(), GroupPage.class);
                                    Bundle bundleToGroupPage = new Bundle();
                                    bundleToGroupPage.putString("classId", classId);
                                    bundleToGroupPage.putString("class_Id", classG.getClass_id());
                                    bundleToGroupPage.putString("classYear", classG.getClass_year());
                                    bundleToGroupPage.putString("className", classG.getClass_name());
                                    bundleToGroupPage.putInt("classStuNum", classG.getStudent_id().size());
                                    intentToGroupPage.putExtras(bundleToGroupPage);
                                    getActivity().startActivity(intentToGroupPage);
                                }
                            });
                            break;
                        case 5:
                            //點人答題
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), Activity_PickAnswer.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("classId", classId);
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);

                            break;
                        case 6:
                            //提問按鈕
                            DocumentReference docRefClass = db.collection("Class").document(classId);
                            docRefClass.get().addOnSuccessListener(documentSnapshot -> {
                                Class classCheckQuestion = documentSnapshot.toObject(Class.class);
                                if(!classCheckQuestion.isQuestion_state()){
                                    Intent intentToQuestion = new Intent();
                                    intentToQuestion.setClass(getActivity(), QuestionSt.class);
                                    Bundle bundleToQuestion = new Bundle();
                                    bundleToQuestion.putString("classId", classId);
                                    intentToQuestion.putExtras(bundleToQuestion);
                                    getActivity().startActivity(intentToQuestion);
                                }
                                else{
                                    DocumentReference questionDoc = db.collection("Class").document(classId)
                                            .collection("Question").document("question");
                                    questionDoc.get().addOnSuccessListener(documentSnapshot2 -> {
                                        Question question = documentSnapshot2.toObject(Question.class);
                                        Date create_time = question.getCreate_time();
                                        Date nowDate = new Date();

                                        if(create_time.before(nowDate)){
                                            Intent intentToAnalysis = new Intent();
                                            intentToAnalysis.setClass(getActivity(), QuestionAnalysis.class);
                                            Bundle bundleToAnalysis = new Bundle();
                                            bundleToAnalysis.putString("classId", classId);
                                            intentToAnalysis.putExtras(bundleToAnalysis);
                                            getActivity().startActivity(intentToAnalysis);
                                        }
                                        else{
                                            Intent intentToQuestion = new Intent();
                                            intentToQuestion.setClass(getActivity(), QuestionWait.class);
                                            Bundle bundleToQuestion = new Bundle();
                                            bundleToQuestion.putString("classId", classId);
                                            intentToQuestion.putExtras(bundleToQuestion);
                                            getActivity().startActivity(intentToQuestion);
                                        }

                                    });
                                }
                            });


                            break;
                        case 7:
                            //intent activity 計分設定
                            Intent intent7 = new Intent();
                            intent7.setClass(getActivity(), Activity_ScoreSetting.class);
                            Bundle bundle7 = new Bundle();
                            bundle7.putString("classId", classId);
                            intent7.putExtras(bundle7);
                            getActivity().startActivity(intent7);
                            break;
                        case 8:
                            //intent activity 繪出成績
                            Intent intentToExport = new Intent();
                            intentToExport.setClass(getActivity(), Export.class);
                            Bundle bundleToExport = new Bundle();
                            bundleToExport.putString("classId", classId);
                            bundleToExport.putString("class_id",class_id);
                            intentToExport.putExtras(bundleToExport);
                            getActivity().startActivity(intentToExport);
//                            OkHttpClient client = new OkHttpClient();
//                            LayoutInflater lf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            @SuppressLint("InflateParams")
//                            ViewGroup vg = (ViewGroup) lf.inflate(R.layout.dialog_export_excel, null);
//                            final EditText etShow = vg.findViewById(R.id.et_name);
//                            new AlertDialog.Builder(getActivity())
//                                    .setView(vg)
//                                    .setPositiveButton("確定", (dialog, which) -> {
//                                        final String email = etShow.getText().toString().trim();
//                                        if ("".equals(email)) {
//                                            Toast.makeText(getActivity(), "請輸入信箱", Toast.LENGTH_SHORT).show();
//                                            Log.d(TAG, "Dialog取消");
//                                        } else {
//                                            String urlToApi = url+"\\"+class_id+"\\"+email;
//                                            RequestBody reqbody = RequestBody.create(null, new byte[0]);
//                                            Request.Builder formBody = new Request.Builder()
//                                                    .url(urlToApi).method("POST",reqbody).header("Content-Length", "0");
//                                            client.newCall(formBody.build());
//                                            Log.d(TAG,"PostTest");
//
//                                        }
//                                    })
//                                    .setNegativeButton("取消", null).show();
                            break;

                    }
                }
            });
        }
    }




}
