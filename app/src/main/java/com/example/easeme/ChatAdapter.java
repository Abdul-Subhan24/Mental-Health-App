package com.example.easeme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<MessageModel> messageList=new ArrayList<>();
    FirebaseAuth auth;

    public ChatAdapter(Context context, ArrayList<MessageModel> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    int SENDER_HOLDER=0;
    int REC_HOLDER=1;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType==SENDER_HOLDER){
            view= LayoutInflater.from(context).inflate(R.layout.senderlayout,parent,false);

            return new SenderViewHolder(view);
        }else{
            view= LayoutInflater.from(context).inflate(R.layout.recieverlayout,parent,false);
            return new RecieveViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SimpleDateFormat format=new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat format2=new SimpleDateFormat("dd MMMM yyyy");
        String time,date;




        if(holder.getClass()==SenderViewHolder.class){

         ((SenderViewHolder) holder).message.setText(messageList.get(position).getMessage());



         if(messageList.size()==1){
             time= format.format(messageList.get(position).getTimestamp());
             ((SenderViewHolder) holder).time.setText(time);
             ((SenderViewHolder) holder).time.setVisibility(View.VISIBLE);
         }else{

             if(position!=0 && sametimecheck(messageList.get(position).getTimestamp(),messageList.get(position-1).getTimestamp())!=true){
                 time= format.format(messageList.get(position).getTimestamp());
                 ((SenderViewHolder) holder).time.setText(time);
                 ((SenderViewHolder) holder).time.setVisibility(View.VISIBLE);
             }


         }



            if(position==0){
                date= format2.format(messageList.get(position).getTimestamp());
                ((SenderViewHolder) holder).senddate.setText(date);
                ((SenderViewHolder) holder).senddate.setVisibility(View.VISIBLE);
            }
            else if(position!=0 && samedaycheck(messageList.get(position).getTimestamp(),messageList.get(position-1).getTimestamp())!=true){
                date= format2.format(messageList.get(position).getTimestamp());
                ((SenderViewHolder) holder).senddate.setText(date);
                ((SenderViewHolder) holder).senddate.setVisibility(View.VISIBLE);
            }

        }else{


            ((RecieveViewHolder) holder).recmessage.setText(messageList.get(position).getMessage());





            if(messageList.size()==1){
                time= format.format(messageList.get(position).getTimestamp());
                ((RecieveViewHolder) holder).rectime.setText(time);
                ((RecieveViewHolder) holder).rectime.setVisibility(View.VISIBLE);
            }else{

                if(position!=0 && sametimecheck(messageList.get(position).getTimestamp(),messageList.get(position-1).getTimestamp())!=true){
                    time= format.format(messageList.get(position).getTimestamp());
                    ((RecieveViewHolder) holder).rectime.setText(time);
                    ((RecieveViewHolder) holder).rectime.setVisibility(View.VISIBLE);
                }


            }




            if(position==0){
                date= format2.format(messageList.get(position).getTimestamp());
                ((RecieveViewHolder) holder).recdate.setText(date);
                ((RecieveViewHolder) holder).recdate.setVisibility(View.VISIBLE);

            } else if(position!=0 && samedaycheck(messageList.get(position).getTimestamp(),messageList.get(position-1).getTimestamp())!=true){
                date= format2.format(messageList.get(position).getTimestamp());
                ((RecieveViewHolder) holder).recdate.setText(date);
                ((RecieveViewHolder) holder).recdate.setVisibility(View.VISIBLE);
            }


        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    public boolean samedaycheck(long time1,long time2){
        SimpleDateFormat formatter=new SimpleDateFormat("dd MMMM yyyy");

        String day1,day2;

        day1= formatter.format(time1);
        day2= formatter.format(time2);



        return Objects.equals(day1, day2);


    }
    public boolean sametimecheck(long time1,long time2){
        SimpleDateFormat formatter=new SimpleDateFormat("hh:mm aa");

        String day1,day2;

        day1= formatter.format(time1);
        day2= formatter.format(time2);



        return Objects.equals(day1, day2);


    }


    @Override
    public int getItemViewType(int position) {
        auth=FirebaseAuth.getInstance();
        MessageModel model =messageList.get(position);

        if(Objects.equals(model.senderId, auth.getUid())) return SENDER_HOLDER;
        else {
            return REC_HOLDER;
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView message,senddate;
        TextView time;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.text_gchat_message_me);
            time=itemView.findViewById(R.id.text_gchat_timestamp_me);
            senddate=itemView.findViewById(R.id.text_gchat_date_me);


        }
    }
    public class RecieveViewHolder extends RecyclerView.ViewHolder{
        TextView recmessage,recdate;
        TextView rectime;

        public RecieveViewHolder(@NonNull View itemView) {
            super(itemView);
            recmessage=itemView.findViewById(R.id.text_gchat_message_other);
            rectime=itemView.findViewById(R.id.text_gchat_timestamp_other);
            recdate=itemView.findViewById(R.id.text_gchat_date_other);




        }
    }
}
