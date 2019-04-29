package com.example.kl.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kl.home.GroupDetail;
import com.example.kl.home.GroupDetailSetting;
import com.example.kl.home.GroupPage;
import com.example.kl.home.Model.Class;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class GroupDetailSettingAdapter extends RecyclerView.Adapter<GroupDetailSettingAdapter.ViewHolder> {

    public Context context;
    FirebaseFirestore db;
    public List<Student> studentList;
    String TAG = "GroupDetailAdapter";
    String groupLeader;

    private GroupDetailSettingAdapter.transPageListener mTransPageListener;//adapter跳轉fragment


    public GroupDetailSettingAdapter(GroupDetailSetting groupDetailSetting, List<Student> studentList, String groupLeader ) {
        this.context = groupDetailSetting;
        this.studentList = studentList;
        this.groupLeader = groupLeader;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_detail_setting_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");
            holder.tvGroupDetailName.setText(String.format("%s\t\t%s\t\t%s",
                    studentList.get(position).getStudent_id(),
                    studentList.get(position).getStudent_department(),
                    studentList.get(position).getStudent_name()));
        holder.ibDeleteStudent.setOnClickListener(v -> {
            notifyItemChanged(position);
            String student_Id = studentList.get(position).getStudent_id();
            mTransPageListener.onTransPageClick(student_Id,studentList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG,"getItemCount");
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView tvGroupDetailName;
        public ImageButton ibDeleteStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvGroupDetailName = mView.findViewById(R.id.groupDetailName);
            ibDeleteStudent = mView.findViewById(R.id.deleteStudent);

        }
    }

    public interface transPageListener {
        void onTransPageClick(String student_Id, Student student);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(GroupDetailSettingAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment

}
