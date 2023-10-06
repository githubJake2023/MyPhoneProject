package com.example.myphoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText auth_username, auth_password;
    Button login_btn;
    TextView signup_link;
    FirebaseDatabase database;
    DatabaseReference reference;

    // Define a SharedPreferences file name (usually one per app)
    private static final String PREFS_NAME = "MyAppPreferences";
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean keyExists = sharedPreferences.contains("username");

        if (keyExists) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        auth_username = findViewById(R.id.auth_username);
        auth_password = findViewById(R.id.auth_password);

        login_btn = findViewById(R.id.login_btn);
        signup_link = findViewById(R.id.signup_link);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUserName() | !validatePassword() ){

                }else{
                    checkUser();
                }
            }
        });

        signup_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public  Boolean validateUserName(){
        String val = auth_username.getText().toString();
        if(val.isEmpty()){
            auth_username.setError("Username cannot be empty");
            return false;
        }else{
            auth_username.setError(null);
            return true;
        }
    }

    public  Boolean validatePassword(){
        String val = auth_password.getText().toString();
        if(val.isEmpty()){
            auth_password.setError("Password cannot be empty");
            return false;
        }else{
            auth_password.setError(null);
            return true;
        }
    }


    public void checkUser(){
        String username = auth_username.getText().toString();
        String pass = auth_password.getText().toString();

        reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(username);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    auth_username.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);
                    String emailFromDB = snapshot.child(username).child("email").getValue(String.class);
                    String phoneFromDB = snapshot.child(username).child("phone").getValue(String.class);
                    if(Objects.equals(passwordFromDB, pass)){
                        auth_username.setError(null);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username); // Store
                        editor.putString("email", emailFromDB);
                        editor.putString("phone", phoneFromDB);
                        editor.apply(); // Apply changes

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        auth_password.setError("Invalid Credentials");
                        auth_password.requestFocus();
                    }
                }else{
                    auth_username.setError("User does not exist");
                    auth_username.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}