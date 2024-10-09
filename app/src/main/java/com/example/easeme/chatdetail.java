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
import android.widget.Toast;

import com.example.easeme.databinding.ActivityChatdetailBinding;
import com.example.easeme.databinding.ActivitySignupformBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class chatdetail extends AppCompatActivity {
ActivityChatdetailBinding bind;
ImageView recievericon,back_icon;
EditText messagebox;
Button sendbtn;
TextView recievername;
RecyclerView recycler_gchat;
FirebaseDatabase database;
FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);





        recievericon=bind.recievericon;
        recievername=bind.recievername;
        back_icon=bind.backButton;
        sendbtn=bind.buttonGchatSend;
        messagebox=bind.editGchatMessage;
        recycler_gchat=bind.recyclerGg;

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        String rec_uid=getIntent().getStringExtra("ruid");
        String r_name=getIntent().getStringExtra("rname");

        String sen_uid=auth.getUid();

        final String sen_room=sen_uid+rec_uid;
        final String rec_room=rec_uid+sen_uid;


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







        messagebox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                sendbtn.setTextColor(getResources().getColor(R.color.fgray));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                sendbtn.setTextColor(getResources().getColor(R.color.tickgreen2));

            }
        });



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String message=messagebox.getText().toString();

                if(!message.trim().equals("")){
                    MessageModel messageModel=new MessageModel(message,sen_uid);

                    messageModel.setTimestamp(new Date().getTime());
                    messagebox.setText("");
                    sendbtn.setTextColor(getResources().getColor(R.color.fgray));

                    database.getReference("Chats").child(sen_room).push().setValue(messageModel);
                    database.getReference("Chats").child(rec_room).push().setValue(messageModel);
                    recycler_gchat.smoothScrollToPosition(messageList.size());


                }
            }
        });









        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
}