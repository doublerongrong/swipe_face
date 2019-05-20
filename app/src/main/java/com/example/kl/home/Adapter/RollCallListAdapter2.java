package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kl.home.Model.Student;
import com.example.kl.home.R;

import java.util.List;

public class RollCallListAdapter2 extends RecyclerView.Adapter<RollCallListAdapter2.ViewHolder>{

    @NonNull

    private RollCallListAdapter2.transPageListener mTransPageListener;//adapter跳轉fragment
    public List<Student> Student_stateList;
    public Context context;
    public String state;

    public RollCallListAdapter2(Context context, List<Student> Student_stateList, String state) {

        this.Student_stateList = Student_stateList;
        this.context =context;
        this.state = state;

    }


    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rollcalllist_item2, parent, false);
        return new RollCallListAdapter2.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rollcall_name.setText(Student_stateList.get(position).getStudent_name());
        holder.rollcall_id.setText(Student_stateList.get(position).getStudent_id());
        holder.rollcall_department.setText(Student_stateList.get(position).getStudent_department());
        holder.rollcall_state.setText(state);
        holder.checkAttendBtn.setOnClickListener(v -> {
            notifyItemChanged(position);
            mTransPageListener.onTransPageClick(Student_stateList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return Student_stateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button rollcall_state;
        TextView rollcall_name;
        TextView rollcall_id;
        TextView rollcall_department;
        Button checkAttendBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            rollcall_state = mView.findViewById(R.id.rollcall_state);
            rollcall_name = mView.findViewById(R.id.rollcall_name);
            rollcall_id = mView.findViewById(R.id.rollcall_id);
            rollcall_department = mView.findViewById(R.id.rollcall_department);
            checkAttendBtn = mView.findViewById(R.id.rollcall_state);

        }

    }

    public interface transPageListener {
        void onTransPageClick(Student student);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(RollCallListAdapter2.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment






}
