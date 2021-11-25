package com.menlopark.rentsyuser.ui.activitys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.menlopark.rentsyuser.R;
import com.menlopark.rentsyuser.model.Help_QuestionAnswer.Detail;

import java.util.ArrayList;

public class QusAws_Adapter extends RecyclerView.Adapter<QusAws_Adapter.MyViewHolder> {

    private Context mContext;
    ArrayList<Detail> Que_ans_List;

    public QusAws_Adapter(Question_Activity question_activity, ArrayList<Detail> que_ans_list) {
        this.mContext = question_activity;
        this.Que_ans_List = que_ans_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_question, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
      Detail  que_ans_List = Que_ans_List.get(position);
      holder.tv_question.setText(que_ans_List.getQuestion());
      holder.tv_anw.setText(que_ans_List.getAnswer());
    }

    @Override
    public int getItemCount() {
        return Que_ans_List.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_question,tv_anw;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            tv_anw = itemView.findViewById(R.id.tv_anw);
        }
    }
}
