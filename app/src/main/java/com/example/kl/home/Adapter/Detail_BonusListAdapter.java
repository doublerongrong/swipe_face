package com.example.kl.home.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kl.home.Model.Bonus;
import com.example.kl.home.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class Detail_BonusListAdapter extends RecyclerView.Adapter<Detail_BonusListAdapter.ViewHolder>{
    public Context context;
    public List<Bonus> bonusList;
    //private StudentListAdapter.transPageListener mTransPageListener;//adapter跳轉fragment

    public Detail_BonusListAdapter(Context context, List<Bonus> bonusList) {
        this.context = context;
        this.bonusList = bonusList;
    }

    @NonNull
    @Override
    public Detail_BonusListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_detail_bonuslist_item, parent, false);
        return new Detail_BonusListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Detail_BonusListAdapter.ViewHolder holder, int position) {

        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy/MM/dd");
        String bonusType = bonusList.get(position).getBonus_reason();

        String bonusTime = myFmt2.format(bonusList.get(position).getBonus_time());
        holder.bouns_reason.setText(bonusList.get(position).getBonus_reason());
        holder.bonus_time.setText(bonusTime);

        if(bonusType.equals("點人答題")){
            holder.plusBonus.setText(bonusList.get(position).getRDanswerBonus());
        }
        else if(bonusType.equals("回答問題")){
            holder.plusBonus.setText(bonusList.get(position).getAnswerBonus());
        }

    }

    @Override
    public int getItemCount() {
        return bonusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView bouns_reason;
        public TextView bonus_time;
        public TextView plusBonus;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

            bouns_reason = (TextView) mView.findViewById(R.id.textViewBonusReason);
            bonus_time = (TextView) mView.findViewById(R.id.textViewBonusTime);
            plusBonus = (TextView) mView.findViewById(R.id.plusBonus);
        }
    }

}

