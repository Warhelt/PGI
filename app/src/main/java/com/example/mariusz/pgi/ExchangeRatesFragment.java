package com.example.mariusz.pgi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mariusz.pgi.exchangerates.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeRatesFragment extends Fragment {


    @BindView(R.id.exchangeRatesList)
    ListView exchangeRatesListView;

    private ExchangeRatesFlagsAdapter flagsAdapter;
    private HttpHandler httpHandler;
    ArrayList<HashMap<String, String>> exchangeRatesList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange_rates_fragment, container,false);
        ButterKnife.bind(this,view);
        exchangeRatesList = new ArrayList<>();

        new GetCurrency().execute();

        return view;
    }

    private class GetCurrency extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(),"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            httpHandler = new HttpHandler();
            String url = "http://api.nbp.pl/api/exchangerates/tables/c?format=json";
            String jsonStr = httpHandler.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    JSONArray currencies = new JSONArray(jsonStr);
                    JSONObject jsonObject = currencies.getJSONObject(0);
                    JSONArray jsonArray = jsonObject.getJSONArray("rates");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        if(c.getString("code").equals("XDR")){
                            continue;
                        }
                        String name = c.getString("currency");
                        String code = c.getString("code");
                        String buyPrice = c.getString("bid");
                        String sellPrice = c.getString("ask");

                        HashMap<String, String> currencyMap = new HashMap<>();
                        currencyMap.put("name", name);
                        currencyMap.put("code", code);
                        currencyMap.put("buyPrice", buyPrice);
                        currencyMap.put("sellPrice", sellPrice);
                        exchangeRatesList.add(currencyMap);
                    }
                } catch (final JSONException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            flagsAdapter = new ExchangeRatesFlagsAdapter(getContext(),exchangeRatesList);
            exchangeRatesListView.setAdapter(flagsAdapter);
        }
    }
}
