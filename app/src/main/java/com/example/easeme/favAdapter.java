package com.example.easeme;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class favAdapter extends RecyclerView.Adapter<favAdapter.FavViewHolder> {

    Context context;
    List<UserModel> modelList=new ArrayList<>();


    public favAdapter(Context context, List<UserModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    FirebaseDatabase database;
    FirebaseAuth auth;
    ImageView crossicon;
    TextView d_name, d_age,d_gender,d_profession;
    int[] role_icons={R.drawable.male_patient,R.drawable.female_user,R.drawable.male_therapist,R.drawable.female_therapist};
    int[] status_icons={R.drawable.online_icon,R.drawable.offline_icon};
    int[] fav_icons_list={R.drawable.fav_tick,R.drawable.fav_dark};

    @NonNull
    @Override
    public favAdapter.FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.favselect_model,parent,false);

        return new favAdapter.FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favAdapter.FavViewHolder holder, int position) {
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        UserModel user= modelList.get(position);
        holder.username.setText(user.getFname());







        holder.infoicon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CutPasteId")
            @Override
            public void onClick(View v) {
                RecyclerView qsetrecycler;
                Dialog dialog=new Dialog(v.getContext());
                dialog.setContentView(R.layout.info_dialog);

                qsetrecycler=dialog.findViewById(R.id.qsetrecycler);
                crossicon=dialog.findViewById(R.id.crossicon);

                d_name=dialog.findViewById(R.id.dialog_name);
                d_gender=dialog.findViewById(R.id.dialog_gender);
                d_age=dialog.findViewById(R.id.dialog_age);
                d_profession=dialog.findViewById(R.id.dialog_profession);

                d_name.setText(user.getFname());
                d_age.setText(user.getAge());
                d_gender.setText(user.getGender());
                d_gender.setText(user.getProfession());



                List<String> qlist=new ArrayList<>();
                List<String> anslist=new ArrayList<>();
                QsetAdapter adapter =new QsetAdapter(qlist,anslist, dialog.getContext());

                database=FirebaseDatabase.getInstance();

                database.getReference("Question Sets").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qlist.clear();
                        anslist.clear();
                        QsetModel model=snapshot.getValue(QsetModel.class);

                        qlist.addAll(model.getQlist());
                        anslist.addAll(model.getAnslist());





                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                qsetrecycler.setAdapter(adapter);
                qsetrecycler.setLayoutManager(new LinearLayoutManager(dialog.getContext(),RecyclerView.VERTICAL,false));
                crossicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        database.getReference("Favourites").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).exists()){



                    holder.favicon.setImageResource(fav_icons_list[0]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.favicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               database.getReference("Favourites").child(auth.getUid()).child(user.getUid()).removeValue();
                holder.favicon.setImageResource(fav_icons_list[1]);


//               ADDING FAVOURITES USER AND CHANGING ICON
//                database.getReference("Favourites").child(auth.getUid()).child(user.getUid()).setValue(user);






            }
        });




    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }


    public class  FavViewHolder extends RecyclerView.ViewHolder{
        LinearLayout userlayout,info_icon_layout;
        TextView username;
        ImageView infoicon,favicon;
        ImageView userrole;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            userlayout=itemView.findViewById(R.id.userlayout);
            username=itemView.findViewById(R.id.username);
            infoicon=itemView.findViewById(R.id.infoicon);
            userrole=itemView.findViewById(R.id.roleicon);
            info_icon_layout=itemView.findViewById(R.id.info_icon_layout);
            favicon=itemView.findViewById(R.id.favicon);

        }
    }

}
