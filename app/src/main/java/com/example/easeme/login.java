package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easeme.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class login extends AppCompatActivity {
AppCompatButton loginbtn,alreadybtnn;
TextInputLayout email,pass;
ImageView logo;
TextView title;
FirebaseAuth auth;
FirebaseDatabase database;
ActivityLoginBinding bind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        bind=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();



        loginbtn=bind.login;
        alreadybtnn=bind.alreadybtn;
        email=bind.email;
        pass=bind.password;
        logo=bind.logo;
        title=bind.title;

        alreadybtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,signupform.class);

                Pair[] pairs=new Pair[6];
                pairs[0]=new Pair<View,String>(loginbtn,"loginbtn");
                pairs[1]=new Pair<View,String>(alreadybtnn,"alreadybtnn");
                pairs[2]=new Pair<View,String>(email,"emailtxt");
                pairs[3]=new Pair<View,String>(pass,"passtxt");
                pairs[4]=new Pair<View,String>(logo,"logo");
                pairs[5]=new Pair<View,String>(title,"title");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(login.this,pairs);
                startActivity(intent,options.toBundle());

            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signInWithEmailAndPassword(String.valueOf(Objects.requireNonNull(email.getEditText()).getText()),String.valueOf(Objects.requireNonNull(pass.getEditText()).getText())).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                                @SuppressLint("SuspiciousIndentation")
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    UserModel model = new UserModel();
                                    model =snapshot.getValue(UserModel.class);
                                    Intent intent;
                                    if(model.isRole()){
                                        intent=new Intent(login.this,mainui.class);

                                    }else intent=new Intent(login.this,helperscreen.class);
                                        startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(login.this, "Error Occured", Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    }
                });


            }
        });
    }
}