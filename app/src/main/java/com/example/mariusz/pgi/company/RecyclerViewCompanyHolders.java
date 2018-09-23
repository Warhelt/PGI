package com.example.mariusz.pgi.company;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.model.Company;
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

public class RecyclerViewCompanyHolders extends RecyclerView.ViewHolder {

    public TextView companyNameTxt;
    public ImageView deleteCompanyImgView;
    private List<Company> companyList;

    public RecyclerViewCompanyHolders(View itemView, final List<Company> companyList) {
        super(itemView);
        System.out.println("company Hoolder");
        companyNameTxt = (TextView) itemView.findViewById(R.id.companyNameTxt);
        deleteCompanyImgView = (ImageView) itemView.findViewById(R.id.deleteCompany   );
        this.companyList = companyList;
        deleteCompanyImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Kliknałem w smietniczke  nr : " + getAdapterPosition() );
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String companyName = companyList.get(getAdapterPosition()).getName().toUpperCase();
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Companies");
                System.out.println("key r : " + databaseRef.child(companyName).getKey());
                Query query = databaseRef.child(companyName).orderByValue();
                System.out.println("query w string  nr : " + query.toString());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("Wykonuje sie ondatachange");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            System.out.println("snapshot.getValue().toString() : " + snapshot.getValue().toString());

                            if(snapshot.getValue().toString().equals(companyName)){
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });

    }
}
