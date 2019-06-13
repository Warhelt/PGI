package com.example.mariusz.pgi.saleshistory;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.company.RecyclerViewCompanyAdapter;
import com.example.mariusz.pgi.model.Company;
import com.example.mariusz.pgi.model.OwnedShares;
import com.example.mariusz.pgi.model.SoldShares;
import com.example.mariusz.pgi.ownedshares.RecyclerViewOwnedSharesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalesHistoryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private LinearLayoutManager linearLayoutManager;

    private RecyclerViewSalesHistoryAdapter recyclerViewSalesHistoryAdapter;
    private List<SoldShares> soldSharesList;
    private SoldShares soldShares;
    @BindView(R.id.salesHistoryList)
    RecyclerView recyclerView;

    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sales_history_fragment, container,false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Historia Sprzedaży");

        soldSharesList = new ArrayList<>();
        recyclerViewSalesHistoryAdapter = new RecyclerViewSalesHistoryAdapter(getContext(),soldSharesList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("SoldShares");
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllSoldPosition(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fillSalesHistoryRecyclerView(soldSharesList);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                fillSalesHistoryRecyclerView(soldSharesList);
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


    private void getAllSoldPosition(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    soldShares = new SoldShares();
                    Map<String, Object> map = (Map<String, Object>) postSnapshot.getValue();
                    soldShares.setCompanyName(map.get("name").toString());
                    soldShares.setAmount(map.get("amount").toString());
                    soldShares.setBuyPrice(map.get("buyPrice").toString());
                    soldShares.setSellPrice(map.get("sellPrice").toString());
                    soldShares.setProfit(map.get("profit").toString());
                    soldShares.setPercentageProfit(map.get("percentageProfit").toString());
                    soldShares.setTimeStamp(map.get("timeStamp").toString());
                    soldSharesList.add(soldShares);
        }
            fillSalesHistoryRecyclerView(soldSharesList);
    }

    private void fillSalesHistoryRecyclerView(List<SoldShares> soldSharesList) {
        recyclerViewSalesHistoryAdapter = new RecyclerViewSalesHistoryAdapter(getContext(), soldSharesList);
        recyclerView.setAdapter(recyclerViewSalesHistoryAdapter);
        recyclerViewSalesHistoryAdapter.notifyDataSetChanged();
    }
}
