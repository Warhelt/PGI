package com.example.mariusz.pgi.saleshistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.model.SoldShares;

import java.util.List;
public class RecyclerViewSalesHistoryAdapter extends RecyclerView.Adapter<RecyclerViewSalesHistoryHolders> {

    private List<SoldShares> soldSharesList;
    protected Context context;

    public RecyclerViewSalesHistoryAdapter(Context context, List<SoldShares> soldSharesList) {
        this.context = context;
        this.soldSharesList = soldSharesList;
    }

    @Override
    public RecyclerViewSalesHistoryHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewSalesHistoryHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sales_history_item, parent, false);
        viewHolder = new RecyclerViewSalesHistoryHolders(layoutView, soldSharesList, context);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewSalesHistoryHolders holder, int position) {
        System.out.println("binduje");

        holder.companyNameSalesHistoryTxt.setText(soldSharesList.get(position).getCompanyName());
        holder.amountSalesHistoryTxt.setText(soldSharesList.get(position).getAmount());
        holder.buyPriceSalesHistoryTxt.setText(soldSharesList.get(position).getBuyPrice());
        holder.sellPriceSalesHistoryTxt.setText(soldSharesList.get(position).getSellPrice());
        holder.profitSalesHistoryTxt.setText(soldSharesList.get(position).getProfit());
        holder.percentageProfitSalesHistoryTxt.setText(soldSharesList.get(position).getPercentageProfit());


    }

    @Override
    public int getItemCount() {
        return this.soldSharesList.size();
    }
}
