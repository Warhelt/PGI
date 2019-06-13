package com.example.mariusz.pgi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExchangeRatesFlagsAdapter extends ArrayAdapter<HashMap<String, String>> {

    private final Context context;
    private List<Integer> flagList;
    ArrayList<HashMap<String, String>> exchangeRatesList;

    public ExchangeRatesFlagsAdapter(Context context, ArrayList<HashMap<String, String>> exchangeRatesList) {
        super(context, R.layout.row_exchange_rates_item, exchangeRatesList);
        this.context=context;
        this.exchangeRatesList = exchangeRatesList;
        flagList = new ArrayList<>();
        generateFlagsList();
        System.out.println("flagi ilość: " + flagList.size());


    }

    public View getView(int position, View view, ViewGroup parent) {
        System.out.println("pozucja : " + position);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView=inflater.inflate(R.layout.row_exchange_rates_item, null,true);

        ImageView flagImg = (ImageView) rowView.findViewById(R.id.countryFlag);
        TextView countryName = (TextView) rowView.findViewById(R.id.currencyNameTxt);
        TextView currencyCode = (TextView) rowView.findViewById(R.id.currencyCodeTxt);
        TextView currencyBuyPrice = (TextView) rowView.findViewById(R.id.currentyBuyPriceTxt);
        TextView currencySellPrice = (TextView) rowView.findViewById(R.id.currencySellPriceTxt);


        flagImg.setImageResource(flagList.get(position));
        countryName.setText(exchangeRatesList.get(position).get("name"));
        currencyCode.setText(exchangeRatesList.get(position).get("code"));
        currencyBuyPrice.setText(exchangeRatesList.get(position).get("buyPrice"));
        currencySellPrice.setText(exchangeRatesList.get(position).get("sellPrice"));
        return rowView;

    };

    public void generateFlagsList(){
        flagList.add(R.drawable.usd);
        flagList.add(R.drawable.aud);
        flagList.add(R.drawable.cad);
        flagList.add(R.drawable.eur);
        flagList.add(R.drawable.huf);
        flagList.add(R.drawable.chf);
        flagList.add(R.drawable.gbp);
        flagList.add(R.drawable.jpy);
        flagList.add(R.drawable.czk);
        flagList.add(R.drawable.dkk);
        flagList.add(R.drawable.nok);
        flagList.add(R.drawable.sek);
        }

    public List<Integer> getFlagList() {
        return flagList;
    }

    public void setFlagList(List<Integer> flagList) {
        this.flagList = flagList;
    }
}