package com.example.mariusz.pgi.ownedshares;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.model.OwnedShares;

import java.util.List;

public class RecyclerViewOwnedSharesAdapter extends RecyclerView.Adapter<RecyclerViewOwnedSharesHolders> {

    private List<OwnedShares> ownedSharesList;
    protected Context context;

    public RecyclerViewOwnedSharesAdapter(Context context, List<OwnedShares> ownedSharesList){
        this.context = context;
        this.ownedSharesList = ownedSharesList;
    }

    @Override
    public RecyclerViewOwnedSharesHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewOwnedSharesHolders viewHolder= null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_owned_shares_item,parent,false);
        viewHolder = new RecyclerViewOwnedSharesHolders(layoutView, ownedSharesList, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewOwnedSharesHolders holder, int position) {
        holder.companyNameOwnedSharesTxt.setText(ownedSharesList.get(position).getCompanyName());
        holder.amountOwnedSharesTxt.setText(ownedSharesList.get(position).getAmount());
        holder.buyPriceOwnedSharesTxt.setText(ownedSharesList.get(position).getBuyPrice());

    }

    @Override
    public int getItemCount() {
        return this.ownedSharesList.size();
    }
}
