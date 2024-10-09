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

//import com.example.banger.databinding.FragmentChatlayoutscreenBinding;
import com.example.easeme.databinding.FragmentChatlayoutscreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class chatlayoutscreen extends Fragment {
FragmentChatlayoutscreenBinding bind;
TextView userCount;
FirebaseDatabase database;
FirebaseAuth auth;
RecyclerView chatrecycler;
    public chatlayoutscreen() {

    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        bind=FragmentChatlayoutscreenBinding.inflate(inflater, container, false);

        chatrecycler=bind.chatrecyclerview;
        userCount=bind.usercount;

        List<UserModel> userModelList=new ArrayList<>();
        UserAdapter adapter=new UserAdapter(getContext(),userModelList);


        database.getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModelList.clear();
                int onlinehelpers=0;

                for(DataSnapshot user: snapshot.getChildren()){
                    UserModel model=user.getValue(UserModel.class);

                    if(!model.getUid().equals(auth.getCurrentUser().getUid())){
                        userModelList.add(model);

                        if(model.isStatus()){
                            onlinehelpers++;
                        }

                    }




                }

                userCount.setText("Helper's Available: " + onlinehelpers);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatrecycler.setAdapter(adapter);
        chatrecycler.setLayoutManager(new LinearLayoutManager(getContext()));



        return bind.getRoot();
    }
}