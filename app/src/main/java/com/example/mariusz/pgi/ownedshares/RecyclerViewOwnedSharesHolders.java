package com.example.mariusz.pgi.ownedshares;

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

public class RecyclerViewOwnedSharesHolders extends RecyclerView.ViewHolder {

    public TextView companyNameOwnedSharesTxt;
    public TextView amountOwnedSharesTxt;
    public TextView buyPriceOwnedSharesTxt;

    private List<OwnedShares> ownedSharesList;
    private Context context;

    public RecyclerViewOwnedSharesHolders(View itemView, final List<OwnedShares> ownedSharesList, Context context) {
        super(itemView);

        System.out.println("constructor Kolder owned");
        companyNameOwnedSharesTxt = (TextView) itemView.findViewById(R.id.ownedSharesCompanyNameTxt);
        amountOwnedSharesTxt = (TextView) itemView.findViewById(R.id.ownedSharesAmountTxt);
        buyPriceOwnedSharesTxt = (TextView) itemView.findViewById(R.id.ownedSharesBuyPriceTxt);
        this.ownedSharesList = ownedSharesList;
        this.context = context;

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
                return true;
            }
        });

    }

    public void deleteSoldPosition(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String companyName = ownedSharesList.get(getAdapterPosition()).getCompanyName().toUpperCase();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("OwnedShares");
        Query query = databaseRef.child(companyName).orderByValue();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(ownedSharesList.get(getAdapterPosition()).getTimeStamp())){
                        snapshot.getRef().removeValue();
                        ownedSharesList.remove(getAdapterPosition());
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
                .setMessage("Do you want to Delete")
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
