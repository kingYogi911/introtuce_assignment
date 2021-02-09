package com.example.introtuce;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowUserFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowUserFrag extends Fragment {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ArrayList<User> users;
    DatabaseReference databaseRef;//
    StorageReference storageRef;//It will be used to delete user's profile photo
    public ShowUserFrag() {
        // Required empty public constructor
    }
    public static ShowUserFrag newInstance(String param1, String param2) {
        return new ShowUserFrag();
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users.clear();
                        for(DataSnapshot ds:snapshot.getChildren()){
                            users.add(ds.getValue(User.class));
                        }
                        adapter.notifyChange(users);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Following Firebase References will be paased to Custom Adapter */
        storageRef= FirebaseStorage.getInstance().getReference().child("android/media");
        databaseRef=FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_show_user, container, false);
        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        users=new ArrayList<>();
        adapter=new CustomAdapter(users,databaseRef,storageRef);

        recyclerView.setAdapter(adapter);
        return view;
    }
}