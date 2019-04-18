package com.example.kl.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.LeaveRecord;
import com.example.kl.home.Model.Leave;
import com.example.kl.home.R;

import java.util.List;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.ViewHolder> {

    public Context context;

    public List<Leave> leaveList;
    public LeaveListAdapter(Context context, List<Leave>leaveList){
        this.leaveList = leaveList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leavelist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.student_name.setText(leaveList.get(position).getStudent_name());
        holder.leave_reason.setText(leaveList.get(position).getLeave_reason());
        holder.leave_check.setText(leaveList.get(position).getLeave_check());
        holder.leave_date.setText(leaveList.get(position).getLeave_date());

        String leaveId = leaveList.get(position).LeaveId;
        holder.mView.setOnClickListener(v -> {
            Toast.makeText(context,"Id  :  " + leaveId, Toast.LENGTH_SHORT).show();

            Context context = v.getContext();

            Intent intent = new Intent();
            intent.setClass(context, LeaveRecord.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", leaveId);
            bundle.putString("ChangePage","Fragment");//
            intent.putExtras(bundle);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView student_name;
        public TextView leave_reason;
        public TextView leave_check;
        public TextView leave_date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            student_name = (TextView) mView.findViewById(R.id.studentName);
            leave_reason = (TextView) mView.findViewById(R.id.leaveReason);
            leave_check = (TextView) mView.findViewById(R.id.leaveCheck);
            leave_date = (TextView) mView.findViewById(R.id.leaveDate);

        }
    }

}
