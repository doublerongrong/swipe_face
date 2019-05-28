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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Fragment_ClassDetail extends Fragment implements FragmentBackHandler {


    private String TAG = "ClassDetail";
    private String classId, rollcallDocId, today_rollcallDocId;
    private Class firestore_class;
    private FirebaseFirestore db;
    private GridLayout gridLayout;
    private TextView text_class_title;
    private String class_id;
    private String teacher_email = (FirebaseAuth.getInstance().getCurrentUser()).toString();
    private Date date, rollcall_date;


    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId = args.getString("info");

        if (args.getString("rollcall_id") != null) {
            rollcallDocId = args.getString("rollcall_id");
            class_id = args.getString("class_id");

            Log.d(TAG, "rollcallId : " + rollcallDocId + "\tclass_id : " + class_id);
        }
        Log.d(TAG, "classId:" + classId);//fragment傳值
        db = FirebaseFirestore.getInstance();

        return inflater.inflate(R.layout.fragment_fragment_class_detail, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        text_class_title = view.findViewById(R.id.text_class_title);
        gridLayout = view.findViewById(R.id.grid_class_detail);

        date = new Date();


        setClass(firestore_class -> {

            text_class_title.setText(firestore_class.getClass_name());

            setSingleEvent(gridLayout, firestore_class);
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
                        today_rollcallDocId = firestore_class.getRollcall_docId();
                        class_id = firestore_class.getClass_id();
                        teacher_email = firestore_class.getTeacher_email();
                        Log.d(TAG, "class_id : " + class_id);
                        Log.d(TAG, "today_rollcallDocId: " + today_rollcallDocId);
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

    // we are setting onClickListener for each element 處理選項
    private void setSingleEvent(GridLayout gridLayout, Class firestore_class) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(view -> {
                Log.d(TAG, "index " + finalI);
                switch (finalI) {
                    case 0:
                        if (!today_rollcallDocId.equals("1")) {
                            DocumentReference docRef1 = db.collection("Rollcall").document(today_rollcallDocId);
                            docRef1.get().addOnSuccessListener(documentSnapshot1 -> {
                                rollcall_date = documentSnapshot1.getDate("rollcall_time");
                                if (isSameDate(date, rollcall_date)) {
                                    Intent i1 = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("class_id", class_id);
                                    bundle.putString("class_doc", classId);
                                    bundle.putString("classDoc_id", today_rollcallDocId);
                                    i1.putExtras(bundle);
                                    i1.setClass(getActivity(), RollcallSelect.class);
                                    startActivity(i1);
                                } else {
                                    Intent i1 = new Intent();
                                    Bundle bundlecall = new Bundle();
                                    bundlecall.putString("class_id", class_id);
                                    bundlecall.putString("class_doc", classId);
                                    i1.putExtras(bundlecall);
                                    i1.setClass(getActivity(), RollcallSelect.class);
                                    startActivity(i1);
                                }
                            });
                        } else {
                            Intent i1 = new Intent();
                            Bundle bundlecall = new Bundle();
                            bundlecall.putString("class_id", class_id);
                            bundlecall.putString("class_doc", classId);
                            i1.putExtras(bundlecall);
                            i1.setClass(getActivity(), RollcallSelect.class);
                            startActivity(i1);
                        }

                        break;
                    case 1:
                        //intent activity 今日出缺席

                        if (!today_rollcallDocId.equals("1")) {
                            DocumentReference docRef2 = db.collection("Rollcall").document(today_rollcallDocId);
                            docRef2.get().addOnSuccessListener(documentSnapshot1 -> {
                                rollcall_date = documentSnapshot1.getDate("rollcall_time");
                                if (isSameDate(date, rollcall_date)) {
                                    Intent i1 = new Intent();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("class_id", class_id);
                                    bundle.putString("class_doc", classId);
                                    bundle.putString("classDoc_id", today_rollcallDocId);
                                    i1.putExtras(bundle);
                                    i1.setClass(getActivity(), RollcallResult.class);
                                    startActivity(i1);
                                } else {
                                    Toast.makeText(getActivity(), "今天還沒點名喔", Toast.LENGTH_LONG).show();
                                }
                            });
                        }


                        break;
                    case 2:
                        //假單管理
//                            mCallback.onFragmentSelected(firestore_class.getClass_id(), "toLeaveManage");//fragment傳值
                        Intent i1 = new Intent();
                        Bundle bundleleave = new Bundle();
                        bundleleave.putString("info", class_id);
                        bundleleave.putString("teacher_email", teacher_email);
                        Log.d(TAG, "LeaveListN set bundle" + bundleleave.toString());
                        i1.putExtras(bundleleave);
                        i1.setClass(getActivity(), Fragment_LeaveListClassN.class);
                        startActivity(i1);
                        break;
                    case 3:
                        //學生清單
                        mCallback.onFragmentSelected(classId, "toClassStudentList");//fragment傳值
                        break;
                    case 4:
                        //小組清單
                        if (!firestore_class.isGroup_state() && !firestore_class.isGroup_state_go()) {//判斷是否分組
                            Intent intentToCreateClassGroupSt1 = new Intent();
                            intentToCreateClassGroupSt1.setClass(getActivity(), CreateClassGroupSt1.class);
                            Bundle bundleToCreateClassGroupSt1 = new Bundle();
                            bundleToCreateClassGroupSt1.putString("classId", classId);
                            bundleToCreateClassGroupSt1.putString("classYear", firestore_class.getClass_year());
                            bundleToCreateClassGroupSt1.putString("className", firestore_class.getClass_name());
                            bundleToCreateClassGroupSt1.putInt("classStuNum", firestore_class.getStudent_id().size());
                            intentToCreateClassGroupSt1.putExtras(bundleToCreateClassGroupSt1);
                            getActivity().startActivity(intentToCreateClassGroupSt1);
                        } else if (!firestore_class.isGroup_state() && firestore_class.isGroup_state_go()) {
                            Intent intentToCreateClassGroupSt3 = new Intent();
                            intentToCreateClassGroupSt3.setClass(getActivity(), CreateClassGroupSt3.class);
                            Bundle bundleToCreateClassGroupSt3 = new Bundle();
                            bundleToCreateClassGroupSt3.putString("classId", classId);
                            bundleToCreateClassGroupSt3.putString("classYear", firestore_class.getClass_year());
                            bundleToCreateClassGroupSt3.putString("className", firestore_class.getClass_name());
                            bundleToCreateClassGroupSt3.putInt("classStuNum", firestore_class.getStudent_id().size());
                            intentToCreateClassGroupSt3.putExtras(bundleToCreateClassGroupSt3);
                            getActivity().startActivity(intentToCreateClassGroupSt3);
                        } else {
                            Intent intentToGroupPage = new Intent();
                            intentToGroupPage.setClass(getActivity(), GroupPage.class);
                            Bundle bundleToGroupPage = new Bundle();
                            bundleToGroupPage.putString("classId", classId);
                            bundleToGroupPage.putString("class_Id", firestore_class.getClass_id());
                            bundleToGroupPage.putString("classYear", firestore_class.getClass_year());
                            bundleToGroupPage.putString("className", firestore_class.getClass_name());
                            bundleToGroupPage.putInt("classStuNum", firestore_class.getStudent_id().size());
                            intentToGroupPage.putExtras(bundleToGroupPage);
                            getActivity().startActivity(intentToGroupPage);
                        }
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
                        if (!firestore_class.isQuestion_state()) {
                            Intent intentToQuestion = new Intent();
                            intentToQuestion.setClass(getActivity(), QuestionSt.class);
                            Bundle bundleToQuestion = new Bundle();
                            bundleToQuestion.putString("classId", classId);
                            intentToQuestion.putExtras(bundleToQuestion);
                            getActivity().startActivity(intentToQuestion);
                        } else {
                            DocumentReference questionDoc = db.collection("Class").document(classId)
                                    .collection("Question").document("question");
                            questionDoc.get().addOnSuccessListener(documentSnapshot2 -> {
                                Question question = documentSnapshot2.toObject(Question.class);
                                Date create_time = question.getCreate_time();
                                Date nowDate = new Date();

                                if (create_time.before(nowDate)) {
                                    Intent intentToAnalysis = new Intent();
                                    intentToAnalysis.setClass(getActivity(), QuestionAnalysis.class);
                                    Bundle bundleToAnalysis = new Bundle();
                                    bundleToAnalysis.putString("classId", classId);
                                    intentToAnalysis.putExtras(bundleToAnalysis);
                                    getActivity().startActivity(intentToAnalysis);
                                } else {
                                    Intent intentToQuestion = new Intent();
                                    intentToQuestion.setClass(getActivity(), QuestionWait.class);
                                    Bundle bundleToQuestion = new Bundle();
                                    bundleToQuestion.putString("classId", classId);
                                    intentToQuestion.putExtras(bundleToQuestion);
                                    getActivity().startActivity(intentToQuestion);
                                }

                            });
                        }

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
                        bundleToExport.putString("class_id", class_id);
                        intentToExport.putExtras(bundleToExport);
                        getActivity().startActivity(intentToExport);
                        break;

                }
            });
        }

    }

    //判斷點名日期是否為同一天
    private static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }
}
