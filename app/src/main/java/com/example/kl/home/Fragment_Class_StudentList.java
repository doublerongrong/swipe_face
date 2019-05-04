package com.example.kl.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.Adapter.StudentListAdapter;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Fragment_Class_StudentList extends Fragment implements FragmentBackHandler {
    private static final String TAG = "LeaveList";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private StudentListAdapter studentListAdapter;
    private List<Student> studentList;

    private TextView textViewClassName;
    private TextView textViewStugentCount;

    private String classid;
    private String class_id;

    private ImageButton backIBtn,serchBtn;
    SearchManager searchManager;

    OnFragmentSelectedListener mCallback;//Fragment傳值

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        Log.d(TAG, "Bundle " + new Bundle());
//        Log.d(TAG,"args0: "+args.toString());
        if (args != null) {
            classid = args.getString("info");
            Log.d(TAG, "args: " + args.toString());
        } else {
            classid = null;
        }

        Log.d(TAG, "class_id:" + classid);//fragment傳值
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_studentlist, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        studentList = new ArrayList<>();
        studentListAdapter = new StudentListAdapter(view.getContext(), studentList);

        mFirestore = FirebaseFirestore.getInstance();

        mMainList = (RecyclerView) view.findViewById(R.id.class_student_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(studentListAdapter);

        textViewClassName = (TextView) view.findViewById(R.id.textViewClassName);
        textViewStugentCount = (TextView) view.findViewById(R.id.textViewStudentCount);

        backIBtn = (ImageButton) view.findViewById(R.id.backIBtn);
        serchBtn = (ImageButton) view.findViewById(R.id.searchBtn);

        getClassInfor(classid);

        backIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onFragmentSelected(classid, "toClassListDetail");//fragment傳值
            }
        });

        serchBtn.setOnClickListener(view1 -> {

           // searchManager.startSearch();
        });



    }


    private void getClassInfor(String classId) {
        DocumentReference docRef = mFirestore.collection("Class").document(classId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Class setClass = document.toObject(Class.class);
                        textViewClassName.setText(setClass.getClass_name());
                        textViewStugentCount.setText(setClass.getStudent_total().toString());

                        class_id = setClass.getClass_id();

                        List<String> studentListStr = setClass.getStudent_id();
                        for (String student : studentListStr) {
                            db.collection("Student").whereEqualTo("student_id", student).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                    if (e != null) {

                                        Log.d(TAG, "Error :" + e.getMessage());
                                    }

                                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                                        if (doc.getType() == DocumentChange.Type.ADDED) {
                                            String studentId = doc.getDocument().getId();
                                            Student aStudent = doc.getDocument().toObject(Student.class).withId(studentId);
                                            Log.d(TAG, "DB2 studentId:" + studentId);
                                            studentList.add(aStudent);
                                            studentListAdapter.notifyDataSetChanged();
                                        }
                                    }

                                }
                            });
                        }


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        studentListAdapter.setOnTransPageClickListener((studentId, student_id, student) -> {
            Log.d(TAG,"onTransPageClickTEST" + studentId);

//                singleClick(view);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            intent.setClass(getActivity(), Activity_StudentDetail.class);
            bundle.putString("PassStudentId", studentId);
            bundle.putString("PassStudent_id", student_id);
            bundle.putSerializable("PassStudent", student);
            bundle.putString("PassClass_id" ,class_id);
            intent.putExtras(bundle);
            startActivity(intent);



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
                mCallback = (OnFragmentSelectedListener) context;//fragment傳值
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
            }
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        // 顯示完成鈕
        searchView.setSubmitButtonEnabled(true);

        return true;
    }


}
