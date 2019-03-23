package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kl.home.GroupDetail;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class GroupListDetailAdapter extends RecyclerView.Adapter<GroupListDetailAdapter.ViewHolder> {

    public Context context;
    FirebaseFirestore db;
    public List<Student> studentList;
    String TAG = "GroupListDetailAdapter";
    String groupLeader;

    public GroupListDetailAdapter(GroupDetail groupDetail, List<Student> studentList,String groupLeader ) {
        this.context = groupDetail;
        this.studentList = studentList;
        this.groupLeader = groupLeader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");
            holder.tvGroupDetailName.setText(String.format("%s\t\t%s\t\t%s", studentList.get(position).getStudent_id(),
                    studentList.get(position).getStudent_department(), studentList.get(position).getStudent_name()));
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"getItemCount\t"+studentList.size());
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView tvGroupDetailName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvGroupDetailName = mView.findViewById(R.id.groupDetailName);
        }
    }

}
