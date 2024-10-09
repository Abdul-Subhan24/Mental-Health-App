package com.example.easeme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.easeme.databinding.FragmentPatientfragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class patientfragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView recyclerView,ytrecycler;
    TextView patientname;
    ScrollView scrollView;

    public patientfragment() {

    }

    FragmentPatientfragmentBinding bind;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind=FragmentPatientfragmentBinding.inflate(inflater, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        scrollView=bind.scrollbar;
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setHorizontalScrollBarEnabled(false);




        recyclerView=bind.recyclervie;
        ytrecycler=bind.ytrecycler;

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user = snapshot.getValue(UserModel.class);
                patientname = bind.usergreet;
                String[] name = user.getFname().split(" ");

                if (name.length != 0) {
                    patientname.setText("Welcome " + name[0] + ",");


                } else {
                    patientname.setText("Welcome " + user.getFname() + ",");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        SupportAdapter adapter=new SupportAdapter(getContext());
        ArrayList<UrlModel> videoList=new ArrayList<>();
//        videoList.add(new UrlModel("qRaqfpKPjIc"));
//        videoList.add(new UrlModel("1mUOPXEj8jk"));
//        videoList.add(new UrlModel("1mUOPXEj8jk"));
//        videoList.add(new UrlModel("1mUOPXEj8jk"));
        VideoAdapter videoAdapter=new VideoAdapter(videoList,getContext(),getLifecycle());

        database.getReference().child("Video Links").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoList.clear();
                for(DataSnapshot data :snapshot.getChildren()){
                    String url=data.getValue(String.class);

                    UrlModel model=new UrlModel(url);
                    videoList.add(model);


                }

                videoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        recyclerView.setAdapter(adapter);
        ytrecycler.setAdapter(videoAdapter);


        LinearLayoutManager linearLayout=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);

        LinearLayoutManager linearLayout2=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);





        recyclerView.setLayoutManager(linearLayout);
        ytrecycler.setLayoutManager(linearLayout2);












        return bind.getRoot();
    }
}