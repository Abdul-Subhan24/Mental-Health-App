package com.example.easeme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easeme.databinding.ActivityQuestionSetBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuestionSetAct extends AppCompatActivity {

    ActivityQuestionSetBinding bind;
    RadioGroup profession_selected,gender_selected;
    NumberPicker age_picker;
    Button submitt;
    TextView q1,q2,q3,q4;
    EditText a1,a2,a3,a4;
    RadioButton genderbtn,professionbtn;

    FirebaseDatabase database;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityQuestionSetBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        profession_selected=bind.professionSelected;
        gender_selected=bind.genderSelected;
        age_picker=bind.agePicker;
        submitt=bind.submitt;
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();


        q1=bind.questiontext;
        q2=bind.questiontext2;
        q3=bind.questiontext3;
        q4=bind.questiontext4;

        a1=bind.answer;
        a2=bind.answer2;
        a3=bind.answer3;
        a4=bind.answer4;


        age_picker.setMinValue(0);
        age_picker.setMaxValue(100);

        ArrayList<String> question_list_patient=new ArrayList<>();
        ArrayList<String> question_list_helper=new ArrayList<>();
        ArrayList<String> answer_list=new ArrayList<>();

        QsetModel qsetModel=new QsetModel();

        question_list_patient.add("Describe what are you feeling?");
        question_list_patient.add("How often do you feel this way?");
        question_list_patient.add("Do you feel suicidal? if yes how often?");
        question_list_patient.add("Have you tried to talk about how you feel to anyone close? if not, why?");

        question_list_helper.add("Do you have any past experience in this field? if yes, elaborate.");
        question_list_helper.add("How long have you been working in this or trying to help?");
        question_list_helper.add("How often will you be available on the app if needed?");
        question_list_helper.add("How will you assess the situation if a patient confesses he is suicidal? Explain in detail");




        database.getReference("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel user=snapshot.getValue(UserModel.class);

                if(user.isRole()){
                    q1.setText(question_list_patient.get(0));
                    q2.setText(question_list_patient.get(1));
                    q3.setText(question_list_patient.get(2));
                    q4.setText(question_list_patient.get(3));

                    qsetModel.setQlist(question_list_patient);

                }else{

                    q1.setText(question_list_helper.get(0));
                    q2.setText(question_list_helper.get(1));
                    q3.setText(question_list_helper.get(2));
                    q4.setText(question_list_helper.get(3));
                    qsetModel.setQlist(question_list_helper);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        submitt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int selectedid_profession=profession_selected.getCheckedRadioButtonId();

                int selectedid_gender=gender_selected.getCheckedRadioButtonId();
                int age=age_picker.getValue();
                qsetModel.isRole();


                if(selectedid_profession==-1){
                    Toast.makeText(QuestionSetAct.this, "Select your profession", Toast.LENGTH_SHORT).show();

                }else if(selectedid_gender==-1){

                    Toast.makeText(QuestionSetAct.this, "Select your gender", Toast.LENGTH_SHORT).show();
                }else if(age==0){

                    Toast.makeText(QuestionSetAct.this, "Enter correct age", Toast.LENGTH_SHORT).show();

                }else if(a1.getText().toString().trim().equals("") && a2.getText().toString().trim().equals("") &&
                        a3.getText().toString().trim().equals("") && a4.getText().toString().trim().equals("") ){


                    Toast.makeText(QuestionSetAct.this, "Answer all the questions", Toast.LENGTH_SHORT).show();

                }else {

                    String useruid=getIntent().getStringExtra("user_uid");
                    genderbtn=findViewById(selectedid_gender);
                    professionbtn=findViewById(selectedid_profession);

                    answer_list.add(a1.getText().toString());
                    answer_list.add(a2.getText().toString());
                    answer_list.add(a3.getText().toString());
                    answer_list.add(a4.getText().toString());

                    qsetModel.setAnslist(answer_list);

                    String age_number=String.valueOf(age);


                    database.getReference("Question Sets").child(useruid).setValue(qsetModel);
                    database.getReference("Users").child(useruid).child("gender").setValue(genderbtn.getText().toString());
                    database.getReference("Users").child(useruid).child("profession").setValue(professionbtn.getText().toString());
                    database.getReference("Users").child(useruid).child("age").setValue(age_number);



                    Intent intent= new Intent(QuestionSetAct.this,mainui.class);
                    startActivity(intent);

                }




            }
        });



    }
}