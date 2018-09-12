package com.example.mariusz.pgi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mariusz.pgi.model.Company;

import java.util.List;

public class RecyclerViewCompanyAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<Company> companyList;
    protected Context context;

    public RecyclerViewCompanyAdapter(Context context, List<Company> companyList){
        this.context = context;
        this.companyList = companyList;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder= null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_company_item,parent,false);
        viewHolder = new RecyclerViewHolders(layoutView,companyList);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.companyNameTxt.setText(companyList.get(position).getName());

    }

    public void removeCompany(Company company){
        companyList.remove(company);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.companyList.size();
    }
}
