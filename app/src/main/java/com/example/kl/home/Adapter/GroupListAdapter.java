package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.kl.home.GroupPage;
import com.example.kl.home.Model.Group;
import com.example.kl.home.Model.Student;
import com.example.kl.home.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.List;

import javax.annotation.Nullable;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    final String TAG = "GroupListAdapter";
    public Context context;
    private GroupListAdapter.transPageListener mTransPageListener;//adapter跳轉fragment
    public List<Group> groupList;
    FirebaseFirestore db ;
    String studentId,groupLeaderName;

    public GroupListAdapter(GroupPage groupPage, List<Group> groupList){
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

        Integer groupNumber = Integer.parseInt(groupList.get(position).getGroup_num());
        String groupLeader = groupList.get(position).getGroup_leader();
        String groupBonus = groupList.get(position).getGroup_bonus();
        String groupId = groupList.get(position).groupId;
        switch (groupNumber){
            case 0:
                holder.tvGroupNum.setText("第一組");
                break;
            case 1:
                holder.tvGroupNum.setText("第二組");
                break;
            case 2:
                holder.tvGroupNum.setText("第三組");
                break;

        }
        holder.tvGroupBonus.setText("回答次數\t\t"+groupBonus);
        List<String> student_Id  =groupList.get(position).getStudent_id();
        String[] studentIdArra = student_Id.toArray(new String[student_Id.size()]);
        db = FirebaseFirestore.getInstance();
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


        holder.mView.setOnClickListener(v -> {
            notifyItemChanged(position);
            mTransPageListener.onTransPageClick(studentIdArra,groupLeader,groupBonus,groupId,groupList.get(position));
       });
    }
    public interface transPageListener {
        void onTransPageClick(String[] studentIdArra,String groupLeader, String groupBonus,String groupId, Group group);
    }//adapter跳轉fragment並攜帶需要的資料

    public void setOnTransPageClickListener(GroupListAdapter.transPageListener transPageListener) {
        this.mTransPageListener = transPageListener;
    }//adapter跳轉fragment

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
