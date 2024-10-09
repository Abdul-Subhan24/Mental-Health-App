package com.example.easeme;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.easeme.databinding.FragmentChatlayoutscreenBinding;
import com.example.easeme.databinding.FragmentFavouritesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class favourites extends Fragment {


    public favourites() {
        // Required empty public constructor
    }

   FragmentFavouritesBinding bind;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView nofavs;
    RecyclerView chatrecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind=FragmentFavouritesBinding.inflate(inflater,container,false);

        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();

        chatrecycler=bind.chatrecyclerview;
        nofavs=bind.nofavs;


        List<UserModel> userModelList=new ArrayList<>();

        favAdapter adapter=new favAdapter(getContext(),userModelList);


        database.getReference("Favourites").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelList.clear();

                for (DataSnapshot user : snapshot.getChildren()) {
                    UserModel model = user.getValue(UserModel.class);


                    userModelList.add(model);


            }

                if(userModelList.size()==0){
            nofavs.setVisibility(View.VISIBLE);
            chatrecycler.setVisibility(View.GONE);
//
        }else {
                    nofavs.setVisibility(View.GONE);
                    chatrecycler.setVisibility(View.VISIBLE);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//


        chatrecycler.setAdapter(adapter);
        chatrecycler.setLayoutManager(new LinearLayoutManager(getContext()));




        return bind.getRoot();
    }
}