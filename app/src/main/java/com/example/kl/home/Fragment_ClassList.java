package com.example.kl.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kl.home.Adapter.ClassListAdapter;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
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
    private String teacher_email;
    private ImageButton ibSelectYear;//選擇正在進行中or已結束
    private Teacher teacher;
    private ArrayList<String> class_id = new ArrayList<String>();

    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private String classId;
    private ImageView imNoData;
    private FloatingActionButton fabCreateClass;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();//抓現在登入user
    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        teacher_email = user.getEmail();
        return inflater.inflate(R.layout.fragment_fragment__class_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();

        imNoData = view.findViewById(R.id.imNoData);

        classList = new ArrayList<>();
        classListAdapter = new ClassListAdapter(getActivity().getApplicationContext(),classList);

        mMainList = getView().findViewById(R.id.class_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(classListAdapter);

        fabCreateClass = (FloatingActionButton) view.findViewById(R.id.fab_creatClass);
        Log.d(TAG, "Flag1");

        int itemSpace = 10;
        mMainList.addItemDecoration(new SpacesItemDecoration(itemSpace));




        db.collection("Class").whereEqualTo("teacher_email", teacher_email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                if (classList.isEmpty()) {
                    mMainList.setVisibility(View.GONE);
                    imNoData.setVisibility(View.VISIBLE);
                }
                else {
                    mMainList.setVisibility(View.VISIBLE);
                    imNoData.setVisibility(View.GONE);
                }

            }
        });
        fabCreateClass.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity(), CreateClassSt1.class);
            startActivity(intent);
        });

        classListAdapter.setOnTransPageClickListener(new ClassListAdapter.transPageListener() {

            @Override
            public void onTransPageClick(String classId2) {
                Log.d(TAG,"onTransPageClick0" +classId2);
                mCallback.onFragmentSelected(classId2 , "toClassListDetail");//fragment傳值


            }

        });//Fragment換頁




    }

    @Override
    public boolean onBackPressed() {
        return BackHandlerHelper.handleBackPress(this);
    }//fragment 返回鍵



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
