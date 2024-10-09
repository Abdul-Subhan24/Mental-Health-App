package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class mainui extends AppCompatActivity implements AddLifecycleCallbackListener {
FirebaseAuth auth;
FirebaseDatabase database;
DatabaseReference infoconnections;
BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientscreen);
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        infoconnections=database.getReference(".info/connected");

        infoconnections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected=snapshot.getValue(boolean.class);


                if(connected){
                    database.getReference("Users").child(auth.getUid()).child("status").setValue(true);
                    database.getReference("Users").child(auth.getUid()).child("status").onDisconnect().setValue(false);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
        bottomNavigationView=findViewById(R.id.bottom_menu);



        getSupportFragmentManager().beginTransaction().replace(R.id.framel,new patientfragment()).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framel, new patientfragment()).commit();
                        return true;
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framel, new chatlayoutscreen()).commit();
                        return true;
                    case R.id.fav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framel, new favourites()).commit();
                        return true;

                    case R.id.settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framel, new SettingsFragment()).commit();
                        return true;
                }

                    return false;
            }
        });




    }

    @Override
    public void addLifeCycleCallBack(YouTubePlayerView youTubePlayerView) {
        getLifecycle().addObserver(youTubePlayerView);
    }
}