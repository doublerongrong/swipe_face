package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.kl.home.GroupNumberForCh;
import com.example.kl.home.GroupPage;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GroupPageAdapter extends RecyclerView.Adapter<GroupPageAdapter.ViewHolder> {
    private final String TAG = "GroupPageAdapter";
    public Context context;
    private GroupPageAdapter.transPageListener mTransPageListener;//adapter跳轉fragment
    private List<Group> groupList;
    private String studentId;
    String groupLeaderName;

    public GroupPageAdapter(GroupPage groupPage, List<Group> groupList){
        this.groupList = groupList;
        this.context = groupPage;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupNumberForCh groupNumberForCh = new GroupNumberForCh();

        Integer groupNumber = groupList.get(position).getGroup_num();
        Integer groupBonus = groupList.get(position).getGroup_bonus();
        String groupLeader = groupList.get(position).getGroup_leader();
        String groupId = groupList.get(position).groupId;
        holder.tvGroupNum.setText(groupNumberForCh.transNum(groupNumber));
        holder.tvGroupBonus.setText(String.format("回答次數\t\t%s", groupBonus.toString()));
        List<String> student_Id  =groupList.get(position).getStudent_id();
        String[] studentIdArra = student_Id.toArray(new String[student_Id.size()]);

        //init db
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Get 小組組長名字
        db.collection("Student").whereEqualTo("student_id",groupLeader).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.d(TAG, "Error :" + e.getMessage());
            }
            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                if (doc.getType() == DocumentChange.Type.ADDED ) {
                    studentId = doc.getDocument().getId();
                    Student student = doc.getDocument().toObject(Student.class).withId(studentId);
                    groupLeaderName = student.getStudent_name();
                    holder.tvGroupLeader.setText("組長\t\t"+groupLeaderName);
                }
            }
        });

        //回傳資料給GroupPage
        holder.mView.setOnClickListener(v -> {
            notifyItemChanged(position);
            mTransPageListener.onTransPageClick(groupLeader,groupBonus,groupId);
       });
    }

    //adapter跳轉fragment並攜帶需要的資料
    public interface transPageListener {
        void onTransPageClick(String groupLeader, Integer groupBonus,String groupId);
    }

    //adapter跳轉fragment
    public void setOnTransPageClickListener(GroupPageAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView tvGroupNum,tvGroupLeader,tvGroupBonus;

        public ViewHolder(@NonNull View itemView) {

            super(itemView) ;
            mView = itemView;
            tvGroupNum = mView.findViewById(R.id.groupNum);
            tvGroupLeader = mView.findViewById(R.id.groupLeader);
            tvGroupBonus = mView.findViewById(R.id.groupBonus);

        }
    }

}
