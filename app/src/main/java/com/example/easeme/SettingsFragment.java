package com.example.easeme;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easeme.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class SettingsFragment extends Fragment {
FragmentSettingsBinding bind;
EditText eusername,epass,eage,eemail;
TextInputLayout passconfirm;

Button logoutbtn,yesbtn,nobtn,submitbtn,changes;
TextView email,name,age,passwordd;
ImageView editicon,userrole;
LinearLayout showbox,hidebox;
FirebaseAuth auth;
String oldpass;
FirebaseDatabase database;


RecyclerView recyclerView;

    public SettingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind=FragmentSettingsBinding.inflate(inflater, container, false);
        logoutbtn=bind.logoutbtn;
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        hidebox=bind.editbox;
        showbox=bind.showtext;
        editicon=bind.editicon;
        changes=bind.changes;
        email=bind.email;

        userrole=bind.usericon;

        name=bind.username;
        age=bind.age;
        email=bind.email;
        passwordd=bind.passwordd;
        recyclerView=bind.questionsetRecycler;
        int[] role_icons={R.drawable.male_patient,R.drawable.female_user,R.drawable.male_therapist,R.drawable.female_therapist};





        eusername=bind.eusername;
        epass=bind.epass;
        eage=bind.eage;
        eemail=bind.eemail;








        List<String> qlist=new ArrayList<>();
        List<String> anslist=new ArrayList<>();
        QsetAdapter adapter =new QsetAdapter(qlist,anslist, getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));



        database.getReference("Question Sets").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
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





        database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userDetails=snapshot.getValue(UserModel.class);
                name.setText(userDetails.getFname());
                age.setText(userDetails.getAge()+ " years old");
                email.setText(userDetails.getEmail());
                passwordd.setText(userDetails.getPass());


                eusername.setText(userDetails.getFname());
                eage.setText(userDetails.getAge());
               eemail.setText(userDetails.getEmail());
                epass.setText(userDetails.getPass());

                if(userDetails.isRole()){

                    if(Objects.equals(userDetails.getGender(), "Male")){
                        userrole.setImageResource(role_icons[0]);
                    }else userrole.setImageResource(role_icons[1]);

                }else{

                    if(Objects.equals(userDetails.getGender(), "Male")){
                        userrole.setImageResource(role_icons[2]);
                    }else userrole.setImageResource(role_icons[3]);

                }









            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.logoutdialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);

                yesbtn=dialog.findViewById(R.id.yesbtn);
                nobtn=dialog.findViewById(R.id.nobtn);

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(),launchpage.class);
                       auth.signOut();
                        dialog.dismiss();
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });



        editicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.editdialog);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                passconfirm=dialog.findViewById(R.id.pass);


                submitbtn=dialog.findViewById(R.id.submitbtn);



                database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        UserModel user=snapshot.getValue(UserModel.class);
                        oldpass=user.getPass();
//                        Toast.makeText(getContext(), oldpass, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                submitbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      if(Objects.equals(oldpass, Objects.requireNonNull(passconfirm.getEditText()).getText().toString())){
                            Toast.makeText(getContext(), "Authentication Successful", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            showbox.setVisibility(View.GONE);
                            name.setVisibility(View.GONE);
                            hidebox.setVisibility(View.VISIBLE);


                            database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    final String check_email;
                                    check_email =snapshot.getValue(UserModel.class).getEmail();




                                    changes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AuthCredential credential = EmailAuthProvider
                                                    .getCredential(check_email, oldpass);




                                            auth.getCurrentUser().reauthenticate( credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        auth.getCurrentUser().updateEmail(eemail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    auth.getCurrentUser().updatePassword(epass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                database.getReference("Users").child(auth.getUid()).child("email").setValue(eemail.getText().toString());
                                                                                database.getReference("Users").child(auth.getUid()).child("pass").setValue(epass.getText().toString());
                                                                                database.getReference("Users").child(auth.getUid()).child("fname").setValue(eusername.getText().toString());
                                                                                database.getReference("Users").child(auth.getUid()).child("age").setValue(eage.getText().toString());
//                                                            database.getReference("Users").child(auth.getUid()).child("age").setValue(eage.getText().toString());

                                                                                Toast.makeText(getContext(), "Changes Successful", Toast.LENGTH_SHORT).show();
                                                                            }else {
                                                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }else{
                                                                    Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }else Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });





                                            showbox.setVisibility(View.VISIBLE);
                                            name.setVisibility(View.VISIBLE);


                                            hidebox.setVisibility(View.GONE);





                                        }
                                    });





                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }else{
                          Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                          dialog.dismiss();
                      }
                    }
                });



                dialog.show();

            }
        });

        return bind.getRoot();
    }
}