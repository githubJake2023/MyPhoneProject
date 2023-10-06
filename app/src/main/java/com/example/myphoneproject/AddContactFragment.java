package com.example.myphoneproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddContactFragment extends Fragment {

    EditText contact_name, contact_phone, contact_email;
    RadioGroup radioGroup;
    RadioButton radioMale, radioFemale;
    Button add_contact_btn;

    FirebaseDatabase database;
    DatabaseReference reference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddContactFragment newInstance(String param1, String param2) {
        AddContactFragment fragment = new AddContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_contact, container, false);

        contact_name = rootView.findViewById(R.id.contact_name);
        contact_phone = rootView.findViewById(R.id.contact_phone);
        contact_email = rootView.findViewById(R.id.contact_email);

        radioGroup = rootView.findViewById(R.id.radioGroup);

        radioMale = rootView.findViewById(R.id.radioMale);
        radioFemale = rootView.findViewById(R.id.radioFemale);

        add_contact_btn = rootView.findViewById(R.id.add_contact_btn);

        add_contact_btn.setOnClickListener(new View.OnClickListener() {
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

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = rootView.findViewById(selectedRadioButtonId);
                    // Now you have a reference to the selected RadioButton
                    String selectedGender = selectedRadioButton.getText().toString();
                    String key = reference.push().getKey();
                    MyContacts myContacts = new MyContacts(key, c_name, c_phone, c_email, selectedGender);
                    reference.child(key).setValue(myContacts);

                    Toast.makeText(getActivity(), "Successfully Added to Contact", Toast.LENGTH_SHORT).show();
                    // restart contact inputs
                    contact_name.setText("");
                    contact_phone.setText("");
                    contact_email.setText("");
                    radioMale.setChecked(true);
                } else {
                    // No RadioButton is selected
                    Toast.makeText(getActivity(), "No Selected Gender", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rootView;
    }
}