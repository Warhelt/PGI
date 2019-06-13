package com.example.mariusz.pgi.newpurchase;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mariusz.pgi.R;
import com.example.mariusz.pgi.company.CompanyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPurchaseFragment extends Fragment {

    @BindView(R.id.purchaseSpinner)
    Spinner spinner;
    @BindView(R.id.createNewCompany)
    ImageView createNewCompany;
    @BindView(R.id.purchaseAmountEditTxt)
    EditText purchaseAmountEditTxt;
    @BindView(R.id.purchaseBuyPriceEditTxt)
    EditText purchaseBuyPriceEditTxt;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private List<String> companyNameList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_purchase_fragment, container,false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj nowy zakup");

        mAuth = FirebaseAuth.getInstance();
        fillSpinner(view);

        return view;

    }

    @OnClick(R.id.createNewCompany)
    public void openAddNewCompany(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CompanyFragment()).addToBackStack(null).commit();
    }

    @OnClick(R.id.addNewPurchaseBtn)
    public void addNewPurchase(){
        String amount = purchaseAmountEditTxt.getText().toString();
        String buyPrice = purchaseBuyPriceEditTxt.getText().toString();
        if(!amount.isEmpty() && !buyPrice.isEmpty() && spinner.getSelectedItemPosition() > 0){
            String companyName = spinner.getSelectedItem().toString();
            mAuth = FirebaseAuth.getInstance();
            String user_id = mAuth.getCurrentUser().getUid();
            String timeStamp = getCurrentTimeStamp();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("OwnedShares").child(companyName).child(timeStamp);
            HashMap<String, String> newPurchaseMap = new HashMap<>();
            newPurchaseMap.put("name", companyName);
            newPurchaseMap.put("amount", amount);
            newPurchaseMap.put("buyPrice", buyPrice);
            newPurchaseMap.put("timeStamp", timeStamp);
            databaseRef.setValue(newPurchaseMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Dodano nowy zakup.", Toast.LENGTH_SHORT).show();
                        cleanFields();
                    }else{
                        Toast.makeText(getContext(),"Nie udało się dodać nowego zakupu.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getContext(),"Aby dodać nowy zakup musisz wypełnić wszystkie pola.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cleanFields(){
         spinner.setSelection(0);
        purchaseAmountEditTxt.setText("");
        purchaseBuyPriceEditTxt.setText("");
    }

    public void fillSpinner(final View view) {
        companyNameList = new ArrayList<>();
        companyNameList.add("Wybierz spółkę");
        String user_id = mAuth.getCurrentUser().getUid();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Companies");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot companySnapshot: dataSnapshot.getChildren()) {
                    String companyName = companySnapshot.child("name").getValue(String.class);
                    companyNameList.add(companyName);
                }
                if(getActivity() != null){
                    ArrayAdapter<String> companyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, companyNameList){
                        @Override
                        public boolean isEnabled(int position){
                            if(position == 0)
                            {
                                // Disable the first item from Spinner
                                // First item will be use for hint
                                return false;
                            }
                            else
                            {
                                return true;
                            }
                        }
                        @Override
                        public View getDropDownView(int position, View convertView,
                                                    ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            if(position == 0){
                                // Set the hint text color gray
                                tv.setTextColor(getResources().getColor(R.color.darkBlue));
                            }
                            else {
                                tv.setTextColor(getResources().getColor(R.color.white));
                            }
                            return view;
                        }
                    };
                    companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(companyAdapter);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                TextView tv = (TextView) view;
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                    tv.setTextColor(getResources().getColor(R.color.white));
                }else{
                    tv.setTextColor(getResources().getColor(R.color.darkBlue));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static String getCurrentTimeStamp() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

}
