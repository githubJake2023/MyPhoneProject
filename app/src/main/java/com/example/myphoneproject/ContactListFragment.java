package com.example.myphoneproject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {
    EditText etSearch;
    ListView lvResult;

    ArrayList<MyContacts> list = new ArrayList<MyContacts>();
    ArrayList<MyContacts> filteredlist = new ArrayList<MyContacts>();
    MyAdapter adapter;

    AdapterView.AdapterContextMenuInfo info;

    int cnt = 0;
    String regex = "";

    AlertDialog.Builder mBuilder;
    AlertDialog mDialog;
    FirebaseDatabase database;
    DatabaseReference reference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactListFragment newInstance(String param1, String param2) {
        ContactListFragment fragment = new ContactListFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("contacts");

        etSearch = rootView.findViewById(R.id.etSearch);
        lvResult = rootView.findViewById(R.id.lvResult);

        adapter = new MyAdapter(getActivity(), filteredlist);
        lvResult.setAdapter(adapter);
        lvResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                TextView dialog_name = (TextView) mView.findViewById(R.id.dialog_name);
                TextView dialog_phone = (TextView) mView.findViewById(R.id.dialog_phone);
                TextView dialog_email = (TextView) mView.findViewById(R.id.dialog_email);
                TextView dialog_gender = (TextView) mView.findViewById(R.id.dialog_gender);
                Button btnDisOkey = (Button) mView.findViewById(R.id.btnDisOkey);

                dialog_name.setText(filteredlist.get(i).getName());
                dialog_phone.setText(filteredlist.get(i).getPhone());
                dialog_email.setText(filteredlist.get(i).getEmail());
                dialog_gender.setText(filteredlist.get(i).getGender());

                btnDisOkey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDialog.dismiss();
                    }
                });

                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        registerForContextMenu(lvResult);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Process the data here
                list = new ArrayList<MyContacts>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyContacts contact = snapshot.getValue(MyContacts.class);
                    list.add(contact);
                }

                refreshFilter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors (if any)
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                regex = charSequence.toString();
                refreshFilter();
            }
            public void afterTextChanged(Editable editable) {}
        });

        return rootView;
    }

    public void refreshFilter(){
        filteredlist.clear();
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        cnt = 0;
        for(MyContacts contact:list){
            Matcher m = p.matcher(contact.getName());
            if(m.find()){
                filteredlist.add(contact);
                cnt++;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.contextmenu, menu);
        info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(filteredlist.get(info.position).getName());
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String phone = filteredlist.get(info.position).getPhone();
        if(id == R.id.call){
            if(ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
            }else startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)));
        }else if(id == R.id.send){
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:" + phone));
                //intent.setType("vnd.android-dir/mms-sms");
                startActivity(intent);
            }catch(Exception e){
                Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
            }
        }else if(id == R.id.delete){
            String key = filteredlist.get(info.position).getkey();
            reference.child(key).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Contact successfully deleted
                        // Handle success as needed
                        Toast.makeText(getActivity(), "Contact Deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while deleting the contact
                        // Handle the error as needed
                        Toast.makeText(getActivity(), "Error Deleting Item", Toast.LENGTH_SHORT).show();
                    }
                });
            refreshFilter();
        }else if(id == R.id.update){
            Intent intent = new Intent(getActivity(), EditContactActivity.class);

            intent.putExtra("key", filteredlist.get(info.position).getkey());
            intent.putExtra("name", filteredlist.get(info.position).getName());
            intent.putExtra("phone", filteredlist.get(info.position).getPhone());
            intent.putExtra("email", filteredlist.get(info.position).getEmail());
            intent.putExtra("gender", filteredlist.get(info.position).getGender());
            startActivityForResult(intent, 1); // request code = 1; updating item
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 1){ // for updating
                Bundle b = data.getExtras();
                if(b !=null){
                    refreshFilter();
                    Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+filteredlist.get(info.position).getPhone())));
            }else Toast.makeText(getActivity(), "Permission declined", Toast.LENGTH_SHORT).show();
        }
    }

}