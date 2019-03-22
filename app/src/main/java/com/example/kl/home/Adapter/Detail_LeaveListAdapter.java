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

import java.text.SimpleDateFormat;
import java.util.List;

public class Detail_LeaveListAdapter extends RecyclerView.Adapter<Detail_LeaveListAdapter.ViewHolder>{
    public Context context;
    public List<Leave> leaveList;
//    private StudentListAdapter.transPageListener mTransPageListener;//adapter跳轉fragment

    public Detail_LeaveListAdapter(Context context, List<Leave> leaveList) {
        this.context = context;
        this.leaveList = leaveList;
    }

    @NonNull
    @Override
    public Detail_LeaveListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_detail_leavelist_item, parent, false);
        return new Detail_LeaveListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Detail_LeaveListAdapter.ViewHolder holder, int position) {

        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy/MM/dd");
        String leaveDate= myFmt2.format(leaveList.get(position).getLeave_uploaddate());
        holder.leave_date.setText(leaveDate);
        holder.leave_reason.setText(leaveList.get(position).getLeave_reason());
        String leaveId = leaveList.get(position).LeaveId;
        holder.chectBtn.setOnClickListener(v -> {
            Toast.makeText(context,"Id  :  " + leaveId, Toast.LENGTH_SHORT).show();

            Context context = v.getContext();

            Intent intent = new Intent();
            intent.setClass(context, LeaveRecord.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", leaveId);
            bundle.putString("ChangePage","Detail");//
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
        public TextView leave_reason;
        public TextView leave_date;
        public Button chectBtn;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            leave_date = (TextView) mView.findViewById(R.id.textViewLeaveDate);
            leave_reason = (TextView) mView.findViewById(R.id.textViewLeaveReason);
            chectBtn = (Button) mView.findViewById(R.id.checkBtn);
        }
    }

}
