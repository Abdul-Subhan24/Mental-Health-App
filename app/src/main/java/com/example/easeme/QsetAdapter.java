package com.example.easeme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QsetAdapter extends RecyclerView.Adapter<QsetAdapter.QsetViewHolder> {
     List<String> quesList=new ArrayList<>();
     List<String> answerList=new ArrayList<>();
     Context context;

    public QsetAdapter(List<String> quesList, List<String> answerList, Context context) {
        this.quesList = quesList;
        this.answerList = answerList;
        this.context = context;
    }

    @NonNull
    @Override
    public QsetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view= LayoutInflater.from(context).inflate(R.layout.qset_layout,parent,false);
        return  new QsetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QsetViewHolder holder, int position) {
        holder.ques.setText(quesList.get(position));
        holder.ans.setText(answerList.get(position));


    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class QsetViewHolder extends RecyclerView.ViewHolder{
        TextView ques,ans;
        public QsetViewHolder(@NonNull View itemView) {
            super(itemView);
            ques=itemView.findViewById(R.id.question);
            ans=itemView.findViewById(R.id.answer);
        }
    }
}
