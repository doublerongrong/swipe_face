package com.example.kl.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
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
    private static final String TAG = "LeaveList";
    private RecyclerView mMainList;
    private FirebaseFirestore mFirestore;
    private LeaveListAdapter leaveListAdapter;
    private List<Leave> leaveList;

    private Spinner leaveListWaySpinner;
    private String[] leaveListWay = {"所有假單", "未審核", "已審核"};
    private ArrayAdapter<String> listAdapterLeave;//假單排列選擇
    private String class_id;
    private String teacher_email;

    OnFragmentSelectedListener mCallback;//Fragment傳值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = new Bundle();//fragment傳值
        args = getArguments();//fragment傳值
        Log.d(TAG,"Bundle "+new Bundle());
//        Log.d(TAG,"args0: "+args.toString());
        if (args!=null){
            class_id = args.getString("info");
            teacher_email = args.getString("teacher_email");
            Log.d(TAG,"args: "+args.toString());

        }else {
            class_id = null;
        }

        Log.d(TAG, "class_id:" + class_id);//fragment傳值


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment__leave_list, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        leaveList = new ArrayList<>();
        leaveListAdapter = new LeaveListAdapter(view.getContext(),leaveList);

        mFirestore = FirebaseFirestore.getInstance();

        mMainList = view.findViewById(R.id.leave_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMainList.setAdapter(leaveListAdapter);

        leaveListWaySpinner =  view.findViewById(R.id.spinner_LeaveListWay);
        listAdapterLeave = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.leave_spinner_style, leaveListWay);//spinner值
        listAdapterLeave.setDropDownViewResource(R.layout.leave_spinner_style);

        leaveListWaySpinner.setAdapter(listAdapterLeave);

        if (class_id == null) {
            setAllLave();
        }else {
            setClassLeave();
        }



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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnFragmentSelectedListener) context;//fragment傳值
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Mush implement OnFragmentSelectedListener ");
        }
    }

    private void setAllLave(){
        leaveListWaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortWay =  leaveListWaySpinner.getSelectedItem().toString();
                leaveList.clear();
                if(sortWay.equals("未審核")){
                    leaveList.clear();
                    mFirestore.collection("Leave").whereEqualTo("leave_check","核准中").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                            if(e != null){
                                Log.d(TAG,"error00" + e.getMessage());
                            }

                            for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                                if(doc.getType() == DocumentChange.Type.ADDED){
                                    Log.d(TAG,"here" );

                                    String leaveRecordId = doc.getDocument().getId();

                                    Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveRecordId);
                                    leaveList.add(leave);

                                    leaveListAdapter.notifyDataSetChanged();
                                }

                            }
                            if(leaveList.isEmpty()){
                                Log.d(TAG,"here0" );

                                leaveListAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
                else if(sortWay.equals("已審核")){
                    leaveList.clear();
                    mFirestore.collection("Leave").whereEqualTo("leave_check","准假").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    leaveList.clear();
                    mFirestore.collection("Leave").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

   private void setClassLeave(){
       leaveListWaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String sortWay =  leaveListWaySpinner.getSelectedItem().toString();
               leaveList.clear();
               if(sortWay.equals("未審核")){
                   leaveList.clear();
                   mFirestore.collection("Leave").whereEqualTo("class_id",class_id).whereEqualTo("leave_check","核准中").addSnapshotListener(new EventListener<QuerySnapshot>() {
                       @Override
                       public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                           if(e != null){
                               Log.d(TAG,"error00" + e.getMessage());
                           }

                           for(DocumentChange doc : documentSnapshots.getDocumentChanges()){

                               if(doc.getType() == DocumentChange.Type.ADDED){
                                   Log.d(TAG,"here" );

                                   String leaveRecordId = doc.getDocument().getId();

                                   Leave leave = doc.getDocument().toObject(Leave.class).withId(leaveRecordId);
                                   leaveList.add(leave);

                                   leaveListAdapter.notifyDataSetChanged();
                               }

                           }
                           if(leaveList.isEmpty()){
                               Log.d(TAG,"here0" );

                               leaveListAdapter.notifyDataSetChanged();
                           }
                       }
                   });

               }
               else if(sortWay.equals("已審核")){
                   leaveList.clear();
                   mFirestore.collection("Leave").whereEqualTo("class_id",class_id).whereEqualTo("leave_check","准假" ).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                   mFirestore.collection("Leave").whereEqualTo("class_id",class_id).whereEqualTo("leave_check","不准假").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                   leaveList.clear();
                   mFirestore.collection("Leave").whereEqualTo("class_id",class_id).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });


   }


}
