package com.example.kl.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.kl.home.Adapter.LeaveListAdapter;
import com.example.kl.home.Model.Leave;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Fragment_LeaveList extends Fragment {
    private static final String TAG = "Ｆirelog";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private LeaveListAdapter leaveListAdapter;
    private List<Leave> leaveList;

    private Spinner leaveListWaySpinner;
    private String[] leaveListWay = {"所有假單", "未審核", "已審核"};
    private ArrayAdapter<String> listAdapterLeave;//假單排列選擇


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__leave_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        leaveList = new ArrayList<>();
        leaveListAdapter = new LeaveListAdapter(view.getContext(),leaveList);

        mFirestore = FirebaseFirestore.getInstance();

        mMainList = (RecyclerView) view.findViewById(R.id.leave_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(leaveListAdapter);

        leaveListWaySpinner = (Spinner) view.findViewById(R.id.spinner_LeaveListWay);
        listAdapterLeave = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.leave_spinner_style, leaveListWay);//spinner值
        listAdapterLeave.setDropDownViewResource(R.layout.leave_spinner_style);

        leaveListWaySpinner.setAdapter(listAdapterLeave);
        leaveListWaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortWay =  leaveListWaySpinner.getSelectedItem().toString();
                leaveList.clear();
                if(sortWay.equals("未審核")){
                    mFirestore.collection("Leave").whereEqualTo("leave_check","核准中").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(e != null){
                                Log.d(TAG,"error" + e.getMessage());
                            }
                            leaveList.clear();
                            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                                if(doc.getType() == DocumentChange.Type.ADDED){

                                    String leaveRecordId = doc.getDocument().getId();

                                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveRecordId);
                                    leaveList.add(leave);

                                    leaveListAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
                }
                else if(sortWay.equals("已審核")){
                    mFirestore.collection("Leave").whereEqualTo("leave_check","准假").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(e != null){
                                Log.d(TAG,"error" + e.getMessage());
                            }
                            leaveList.clear();
                            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                                if(doc.getType() == DocumentChange.Type.ADDED){

                                    String leaveId = doc.getDocument().getId();

                                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveId);
                                    leaveList.add(leave);

                                    leaveListAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
                    mFirestore.collection("Leave").whereEqualTo("leave_check","不准假").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(e != null){
                                Log.d(TAG,"error" + e.getMessage());
                            }
                            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                                if(doc.getType() == DocumentChange.Type.ADDED){

                                    String leaveId = doc.getDocument().getId();

                                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveId);
                                    leaveList.add(leave);

                                    leaveListAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
                }
                else if(sortWay.equals("所有假單")){
                    mFirestore.collection("Leave").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(e != null){
                                Log.d(TAG,"error" + e.getMessage());
                            }
                            leaveList.clear();
                            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                                if(doc.getType() == DocumentChange.Type.ADDED){

                                    String leaveId = doc.getDocument().getId();

                                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveId);
                                    leaveList.add(leave);

                                    leaveListAdapter.notifyDataSetChanged();
                                }

                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        /*mFirestore.collection("Leave").whereEqualTo("a","A").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(e != null){
                        Log.d(TAG,"error" + e.getMessage());
                    }
                leaveRecordList.clear();
            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                        if(doc.getType() == DocumentChange.Type.ADDED){

                            String leaveRecordId = doc.getDocument().getId();

                            LeaveRecord leaverecord = doc.getDocument().toObject(LeaveRecord.class).withId(leaveRecordId);
                            leaveRecordList.add(leaverecord);

                        leaveRecordListAdapter.notifyDataSetChanged();
                    }

                }
            }
        });*/

    }


}
