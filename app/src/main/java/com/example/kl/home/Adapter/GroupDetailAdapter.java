package com.example.kl.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kl.home.GroupDetail;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;

public class GroupDetailAdapter extends RecyclerView.Adapter<GroupDetailAdapter.ViewHolder> {

    public Context context;
    private List<Student> studentList;
    private String TAG = "GroupDetailAdapter";
    private String studentId;
    private String student_id;

    private GroupDetailAdapter.transPageListener mTransPageListener;//adapter跳轉fragment


    public GroupDetailAdapter(GroupDetail groupDetail, List<Student> studentList) {
        this.context = groupDetail;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        holder.tvGroupDetailName.setText(String.format("%s\t\t%s\t\t%s", studentList.get(position).getStudent_id(),
                studentList.get(position).getStudent_department(), studentList.get(position).getStudent_name()));

        student_id = studentList.get(position).getStudent_id();
        studentId = studentList.get(position).StudentId;
        holder.btCheckStudent.setOnClickListener(v -> {
            mTransPageListener.onTransPageClick(studentId, student_id);
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount\t" + studentList.size());
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView tvGroupDetailName;
        Button btCheckStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvGroupDetailName = mView.findViewById(R.id.groupDetailName);
            btCheckStudent = mView.findViewById(R.id.checkStudent);

        }
    }

    public interface transPageListener {
        void onTransPageClick(String studentId, String student_id);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(GroupDetailAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment

}
