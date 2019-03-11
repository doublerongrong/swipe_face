package com.example.kl.home;

import android.content.Context;
import android.os.Bundle;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.kl.home.BackHandlerHelper.handleBackPress;
import com.example.kl.home.Model.Class;


public class Fragment_ClassDetail extends Fragment implements FragmentBackHandler {


    private String TAG = "ClassDetail";
    private String classId;
    private Class aclass;
    private Class firestore_class;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GridLayout gridLayout;
    private TextView text_class_id;
    private TextView text_class_title;



    OnFragmentSelectedListener mCallback;//Fragment傳值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        classId=args.getString("info");
        Log.d(TAG,"classId:" +classId);//fragment傳值
        Toast.makeText(getContext(),"現在課程資料庫代碼是"+classId,Toast.LENGTH_LONG).show();


        return inflater.inflate(R.layout.fragment_fragment_class_detail, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "classId2:" + classId);
        text_class_title = (TextView) view.findViewById(R.id.text_class_title);
        text_class_id = (TextView) view.findViewById(R.id.text_class_id);
        gridLayout = (GridLayout) view.findViewById(R.id.grid_class_detail);

        setClass(new FirebaseCallback() {
            @Override
            public void onCallback(Class firestore_class) {

                text_class_title.setText(firestore_class.getClass_name());
                text_class_id.setText(firestore_class.getClass_id());

                setSingleEvent(gridLayout , firestore_class);
            }
        });


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

    // we are setting onClickListener for each element 處理選項
    private void setSingleEvent(GridLayout gridLayout , Class firestore_class) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Clicked at index " + finalI,
                            Toast.LENGTH_SHORT).show();
                    switch (finalI) {
                        case 0:
                            //intent activity
                            break;
                        case 1:
                            //intent activity

                            break;
                        case 2:

                            mCallback.onFragmentSelected(firestore_class.getClass_id(), "toLeaveManage");//fragment傳值

                            break;
                        case 3:

                            mCallback.onFragmentSelected("", "toClassPerformance");//fragment傳值
                            break;
                        case 4:



                            break;
                        case 5:


                            break;
                        case 6:
                            //intent activity
                            break;
                        case 7:
                            //intent activity
                            break;
                        case 8:
                            //intent activity
                            break;

                    }
                }
            });
        }
    }


}
