package com.example.deardiary;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class Fragment_Profile extends Fragment_Base {

    private static final int GALLERY_ACTION_PICK_CODE = 100;
    Uri imageUri;
    private ImageView profile_IMG_background;
    private CircularImageView profile_IMG_user;
    private EditText profile_EDT_first;
    private EditText profile_EDT_last;
    private EditText profile_EDT_email;
    private TextView profile_LBL_edit;
    private TextView profile_LBL_logout;

    private boolean inEdit = false;
    private  CallBack callBack;
    private User user;
    private Gson gson;

    //firebase
    FirebaseUser firebaseUser;
    DatabaseReference myRef;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initView();
        return view;
    }

    private void findViews(View view) {
        profile_IMG_background = view.findViewById(R.id.profile_IMG_background);
        profile_IMG_user = view.findViewById(R.id.profile_IMG_user);
        profile_EDT_first = view.findViewById(R.id.profile_EDT_first);
        profile_EDT_last = view.findViewById(R.id.profile_EDT_last);
        profile_EDT_email = view.findViewById(R.id.profile_EDT_email);
        profile_LBL_edit = view.findViewById(R.id.profile_LBL_edit);
        profile_LBL_logout=view.findViewById(R.id.profile_LBL_logout);

    }

    private void initView() {
        gson = new Gson();
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getContext().getPackageName()), profile_IMG_background);

        readProfileFromDB();

        profile_LBL_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!inEdit){
                    inEdit=true;
                    profile_LBL_edit.setText("Save Changes");
                    profile_EDT_first.setEnabled(true);
                    profile_EDT_last.setEnabled(true);
                }else{

                    String fName = profile_EDT_first.getText().toString();
                    String lName = profile_EDT_last.getText().toString();
                    if (TextUtils.isEmpty(fName) ||TextUtils.isEmpty(lName))
                        Toast.makeText(getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                    else{
                        changeAttributeInFireBase(Activity_Register.F_NAME, fName);
                        changeAttributeInFireBase(Activity_Register.L_NAME, lName);
                        inEdit=false;
                        profile_LBL_edit.setText("Edit");
                        profile_EDT_first.setEnabled(false);
                        profile_EDT_last.setEnabled(false);
                    }
                }
            }
        });

        profile_IMG_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.changeActivity();
            }
        });

        profile_LBL_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack !=null)
                    callBack.logOut();
            }
        });


    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    private void changeAttributeInFireBase(String key, String value) {
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        myRef.updateChildren(map);
    }

    private void updateProfile() {

        profile_EDT_first.setText(user.getUsername());
        profile_EDT_last.setText(user.getUserLastName());
        profile_EDT_email.setText(user.getEmail());

    }

    private void readProfileFromDB(){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("MyUsers").child(firebaseUser.getUid());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class); //load user from DB
                if (!user.getImageURL().equals("default")) {
                    Glide.with(getContext()).load(user.getImageURL()).into(profile_IMG_user);
                }
                updateProfile();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}