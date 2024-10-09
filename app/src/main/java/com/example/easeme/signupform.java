package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easeme.databinding.ActivitySignupformBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class signupform extends AppCompatActivity {
ActivitySignupformBinding bind;
ImageView logo;
TextView title;
TextInputLayout fname,email,phoneno,pass,repass;
FirebaseDatabase database;
FirebaseAuth auth;
AppCompatButton signup,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySignupformBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(bind.getRoot());
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        fname=bind.fname;
        email=bind.email;
        phoneno=bind.phoneno;
        title=bind.title2;
        logo=bind.logo;

        pass=bind.password;
        repass=bind.repassword;

        signup=bind.signup;
        login=bind.alreadybtn;

//        String imgrole=getIntent().getStringExtra("role");
//        if(imgrole.equals("Patient")){
//            logo.setImageResource(R.drawable.patient__2_);
//        }else{
//            logo.setImageResource(R.drawable.therapist__2_);
//
//        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signupform.this,login.class);
                Pair[] pairs=new Pair[6];
                pairs[0]=new Pair<View,String>(signup,"loginbtn");
                pairs[1]=new Pair<View,String>(login,"alreadybtnn");
                pairs[2]=new Pair<View,String>(email,"emailtxt");
                pairs[3]=new Pair<View,String>(pass,"passtxt");
                pairs[4]=new Pair<View,String>(logo,"logo");
                pairs[5]=new Pair<View,String>(title,"title");

                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(signupform.this,pairs);
                startActivity(intent,options.toBundle());

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn= String.valueOf(Objects.requireNonNull(fname.getEditText()).getText());
                String aemail= String.valueOf(Objects.requireNonNull(email.getEditText()).getText());
                String phone= String.valueOf(Objects.requireNonNull(phoneno.getEditText()).getText());
                String passw= String.valueOf(Objects.requireNonNull(pass.getEditText()).getText());
                String repassw= String.valueOf(Objects.requireNonNull(repass.getEditText()).getText());



                if(fn.trim().isEmpty()){
                    Toast.makeText(signupform.this, "Enter full name", Toast.LENGTH_SHORT).show();

                }else if(!passw.equals(repassw)){
                    Toast.makeText(signupform.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                }else{
                    UserModel user=new UserModel();
                    auth.createUserWithEmailAndPassword(aemail,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                user.setEmail(aemail);
                                user.setPass(passw);
                                user.setFname(fn);
                                user.setUid(auth.getUid());

                                user.setPhoneno(phone);


                                String role=getIntent().getStringExtra("role");
                                if(role.equals("Patient")){
                                    user.setRole(true);
                                }else{
                                    user.setRole(false);
                                }

                                database.getReference("Users").child(auth.getUid()).setValue(user);

                                Intent intent=new Intent(signupform.this,QuestionSetAct.class);

                                intent.putExtra("role",user.isRole());
                                intent.putExtra("user_uid",user.getUid());

                                startActivity(intent);
                                finish();



                            }else{
                                Toast.makeText(signupform.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }


            }
        });

    }
}