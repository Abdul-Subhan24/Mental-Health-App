package com.example.easeme;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyHolder> {
    public SupportAdapter(Context context) {
        this.context = context;
    }

    Context context;
String[] titles={
    "iCall","Parivarthan","Sangath","COOJ Mental Health","Fortis",
        "Voice That Cares","Arpita"
    };
String[] nums={"022-25521111","+91 7676602602","011-41198666","0832-2252525","+91 837 680 4102"
        ,"8448-8448-45","+91 80 23655557"};

int [] icons={R.drawable.icall_m2,R.drawable.parivarthan_m1,R.drawable.sangath_m3,R.drawable.coojlogo,R.drawable.fortis_m7,
        R.drawable.voicethatcares_m4,
        R.drawable.arpilogo};

String[] urls={"https://icallhelpline.org/","https://parivarthan.org/counselling-helpline/","https://sangath.in/","https://cooj.co.in/our-programs/suicide-prevention/",
"https://www.fortishealthcare.com/india/clinical-speciality/mental-health-and-behavioural-sciences-268","https://www.rocf.org/voice-that-cares/",
"http://arpitafoundation.org/"};

String descrips[]={"iCALL is a psychosocial helpline for individuals in emotional and psychological distress",
"Parivarthan Counselling, Training and Research Centre is a registered, non-profit society that provides multimodal services",
"Sangath is a not-for-profit organisation working in Goa, India for 24 years to make mental health services accessible",
        "COOJ works towards promoting mental health in Goa under needs of Suicide Prevention, Mental Health,  etc",
"Fortis Healthcare is running a helpline program as a continuing service to reaffirm the commitment towards ensuring emotional health of students.",
        "Voice That Cares is a public helpline that provides psychosocial counselling support on a wide range of mental health matters",
"Arpita Suicide Prevention Helpline is one of the services of Arpita Foundation, started in 2019 to reach out in Society"
};

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=LayoutInflater.from(context).inflate(R.layout.contacthelplists,parent,false);
    return new MyHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(titles[position]);
        holder.spprticon.setImageResource(icons[position]);
        holder.desc.setText(descrips[position]);
        holder.number.setText(nums[position]);
        holder.knowbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlString =urls[position];
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlString));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    context.startActivity(intent);
                }

            }
        });
        holder.number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+nums[position]));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView spprticon;
        Button knowbtn;
        TextView title,desc,number;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
           spprticon=itemView.findViewById(R.id.supportlogo);
           knowbtn=itemView.findViewById(R.id.knowbtn);
           title=itemView.findViewById(R.id.supporttitle);
           desc=itemView.findViewById(R.id.supportdesc);
           number=itemView.findViewById(R.id.supportnumber);


        }
    }
}
