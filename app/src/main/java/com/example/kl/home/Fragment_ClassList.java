package com.example.kl.home;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_ClassList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_ClassList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_ClassList extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView mMainList;
    private ClassListAdapter classListAdapter;
    private List<Class> classList;
    private String TAG = "FLAG";
    private String teacher_email = "053792@mail.fju.edu.tw";

    private Teacher teacher;
    private ArrayList<String> class_id = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__class_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        db = FirebaseFirestore.getInstance();

        classList = new ArrayList<>();
        classListAdapter = new ClassListAdapter(classList);

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

                        Class aClass = doc.getDocument().toObject(Class.class);

                        classList.add(aClass);
                        classListAdapter.notifyDataSetChanged();

                    }
                }

            }
        });



    }


}
