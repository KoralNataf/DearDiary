package com.example.deardiary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Login extends Activity_Base {
    private ImageView login_IMG_background;
    private TextInputLayout login_EDT_email, login_EDT_password;
    private MaterialButton login_BTN_login,login_BTN_register;

    //Firebase
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        initViews();

    }

    private void findViews() {
        login_IMG_background = findViewById(R.id.login_IMG_background);
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_login = findViewById(R.id.login_BTN_login);
        login_BTN_register= findViewById(R.id.login_BTN_register);
        //Firebase Auth:
        auth = FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    }

    private void initViews() {
        updateImage(this.getResources().getIdentifier("background",
                "drawable", this.getPackageName()), login_IMG_background);

        //Checking for users existence
        if(firebaseUser != null)
            switchActivity(false,Activity_Main.class);

        //Register Button:
        login_BTN_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity(false,Activity_Register.class);
            }
        });

        //Login Button:
        login_BTN_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = login_EDT_email.getEditText().getText().toString();
                String password_text = login_EDT_password.getEditText().getText().toString();

                //Checking if it is empty:
                if (TextUtils.isEmpty(email_text) || TextUtils.isEmpty(password_text))
                    Toast.makeText(Activity_Login.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                else {
                    auth.signInWithEmailAndPassword(email_text, password_text)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        switchActivity(true,Activity_Main.class);
                                    } else
                                        Toast.makeText(Activity_Login.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

    }

    private void switchActivity(boolean flag,Class dest){
        Intent myIntent = new Intent(Activity_Login.this, dest);
        if (flag)
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
        finish();
    }


}