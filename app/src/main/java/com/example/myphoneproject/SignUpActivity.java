package com.example.myphoneproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText auth_username, auth_email, auth_phone, auth_password;
    Button signup_btn;
    TextView login_link;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth_username = findViewById(R.id.auth_username);
        auth_email = findViewById(R.id.auth_email);
        auth_phone = findViewById(R.id.auth_phone);
        auth_password = findViewById(R.id.auth_password);

        signup_btn = findViewById(R.id.signup_btn);
        login_link = findViewById(R.id.login_link);

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String username = auth_username.getText().toString();
                if(username.isEmpty()){
                    auth_username.setError("Username cannot be empty");
                    return;
                }else{
                    auth_username.setError(null);
                }
                String email = auth_email.getText().toString();
                if(email.isEmpty()){
                    auth_email.setError("Email cannot be empty");
                    return;
                }else{
                    auth_email.setError(null);
                }
                String phone = auth_phone.getText().toString();
                if(phone.isEmpty()){
                    auth_phone.setError("Phone cannot be empty");
                    return;
                }else{
                    auth_phone.setError(null);
                }
                String pass = auth_password.getText().toString();
                if(pass.isEmpty()){
                    auth_password.setError("Password cannot be empty");
                    return;
                }else{
                    auth_password.setError(null);
                }

                AuthUser authUser = new AuthUser(username, phone, email, pass);
                reference.child(username).setValue(authUser);


                Toast.makeText(SignUpActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}