package com.example.kl.home.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.LeaveRecord;
import com.example.kl.home.Model.Leave;
import com.example.kl.home.R;

import java.util.List;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.ViewHolder> {

    public Context context;

    public List<Leave> leaveList;

    public LeaveListAdapter(Context context, List<Leave> leaveList) {
        this.leaveList = leaveList;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leavelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String checkColor = leaveList.get(position).getLeave_check();

        holder.class_name.setText(leaveList.get(position).getClass_name());
        holder.student_name.setText(leaveList.get(position).getStudent_name());
        holder.leave_reason.setText(leaveList.get(position).getLeave_reason());
        holder.leave_check.setText(leaveList.get(position).getLeave_check());
        if (checkColor.equals("准假")) {
            holder.leave_check.setTextColor(context.getResources().getColor(R.color.leaveGreen));
            holder.leave_check.setBackground(context.getResources().getDrawable(R.drawable.leavebtngreen));
        } else if (checkColor.equals("不准假")) {
            holder.leave_check.setTextColor(context.getResources().getColor(R.color.leaveRed));
            holder.leave_check.setBackground(context.getResources().getDrawable(R.drawable.leavebtnred));
        } else {
            holder.leave_check.setTextColor(context.getResources().getColor(R.color.leaveBlue));
            holder.leave_check.setBackground(context.getResources().getDrawable(R.drawable.leavebtnblue));
        }
        holder.leave_date.setText(leaveList.get(position).getLeave_date());

        String leaveId = leaveList.get(position).LeaveId;
        String checkWay = leaveList.get(position).getCheckWay();

        holder.mView.setOnClickListener(v -> {
            Toast.makeText(context, "Id  :  " + leaveId, Toast.LENGTH_SHORT).show();

            Context context = v.getContext();

            Intent intent = new Intent();
            intent.setClass(context, LeaveRecord.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", leaveId);
            bundle.putString("ChangePage", checkWay);//
            intent.putExtras(bundle);
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView student_name;
        public TextView leave_reason;
        public Button leave_check;
        public TextView leave_date;
        public TextView class_name;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            student_name = (TextView) mView.findViewById(R.id.studentName);
            leave_reason = (TextView) mView.findViewById(R.id.leaveReason);
            leave_check = (Button) mView.findViewById(R.id.leaveCheck);
            leave_date = (TextView) mView.findViewById(R.id.leaveDate);
            class_name = (TextView) mView.findViewById(R.id.className);

        }
    }

}
