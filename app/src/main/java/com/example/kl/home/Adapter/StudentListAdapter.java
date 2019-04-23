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

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    public Context context;
    public List<Student> studentList;
    private String studentId;

    private StudentListAdapter.transPageListener mTransPageListener;//adapter跳轉fragment

    public StudentListAdapter(Context context, List<Student> studentList) {
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_studentlist_item, parent, false);
        return new StudentListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.student_id.setText(studentList.get(position).getStudent_id());
        holder.student_department.setText(studentList.get(position).getStudent_department());
        holder.student_name.setText(studentList.get(position).getStudent_name());

        String studentId = studentList.get(position).StudentId;
        String student_id = studentList.get(position).getStudent_id();
        holder.checkBtn.setOnClickListener(v -> {


            notifyItemChanged(position);
            mTransPageListener.onTransPageClick(studentId, student_id, studentList.get(position));

        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView student_id;
        public TextView student_department;
        public TextView student_name;
        public Button checkBtn;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            student_id = (TextView) mView.findViewById(R.id.textViewStudentId);
            student_department = (TextView) mView.findViewById(R.id.textViewStudentDe);
            student_name = (TextView) mView.findViewById(R.id.textViewStudentName);
            checkBtn = (Button) mView.findViewById(R.id.BtnCheck);

        }
    }
    public interface transPageListener {
        void onTransPageClick(String studentId, String student_id, Student student);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(StudentListAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment
}
