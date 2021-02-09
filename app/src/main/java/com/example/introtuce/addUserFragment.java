package com.example.introtuce;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class addUserFragment extends Fragment {
    private Button bt_add;
    private ImageButton img_bt;
    private Boolean pic_check;
    private EditText[] et;
    Uri imageUri;
    DatabaseReference databaseUsers;
    public addUserFragment() {
        // Required empty public constructor
    }

    public static addUserFragment newInstance(String param1, String param2) {
        return new addUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_user, container, false);


        pic_check=false;
        img_bt=view.findViewById(R.id.img_bt);
        img_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });


        et=new EditText[8];
        et[0]=view.findViewById(R.id.et_first_name);
        et[1]=view.findViewById(R.id.et_last_name);
        et[2]=view.findViewById(R.id.et_dob);
        et[3]=view.findViewById(R.id.et_gender);
        et[4]=view.findViewById(R.id.et_country);
        et[5]=view.findViewById(R.id.et_state);
        et[6]=view.findViewById(R.id.et_home_town);
        et[7]=view.findViewById(R.id.et_phone_number);

        databaseUsers= FirebaseDatabase.getInstance().getReference("users");

        bt_add=view.findViewById(R.id.bt_add_user);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(getContext());//Will be used for showing alert message for invalid values
                builder.setCancelable(true);

                String s[]=new String[10];
                for(int i=0;i<8;i++){
                    s[i]=et[i].getText().toString().trim();
                    if(s[i].length()==0){//check for empty field
                        builder.setMessage("Please fill each detail");
                        builder.create().show();
                        return;//end further processing
                    }
                }


                if(!pic_check){
                    builder.setMessage("Profile photo not selected");
                    builder.create().show();
                    return;
                }


                /*validation of Date Of Birth*/
                try{
                    Date d=new SimpleDateFormat("dd/mm/yyyy").parse(s[2]);
                }catch (Exception e){
                    builder.setMessage("Use dd/mm/yyyy pattern");
                    builder.create().show();
                }
                /* Validation of Phone Number*/
                try{
                    Long.parseLong(s[7]);//No digits will lead to throw error
                }catch(Exception e){
                    builder.setMessage("Number is not a valid one");
                    builder.create().show();
                }

                //TimeStamp for Descending Order,while fetching from firebase Realtime Databse
                s[8]= Long.toString(-1*new Date().getTime());

                /*Checking database if the phone Number is alredy present*/
                databaseUsers.child(String.valueOf(s[7])).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            builder.setMessage("Phone Number is alredy in use");
                            builder.create().show();
                        }
                        else{/* Using Different Thread to maintain Responsivemess*/
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    saveToFireBase(s);
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==9&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).fit().centerCrop().into(img_bt);
            pic_check=true;
        }
    }
    public void selectImage(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Image from.."),9);
    }


        /* This methods saves image into Firebase Storage
        and gets its download URL that is then further saved in the
        Firebase Realtime Database along with other informations */

    public void saveToFireBase(String[] s){
        StorageReference srf=FirebaseStorage.getInstance().getReference()
                .child("android/media").child(s[7]);//Using Phone Number as Key
        srf.putFile(imageUri).addOnSuccessListener(taskSnapshot -> srf.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                s[9]=uri.toString();
                User u=new User(s);
                databaseUsers.child(String.valueOf(u.getPhoneNumber())).setValue(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "SuccessFully added in DataBase", Toast.LENGTH_SHORT).show();
                        for (EditText e:et){
                            img_bt.setImageDrawable(null);
                            e.setText("");
                        }
                    }
                });
            }
        }));

    }
}