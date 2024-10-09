package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.easeme.databinding.ActivityChatdetailBinding;
import com.example.easeme.databinding.ActivityChatofflineBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatoffline extends AppCompatActivity {
    ActivityChatofflineBinding bind;
    ImageView recievericon,back_icon;
    TextView messagebox;

    TextView recievername;
    RecyclerView recycler_gchat;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityChatofflineBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);





        recievericon=bind.recievericon;
        recievername=bind.recievername;
        back_icon=bind.backButton;


        messagebox=bind.editGchatMessage;
        recycler_gchat=bind.recyclerGg;

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        String rec_uid=getIntent().getStringExtra("ruid");
        String r_name=getIntent().getStringExtra("rname");

        String sen_uid=auth.getUid();

        final String sen_room=sen_uid+rec_uid;



        recievername.setText(r_name);
        final ArrayList<MessageModel> messageList=new ArrayList<>();
        final ChatAdapter adapter=new ChatAdapter(getApplicationContext(),messageList);

        database.getReference("Chats").child(sen_room).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageList.clear();
                for(DataSnapshot model :snapshot.getChildren()){
                    MessageModel Model=model.getValue(MessageModel.class);

                    messageList.add(Model);
                }
                recycler_gchat.smoothScrollToPosition(messageList.size());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recycler_gchat.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        recycler_gchat.setLayoutManager(linearLayoutManager);









        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}