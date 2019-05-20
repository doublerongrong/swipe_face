package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kl.home.Model.RollCallList;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;

import java.util.List;

public class RollCallListAdapter extends RecyclerView.Adapter<RollCallListAdapter.ViewHolder>{

    @NonNull

    private RollCallListAdapter.transPageListener mTransPageListener;//adapter跳轉fragment
    private onClickMyButton onClickMyButton;
    public List<Student> Student_stateList;
    public Context context;
    public String state;

    public interface onClickMyButton{
        void myButton(int id);
    }
    public void setOnClickMyButton(onClickMyButton onClickMyButton) {
        this.onClickMyButton = onClickMyButton;
    }

    public RollCallListAdapter(Context context,List<Student> Student_stateList,String state) {

        this.Student_stateList = Student_stateList;
        this.context =context;
        this.state = state;

    }


    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rollcalllist_item, parent, false);
        return new RollCallListAdapter.ViewHolder(view);
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
        public Button rollcall_state;
        public TextView rollcall_name;
        public TextView rollcall_id;
        public TextView rollcall_department;
        public Button checkAttendBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            rollcall_state = (Button) mView.findViewById(R.id.rollcall_state);
            rollcall_name = (TextView) mView.findViewById(R.id.rollcall_name);
            rollcall_id = (TextView) mView.findViewById(R.id.rollcall_id);
            rollcall_department = (TextView) mView.findViewById(R.id.rollcall_department);
            checkAttendBtn = (Button)mView.findViewById(R.id.rollcall_state);

        }

    }

    public interface transPageListener {
        void onTransPageClick(Student student);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(RollCallListAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment






}

