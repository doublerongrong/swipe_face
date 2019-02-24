package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class Fragment_ClassList extends Fragment  implements FragmentBackHandler {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView mMainList;
    private ClassListAdapter classListAdapter;
    private List<Class> classList;
    private String TAG = "FLAG";
    private String teacher_email = "053792@mail.fju.edu.tw";

    private Teacher teacher;
    private ArrayList<String> class_id = new ArrayList<String>();

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private String classId;
    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__class_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();

        classList = new ArrayList<>();
        classListAdapter = new ClassListAdapter(getActivity().getApplicationContext(),classList);

        mMainList = (RecyclerView)getView().findViewById(R.id.class_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(classListAdapter);
        Log.d(TAG, "Flag1");




        db.collection("Class").whereEqualTo("teacher_email", teacher_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {

                    Log.d(TAG, "Error :" + e.getMessage());
                }


                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {


                    if (doc.getType() == DocumentChange.Type.ADDED ) {
                        classId = doc.getDocument().getId();
                        Class aClass = doc.getDocument().toObject(Class.class).withId(classId);
                        Log.d(TAG, "DB2 classId:"+classId);


                        classList.add(aClass);
                        classListAdapter.notifyDataSetChanged();



                    }
                }

            }
        });

        classListAdapter.setOnTransPageClickListener(new ClassListAdapter.transPageListener() {

            @Override
            public void onTransPageClick(String classId2) {
                Log.d(TAG,"onTransPageClick0" +classId2);
                mCallback.onFragmentSelected(classId2 , "ClassList");//fragment傳值
//                Log.d(TAG," classId:"+classId);
//
//                fragmentManager = getChildFragmentManager();
//                Log.d(TAG,"onTransPageClick1");
//                transaction = fragmentManager.beginTransaction();
//                Log.d(TAG,"onTransPageClick2");
//                transaction.replace(R.id.fragment_class_list, new FragmentClassDetail());
//                transaction.addToBackStack(new FragmentClassDetail().getClass().getName());
//                transaction.commit();

            }

        });//Fragment換頁




    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }//fragment 返回鍵

    public interface OnFragmentSelectedListener {
        public void onFragmentSelected(String info ,String fragmentKey);
    }//Fragment傳值

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener)context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }


}
