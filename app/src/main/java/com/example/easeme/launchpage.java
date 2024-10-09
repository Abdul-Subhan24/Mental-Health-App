package com.example.easeme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.easeme.databinding.ActivityLaunchpageBinding;

public class launchpage extends AppCompatActivity {
ActivityLaunchpageBinding bind;
Button pbutton,hbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityLaunchpageBinding.inflate(getLayoutInflater());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(bind.getRoot());
        pbutton=bind.patientsign;
        hbutton=bind.helpersign;
       Intent intent=new Intent(this,signupform.class);

        pbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role="Patient";
                intent.putExtra("role",role);
                startActivity(intent);
                finish();

            }
        });
        hbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String role="Helper";
                intent.putExtra("role",role);
                startActivity(intent);
                finish();

            }
        });

    }
}