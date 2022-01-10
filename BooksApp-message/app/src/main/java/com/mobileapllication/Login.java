package com.mobileapllication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    CheckBox saveDetails;
    Switch loadDetails;
    TextView mCreateBtn,forgotTextLink;
    FirebaseAuth fAuth;

    DatabaseReference mUserReff;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        SharedPreferences sp = getSharedPreferences("myPreference", Context.MODE_PRIVATE);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);

        fAuth = FirebaseAuth.getInstance();
        saveDetails = findViewById(R.id.saveEmail);
        loadDetails = findViewById(R.id.loadEmail);
        mLoginBtn = findViewById(R.id.loginBtn);
        mCreateBtn = findViewById(R.id.createText);

        mUserReff = FirebaseDatabase.getInstance().getReference().child("users");

//        get values in the email and password textviews
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();





        loadDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//  SHARED PREFERENCE - when the load button is checked, get the previous email and password that was stored
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("myPreference", Context.MODE_PRIVATE);
                    String emailLoaded = sp.getString("email", "");
                    String passwordLoaded = sp.getString("password", "");

//                    if there has been no password or email stored, send warning message.
                    if (emailLoaded.isEmpty() || passwordLoaded.isEmpty()) {
                        Toast.makeText(Login.this, "Please tick the save email and password book to save your details for the next login!", Toast.LENGTH_SHORT).show();
                    } else {

                        mEmail.setText(emailLoaded);
                        mPassword.setText(passwordLoaded);
                    }
                }else{
//                    when the load button becomes unchecked, set an empty value in email and password field
                    mEmail.setText("");
                    mPassword.setText("");
                }
            }

        });


        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }


//hard code lecturer login
                if(email.equals("eee") && password.equals("eee")){
                    email = "test@gmail.com";
                    password = "eeeeee";
                    System.out.println(email);
                }


                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String currentUID = fAuth.getCurrentUser().getUid();
                            mUserReff.child(currentUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this, "Sorry login not working", Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }else {
                            Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });


                if(saveDetails.isChecked()){

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.commit();

                }

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });



    }
}