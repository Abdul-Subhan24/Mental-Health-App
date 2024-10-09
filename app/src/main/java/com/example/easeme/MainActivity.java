package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class MainActivity extends AppCompatActivity  {
    private static int SPLASH_TIME=1500;
   FirebaseAuth auth;
   DatabaseReference qset_reference;
   FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        qset_reference=database.getReference("Question Sets");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent2;

                if(auth.getCurrentUser()!=null){
                    database.getReference("Question Sets").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Intent intent;
                            if(snapshot.child(auth.getUid()).exists()){
                                intent=new Intent(MainActivity.this,mainui.class);


                            }else {
                                intent = new Intent(MainActivity.this, QuestionSetAct.class);
                                intent.putExtra("user_uid",auth.getUid());

                            }

                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }else {
                    intent2 = new Intent(MainActivity.this, launchpage.class);

                    startActivity(intent2);
                }


            }
        },SPLASH_TIME);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               finish();

            }
        },2500);



        setContentView(R.layout.activity_main);
    }


}