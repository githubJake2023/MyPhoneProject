package com.example.myphoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditContactActivity extends AppCompatActivity {

    EditText contact_name, contact_phone, contact_email;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;
    Button save_contact_btn;

    private String e_key, e_name, e_phone, e_email, e_gender;
    Dialog dialog;

    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        contact_name = findViewById(R.id.contact_name);
        contact_phone = findViewById(R.id.contact_phone);
        contact_email = findViewById(R.id.contact_email);

        radioGroup = findViewById(R.id.radioGroup);

        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        save_contact_btn = findViewById(R.id.save_contact_btn);

        dialog = new Dialog(this);

        Bundle b= this.getIntent().getExtras();
        if(b != null){
            e_key = b.getString("key");
            e_name = b.getString("name");
            e_phone = b.getString("phone");
            e_email = b.getString("email");
            e_gender = b.getString("gender");

            this.contact_name.setText(e_name);
            this.contact_phone.setText(e_phone);
            this.contact_email.setText(e_email);

            if(e_gender == "Male"){
                radioMale.setChecked(true);
            }else{
                radioFemale.setChecked(true);
            }

        }

        save_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("contacts");

                String c_name = contact_name.getText().toString();
                if(c_name.isEmpty()){
                    contact_name.setError("Name cannot be empty");
                    return;
                }else{
                    contact_name.setError(null);
                }
                String c_phone = contact_phone.getText().toString();
                if(c_phone.isEmpty()){
                    contact_phone.setError("Phone cannot be empty");
                    return;
                }else{
                    contact_phone.setError(null);
                }
                String c_email = contact_email.getText().toString();
                if(c_email.isEmpty()){
                    contact_email.setError("Email cannot be empty");
                    return;
                }else{
                    contact_email.setError(null);
                }

                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // No RadioButton is selected
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
                    // Now you have a reference to the selected RadioButton
                    String selectedGender = selectedRadioButton.getText().toString();
                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("name", c_name); // Update the name
                    updatedData.put("phone", c_phone); // Update the phone number
                    updatedData.put("email", c_email); // Update the email
                    updatedData.put("gender", selectedGender); // Update the gender
                    reference.child(e_key).updateChildren(updatedData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Contact successfully updated
                                    // Handle success as needed
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // An error occurred while updating the contact
                                    // Handle the error as needed
                                    Intent intent = new Intent();
                                    setResult(Activity.RESULT_CANCELED, intent);
                                    finish();
                                }
                            });

                }


            }
        });
    }
}