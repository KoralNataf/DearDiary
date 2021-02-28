package com.example.deardiary;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;


public class Activity_Register extends Activity_Base {

    public static final String USER = "USER";
    public static final String NOT_EXIST = "NOT_EXIST";
    public static final String F_NAME = "username";
    public static final String L_NAME = "userLastName";
    private ImageView register_IMG_background;
    private TextInputLayout register_EDT_first_name,register_EDT_last_name, register_EDT_password, register_EDT_email, register_EDT_password_verification;
    private MaterialButton register_BTN_register;
    private MaterialButton register_BTN_back;
    private User user;

    //firebase
    FirebaseAuth auth;
    DatabaseReference myRef;

    //Gson
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
        initViews();

    }

    private void findViews() {
        register_IMG_background = findViewById(R.id.register_IMG_background);
        register_EDT_first_name = findViewById(R.id.register_EDT_first_name);
        register_EDT_last_name = findViewById(R.id.register_EDT_last_name);
        register_EDT_password = findViewById(R.id.register_EDT_password);
        register_EDT_password_verification = findViewById(R.id.register_EDT_password_verification);
        register_EDT_email = findViewById(R.id.register_EDT_email);
        register_BTN_register = findViewById(R.id.register_BTN_register);
        register_BTN_back=findViewById(R.id.register_BTN_back);
    }

    private void initViews() {

        gson = new Gson();
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getPackageName()), register_IMG_background);

        //Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Adding Event Listener to Button Register
        register_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName_text = register_EDT_first_name.getEditText().getText().toString();
                String userLastName_text = register_EDT_last_name.getEditText().getText().toString();
                String email_text = register_EDT_email.getEditText().getText().toString();
                String password_text = register_EDT_password.getEditText().getText().toString();
                String password_verification_text = register_EDT_password_verification.getEditText().getText().toString();


                if (TextUtils.isEmpty(userName_text) ||TextUtils.isEmpty(userLastName_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text) || TextUtils.isEmpty(password_verification_text) )
                    Toast.makeText(Activity_Register.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                else if (!password_text.equals(password_verification_text))
                    Toast.makeText(Activity_Register.this, "Password Verification Failed! Try Again..", Toast.LENGTH_SHORT).show();
                else
                    registerNow(userName_text, userLastName_text, email_text, password_text);

            }
        });

        register_BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Activity_Register.this,Activity_Login.class);
                startActivity(myIntent);
                finish();
            }
        });
    }

    private void registerNow(final String userFirstName,final String userLastName , String email , String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            myRef = FirebaseDatabase.getInstance()
                                    .getReference("MyUsers");


                            user = new User(userId, userLastName, userFirstName, email, "default");

                            myRef.child(userId).setValue(user);

                            Intent myIntent = new Intent(Activity_Register.this,Activity_Main.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(myIntent);
                            finish();
                        }else{
                            Toast.makeText(Activity_Register.this,"Invalid Email or Password - Password Must Be At List 6 Characters", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}