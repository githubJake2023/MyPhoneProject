package com.example.myphoneproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.myphoneproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    // Define a SharedPreferences file name (usually one per app)

    private static final String PREFS_NAME = "MyAppPreferences";
    SharedPreferences sharedPreferences;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ContactListFragment());

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean keyExists = sharedPreferences.contains("username");

        if (!keyExists) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.contact_list){
                replaceFragment(new ContactListFragment());
            }else if(item.getItemId() == R.id.add_contact){
                replaceFragment(new AddContactFragment());
            }else if(item.getItemId() == R.id.account){
                replaceFragment(new AccountFragment());
            }
            return true;
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}