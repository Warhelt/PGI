package com.example.mariusz.pgi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mariusz.pgi.model.Company;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyFragment extends Fragment {

    @BindView(R.id.addCompanyEditTxt)
    EditText addCompanyEditTxt;
    @BindView(R.id.companyList)
    RecyclerView recyclerView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerViewCompanyAdapter recyclerViewCompanyAdapter;
    private List<Company> companyList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.company_fragment, container,false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj nową spółkę");

        mAuth = FirebaseAuth.getInstance();
        companyList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        String user_id = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Companies");
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllCompany(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getAllCompany(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getContext(), "Rozmoczynam removed", Toast.LENGTH_SHORT).show();
                deleteCompany(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void deleteCompany(DataSnapshot dataSnapshot) {
        for(DataSnapshot singleDataSnapshot : dataSnapshot.getChildren()){
            String companyName = singleDataSnapshot.getValue(String.class);
            for (int i = 0; i < companyList.size() ; i++) {
                if(companyList.get(i).getName().equals(companyName)){
                    companyList.remove(i);
                    recyclerViewCompanyAdapter = new RecyclerViewCompanyAdapter(getContext(),companyList);
                    recyclerView.setAdapter(recyclerViewCompanyAdapter);
                    addCompanyEditTxt.setText("");
                    Toast.makeText(getContext(), "Usunięto", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }

    private void getAllCompany(DataSnapshot dataSnapshot) {
        for (DataSnapshot singleSnapShot :  dataSnapshot.getChildren()) {
            String companyName = singleSnapShot.getValue(String.class);
            companyList.add(new Company(companyName));
            recyclerViewCompanyAdapter = new RecyclerViewCompanyAdapter(getContext(), companyList);
            recyclerView.setAdapter(recyclerViewCompanyAdapter);

        }
    }

    @OnClick(R.id.addNewCompany)
    public void addNewCompany(){

        if(!addCompanyEditTxt.getText().toString().equals("")){
            final String companyName = addCompanyEditTxt.getText().toString();

            databaseRef.child(companyName.toUpperCase()).setValue(new Company(companyName.toUpperCase())).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "Dodano spółkę: " + companyName, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Wystapił błąd przy dodawaniu.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            addCompanyEditTxt.setText("");
        }else{
            Toast.makeText(getContext(), "Pole z nazwą spółki nie może być pustę", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
