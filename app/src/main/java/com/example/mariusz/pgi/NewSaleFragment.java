package com.example.mariusz.pgi;

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

import com.example.mariusz.pgi.company.CompanyFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSaleFragment extends Fragment {

    @BindView(R.id.saleSpinner)
    Spinner spinner;
    @BindView(R.id.chargeSpinner)
    Spinner spinnerCharge;
    @BindView(R.id.saleAmountEditTxt)
    EditText saleAmountEditTxt;
    @BindView(R.id.saleBuyPriceEditTxt)
    EditText saleBuyPriceEditTxt;
    @BindView(R.id.saleSellPriceEditTxt)
    EditText saleSellPriceEditTxt;
    @BindView(R.id.saleProfitTxt)
    TextView saleProfitTxt;
    @BindView(R.id.saleChargeTxt)
    TextView saleChargeTxt;
    @BindView(R.id.saleProcentProfitTxt)
    TextView saleProcentProfitTxt;


    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private List<String> companyNameList;
    private DecimalFormat decimalFormat;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_sale_fragment, container,false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj nową sprzedaż");
        decimalFormat = new DecimalFormat("####0.00");

        mAuth = FirebaseAuth.getInstance();
        fillSpinner();
        fillChargeSpinner();
        return view;

    }

    @OnClick(R.id.countProfitBtn)
    public void countProfit() {

        String amount = saleAmountEditTxt.getText().toString();
        String buyPrice = saleBuyPriceEditTxt.getText().toString();
        String sellPrice = saleSellPriceEditTxt.getText().toString();


        if(!amount.isEmpty() && !buyPrice.isEmpty() && !sellPrice.isEmpty() && spinner.getSelectedItemPosition() > 0 && spinnerCharge.getSelectedItemPosition() > 0) {
            Double percentageCharge = Double.parseDouble(spinnerCharge.getSelectedItem().toString());
            Double countedCharge = (((Double.parseDouble(sellPrice) + Double.parseDouble(buyPrice)) / 2) * Double.parseDouble(amount) * percentageCharge) / 100;

            Double profit = ((Double.parseDouble(sellPrice) - Double.parseDouble(buyPrice)) * Double.parseDouble(amount)) - countedCharge;
            Double percentageProfit = (Double.parseDouble(sellPrice) - Double.parseDouble(buyPrice)) / Double.parseDouble(buyPrice) * 100 - percentageCharge;
            saleProfitTxt.setText(String.valueOf(decimalFormat.format(profit)));
            saleProcentProfitTxt.setText(String.valueOf(decimalFormat.format(percentageProfit)));
            saleChargeTxt.setText(String.valueOf(decimalFormat.format(countedCharge)));

        }
    }


    @OnClick(R.id.addNewSaleBtn)
    public void addNewSale(){
        String amount = saleAmountEditTxt.getText().toString();
        String buyPrice = saleBuyPriceEditTxt.getText().toString();
        String sellPrice = saleSellPriceEditTxt.getText().toString();
        if(!amount.isEmpty() && !buyPrice.isEmpty() && !sellPrice.isEmpty() && spinner.getSelectedItemPosition() > 0 && spinnerCharge.getSelectedItemPosition() > 0){
            String companyName = spinner.getSelectedItem().toString();
            Double percentageCharge = Double.parseDouble(spinnerCharge.getSelectedItem().toString());
            Double countedCharge = (((Double.parseDouble(sellPrice) + Double.parseDouble(buyPrice)) / 2) * Double.parseDouble(amount) * percentageCharge) / 100;
            Double profit = ((Double.parseDouble(sellPrice) - Double.parseDouble(buyPrice)) * Double.parseDouble(amount)) - countedCharge;
            Double percentageProfit = (Double.parseDouble(sellPrice) - Double.parseDouble(buyPrice)) / Double.parseDouble(buyPrice) * 100 - percentageCharge;
            String timeStamp = getCurrentTimeStamp();
            mAuth = FirebaseAuth.getInstance();
            String user_id = mAuth.getCurrentUser().getUid();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("SoldShares").child(companyName).child(timeStamp);
            HashMap<String, String> newSoldMap = new HashMap<>();
            newSoldMap.put("name", companyName);
            newSoldMap.put("amount", amount);
            newSoldMap.put("buyPrice", buyPrice);
            newSoldMap.put("sellPrice", sellPrice);
            newSoldMap.put("profit", String.valueOf(decimalFormat.format(profit)).replace(',','.'));
            newSoldMap.put("percentageProfit", String.valueOf(decimalFormat.format(percentageProfit)).replace(',','.'));
            newSoldMap.put("timeStamp", timeStamp);
            databaseRef.setValue(newSoldMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(),"Dodano nową sprzedaż.", Toast.LENGTH_SHORT).show();
                        cleanFields();
                    }else{
                        Toast.makeText(getContext(),"Nie udało się dodać nowej sprzedaży.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(getContext(),"Aby dodać nowy zakup musisz wypełnić wszystkie pola.", Toast.LENGTH_SHORT).show();
        }
    }

    public void cleanFields(){
        spinner.setSelection(0);
        saleAmountEditTxt.setText("");
        saleSellPriceEditTxt.setText("");
        saleBuyPriceEditTxt.setText("");
        saleProcentProfitTxt.setText("");
        saleProfitTxt.setText("");
        saleChargeTxt.setText("");
    }

    public void fillSpinner() {
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

    public void fillChargeSpinner(){
        List<String> spinnerChargeArray =  new ArrayList<String>();
        spinnerChargeArray.add("Wybierz prowizję");
        spinnerChargeArray.add("0.20");
        spinnerChargeArray.add("0.30");
        spinnerChargeArray.add("0.40");
        spinnerChargeArray.add("0.50");
        spinnerChargeArray.add("0.60");
        spinnerChargeArray.add("0.70");
        spinnerChargeArray.add("0.80");
        spinnerChargeArray.add("0.90");
        spinnerChargeArray.add("1");


        if(getActivity() != null){
            ArrayAdapter<String> chargeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerChargeArray){
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
            chargeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCharge.setAdapter(chargeAdapter);
        }
        spinnerCharge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
