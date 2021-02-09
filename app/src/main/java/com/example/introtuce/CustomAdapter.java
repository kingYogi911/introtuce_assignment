package com.example.introtuce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.TimeUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ItemViewHolder> {
    private DatabaseReference dbref;
    StorageReference sref;
    private ArrayList<User> users;
    public CustomAdapter( ArrayList<User> users,DatabaseReference dbref,StorageReference sref) {
        this.users=new ArrayList<>(users);
        this.dbref=dbref;
        this.sref=sref;
    }

    @NonNull
    @Override
    public CustomAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ItemViewHolder holder, int position) {
        User x=users.get(position);
        long diff=0;
        try {
            Date d=new Date();
            Date dob= new SimpleDateFormat("dd/mm/yyyy").parse(x.getDob());
            diff=((d.getTime()-dob.getTime())/1000)/(60*60*24*365);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Picasso.get().load(x.getImageUri()).fit().centerCrop().into(holder.imageView);
        holder.tv_name.setText(String.format("%s %s", x.getFirstName(), x.getLastName()));
        holder.tv_details.setText(x.getGender()+" | "+ diff+" | "+x.getHomeTown());
        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
         ImageView imageView;
         TextView tv_name;
         TextView tv_details;
         ImageButton bt_delete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.user_image);
            tv_name=itemView.findViewById(R.id.user_name);
            tv_details=itemView.findViewById(R.id.user_details);
            bt_delete=itemView.findViewById(R.id.bt_delete);
        }
    }
    public void deleteUser(User u){
        sref.child(""+u.getPhoneNumber()).delete();
        dbref.child(String.valueOf(u.getPhoneNumber())).removeValue();
        users.remove(u);
        notifyDataSetChanged();
    }
    public void notifyChange(ArrayList<User> users){
        this.users=users;
        notifyDataSetChanged();
    }
}
