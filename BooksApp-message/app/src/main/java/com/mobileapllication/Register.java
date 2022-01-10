package com.mobileapllication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

//Register user using firebase

public class Register extends AppCompatActivity {

    TextView myLoginBtn;
    FirebaseAuth fAuth;
    EditText myUserName, myEmail, myPassword;
    Button myRegisterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myPassword = findViewById(R.id.password);
        myRegisterBtn = findViewById(R.id.registerBtn);
        myLoginBtn = findViewById(R.id.createText);
        myUserName = findViewById(R.id.userName);
        myEmail = findViewById(R.id.Email);

        fAuth = FirebaseAuth.getInstance();


    if(fAuth.getCurrentUser() != null){
        startActivity(new Intent (getApplicationContext(), MainActivity.class));
        finish();
    }

    myRegisterBtn.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            String email = myEmail.getText().toString().trim();
            String password = myPassword.getText().toString().trim();
            String username = myUserName.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                myEmail.setError("Please enter an email address");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                myPassword.setError("Please enter a password");
                return;
            }

            if (password.length() < 6){
                myPassword.setError("Password Must be at least 6 chars");
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(myEmail.getText().toString().trim(), myPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference mUserReff = FirebaseDatabase.getInstance().getReference().child("users");
                        HashMap map = new HashMap();
                        map.put("username", username);
                        map.put("email", email);
                        mUserReff.child(currentUserID).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                                else{
                                    Toast.makeText(Register.this, "Database Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Register.this, "Auth Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    });


    myLoginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), Login.class));
        }
    });


    }
}