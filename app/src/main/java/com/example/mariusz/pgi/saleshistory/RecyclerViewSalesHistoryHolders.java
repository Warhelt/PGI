package com.example.mariusz.pgi.saleshistory;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.model.Company;
import com.example.mariusz.pgi.model.OwnedShares;
import com.example.mariusz.pgi.model.SoldShares;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewSalesHistoryHolders extends RecyclerView.ViewHolder {

    public TextView companyNameSalesHistoryTxt;
    public TextView amountSalesHistoryTxt;
    public TextView buyPriceSalesHistoryTxt;
    public TextView sellPriceSalesHistoryTxt;
    public TextView profitSalesHistoryTxt;
    public TextView percentageProfitSalesHistoryTxt;
    private Context context;


    private List<SoldShares> soldSharesList;

    public RecyclerViewSalesHistoryHolders(View itemView, final List<SoldShares> soldSharesList, Context context) {
        super(itemView);
        this.context = context;
        companyNameSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistoryCompanyNameTxt);
        amountSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistoryAmountTxt);
        buyPriceSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistoryBuyPriceTxt);
        sellPriceSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistorySellPriceTxt);
        profitSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistoryProfitTxt);
        percentageProfitSalesHistoryTxt = (TextView) itemView.findViewById(R.id.salesHistoryProfitPercentageTxt);
        this.soldSharesList = soldSharesList;

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(soldSharesList.get(getAdapterPosition()).getBuyPrice());
            }
        });

    }

    public void deleteSoldPosition(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String companyName = soldSharesList.get(getAdapterPosition()).getCompanyName().toUpperCase();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("SoldShares");
        Query query = databaseRef.child(companyName).orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(soldSharesList.get(getAdapterPosition()).getTimeStamp())){
                        snapshot.getRef().removeValue();
                        soldSharesList.remove(getAdapterPosition());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setIcon(R.drawable.ic_delete_blue_24dp)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteSoldPosition();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        return myQuittingDialogBox;
    }
}