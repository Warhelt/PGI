package com.example.mariusz.pgi.company;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.model.Company;

import java.util.List;

public class RecyclerViewCompanyAdapter extends RecyclerView.Adapter<RecyclerViewCompanyHolders> {

    private List<Company> companyList;
    protected Context context;
    private RecyclerViewCompanyHolders viewHolder;


    public RecyclerViewCompanyAdapter(Context context, List<Company> companyList){
        this.context = context;
        this.companyList = companyList;
    }

    @Override
    public RecyclerViewCompanyHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_company_item,parent,false);
        viewHolder = new RecyclerViewCompanyHolders(layoutView,companyList);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewCompanyHolders holder, int position) {
        holder.companyNameTxt.setText(companyList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        System.out.println("size company : "+ companyList.size());
        return this.companyList.size();
    }
}
