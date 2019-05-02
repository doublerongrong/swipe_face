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
import com.example.kl.home.R;

import java.util.List;

public class RollCallListAdapter3 extends RecyclerView.Adapter<RollCallListAdapter3.ViewHolder>{

    @NonNull

    private RollCallListAdapter3.transPageListener mTransPageListener;//adapter跳轉fragment
    private onClickMyButton onClickMyButton;
    public List<RollCallList> Student_stateList;
    public Context context;
    public String state;

    public interface onClickMyButton{
        public void myButton(int id);
    }
    public void setOnClickMyButton(onClickMyButton onClickMyButton) {
        this.onClickMyButton = onClickMyButton;
    }

    public RollCallListAdapter3(Context context,List<RollCallList> Student_stateList,String state) {

        this.Student_stateList = Student_stateList;
        this.context =context;
        this.state = state;

    }


    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rollcalllist_item3, parent, false);
        return new RollCallListAdapter3.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.rollcall_name.setText(Student_stateList.get(position).getRollcall_name());
        holder.rollcall_id.setText(Student_stateList.get(position).getRollcall_id());
        holder.rollcall_department.setText(Student_stateList.get(position).getRollcall_department());
        holder.rollcall_state.setText(state);
        holder.checkAttendBtn.setTag(position);
        holder.checkAttendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Toast.makeText(context,"Id  :  " + rollcallId + student_id, Toast.LENGTH_SHORT).show();
                int position = (int) v.getTag();
                //Toast.makeText(v.getContext(),Integer.toString(position),Toast.LENGTH_SHORT).show();
                onClickMyButton.myButton(position);
                notifyItemChanged(position);
                mTransPageListener.onTransPageClick();
            }
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
        public void onTransPageClick();
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(RollCallListAdapter3.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment

}