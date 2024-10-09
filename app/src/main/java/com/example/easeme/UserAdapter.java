package com.example.easeme;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Objects;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

   Context context;
   List<UserModel> modelList=new ArrayList<>();


   public UserAdapter(Context context, List<UserModel> modelList) {
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
   public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(context).inflate(R.layout.userselectmodel,parent,false);

      return new UserViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
      database=FirebaseDatabase.getInstance();
      auth=FirebaseAuth.getInstance();
      UserModel user= modelList.get(position);
      holder.username.setText(user.getFname());


      if(user.isRole()){

      if(Objects.equals(user.getGender(), "Male")){
         holder.userrole.setImageResource(role_icons[0]);
      }else holder.userrole.setImageResource(role_icons[1]);

      }else{

         if(Objects.equals(user.getGender(), "Male")){
            holder.userrole.setImageResource(role_icons[2]);
         }else holder.userrole.setImageResource(role_icons[3]);

      }



      if(user.isStatus()) {

         holder.username.setCompoundDrawablesWithIntrinsicBounds(0,0,status_icons[0],0);


         holder.userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, chatdetail.class);
               intent.putExtra("rname", user.getFname());
               intent.putExtra("ruid", user.getUid());
               context.startActivity(intent);


            }
         });


      }else{


         holder.username.setCompoundDrawablesWithIntrinsicBounds(0,0,status_icons[1],0);
         holder.userlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(context, chatoffline.class);
               intent.putExtra("rname", user.getFname());
               intent.putExtra("ruid", user.getUid());
               context.startActivity(intent);


            }
         });


      }


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
                        holder.favicon.setEnabled(false);
                     }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
               });



         holder.favicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               database.getReference("Favourites").child(auth.getUid()).child(user.getUid()).removeValue();


//               ADDING FAVOURITES USER AND CHANGING ICON
               database.getReference("Favourites").child(auth.getUid()).child(user.getUid()).setValue(user);
               holder.favicon.setImageResource(fav_icons_list[0]);






            }
         });




   }

   @Override
   public int getItemCount() {
      return modelList.size();
   }


   public class  UserViewHolder extends RecyclerView.ViewHolder{
     LinearLayout userlayout,info_icon_layout;
     TextView username;
     ImageView infoicon,favicon;
     ImageView userrole;

      public UserViewHolder(@NonNull View itemView) {
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
