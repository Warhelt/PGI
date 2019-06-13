package com.example.mariusz.pgi.ownedshares;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mariusz.pgi.R;

import com.example.mariusz.pgi.model.OwnedShares;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OwnedSharesFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private LinearLayoutManager linearLayoutManager;
    private OwnedShares ownedShares;
    private RecyclerViewOwnedSharesAdapter recyclerViewOwnedSharesAdapter;
    private List<OwnedShares> ownedSharesList;

    @BindView(R.id.ownedSharesList)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.owned_shares_fragment, container,false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Posiadane Akcje");

        ownedSharesList = new ArrayList<>();
        recyclerViewOwnedSharesAdapter = new RecyclerViewOwnedSharesAdapter(getContext(),ownedSharesList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("OwnedShares");


        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllOwnedShares(dataSnapshot);
                fillOwnedSharesRecyclerView(ownedSharesList);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fillOwnedSharesRecyclerView(ownedSharesList);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fillOwnedSharesRecyclerView(ownedSharesList);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/*        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    for (DataSnapshot singlePostSnapshot: postSnapshot.getChildren()) {
                        OwnedShares ownedShares = new OwnedShares();
                        Map<String, Object> map = (Map<String, Object>) singlePostSnapshot.getValue();
                        ownedShares.setCompanyName(map.get("name").toString());
                        ownedShares.setAmount(map.get("amount").toString());
                        ownedShares.setBuyPrice(map.get("buyPrice").toString());
                        ownedSharesList.add(ownedShares);
                    }
                }
                fillOwnedSharesRecyclerView(ownedSharesList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("jest cancell");

            }
        });*/

        return view;
    }

    public void getAllOwnedShares(DataSnapshot dataSnapshot){
            for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                ownedShares = new OwnedShares();
                Map<String, Object> map = (Map<String, Object>) singleSnapshot.getValue();
                ownedShares.setCompanyName(map.get("name").toString());
                ownedShares.setAmount(map.get("amount").toString());
                ownedShares.setBuyPrice(map.get("buyPrice").toString());
                ownedShares.setTimeStamp(map.get("timeStamp").toString());
                ownedSharesList.add(ownedShares);
            }
    }

    private void fillOwnedSharesRecyclerView(List<OwnedShares> ownedSharesList) {
            recyclerViewOwnedSharesAdapter = new RecyclerViewOwnedSharesAdapter(getContext(), ownedSharesList);
            recyclerView.setAdapter(recyclerViewOwnedSharesAdapter);
            recyclerViewOwnedSharesAdapter.notifyDataSetChanged();
    }
}
