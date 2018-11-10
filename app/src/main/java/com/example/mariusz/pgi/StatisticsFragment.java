package com.example.mariusz.pgi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mariusz.pgi.model.OwnedShares;
import com.example.mariusz.pgi.model.SoldShares;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticsFragment extends Fragment {

    @BindView(R.id.idPieChart)
    PieChart pieChart;
    @BindView(R.id.ownedSharesChartBtn)
    Button ownedSharesChartBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRefOwnedShares;
    private DatabaseReference databaseRefSoldShares;
    private List<OwnedShares> ownedSharesList;
    private List<SoldShares> soldSharesList;
    Map<String, Integer> map;
    Map<String, Double> mapSoldShares;
    ArrayList<Integer> colors = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container,false);
        ButterKnife.bind(this, view);
        ownedSharesList = new ArrayList<>();
        soldSharesList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        databaseRefOwnedShares = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("OwnedShares");
        databaseRefOwnedShares.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllOwnedShares(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseRefSoldShares = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("SoldShares");
        databaseRefSoldShares.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                getAllSoldShares(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.CYAN);
        colors.add(Color.YELLOW);
        colors.add(Color.MAGENTA);

        return view;
    }


    private void addDataSet(List<PieEntry> yEntrys, List<LegendEntry> entries, String name) {
//        List<Integer> sharesAmounts = new ArrayList<>(map.values());
//        List<PieEntry> yEntrys = new ArrayList<>();
//
//        for(int i = 0; i < map.size(); i++){
//            yEntrys.add(new PieEntry(sharesAmounts.get(i), i));
//        }
        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, name);
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(14);
        pieDataSet.setValueLineColor(Color.WHITE);

        //add colors to dataset

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.DEFAULT);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

//        List<LegendEntry> entries = new ArrayList<>();
//        List<String> sharesNames = new ArrayList<String>(map.keySet());
//        for (int i = 0; i < map.size(); i++) {
//            LegendEntry entry = new LegendEntry();
//            entry.formColor = colors.get(i);
//            entry.label = sharesNames.get(i);
//            entry.formSize = 14;
//
//            entries.add(entry);
//        }
        legend.setCustom(entries);
        legend.setTextColor(Color.WHITE);
        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public void getAllOwnedShares(DataSnapshot dataSnapshot){
        for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
            OwnedShares ownedShares = new OwnedShares();
            Map<String, Object> map = (Map<String, Object>) singleSnapshot.getValue();
            ownedShares.setCompanyName(map.get("name").toString());
            ownedShares.setAmount(map.get("amount").toString());
            ownedShares.setBuyPrice(map.get("buyPrice").toString());
            ownedShares.setTimeStamp(map.get("timeStamp").toString());
            ownedSharesList.add(ownedShares);
        }
    }

    public void getAllSoldShares(DataSnapshot dataSnapshot){
        for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
            SoldShares soldShares = new SoldShares();
            Map<String, Object> map = (Map<String, Object>) singleSnapshot.getValue();
            soldShares.setCompanyName(map.get("name").toString());
            soldShares.setAmount(map.get("amount").toString());
            soldShares.setBuyPrice(map.get("buyPrice").toString());
            soldShares.setProfit(map.get("profit").toString());
            soldSharesList.add(soldShares);
        }
    }


    public void fillChart(String name){
        Description description = new Description();
        description.setText(name);
        pieChart.setDescription(description);
        pieChart.setRotationEnabled(true);
        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText(name);
        pieChart.setCenterTextSize(10);
    }

    public void generateOwnedSharesEntries(){
        List<Integer> sharesAmounts = new ArrayList<>(map.values());
        List<PieEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < map.size(); i++){
            yEntrys.add(new PieEntry(sharesAmounts.get(i), i));
        }

        List<LegendEntry> entries = new ArrayList<>();
        List<String> sharesNames = new ArrayList<String>(map.keySet());
        for (int i = 0; i < map.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = sharesNames.get(i);
            entry.formSize = 14;

            entries.add(entry);
        }
        addDataSet(yEntrys, entries, "Posiadane akcje");
    }

    public void generateSoldSharesEntries(){
        List<Double> sharesAmounts = new ArrayList<>(mapSoldShares.values());
        List<PieEntry> yEntrys = new ArrayList<>();

        for(int i = 0; i < mapSoldShares.size(); i++){
            yEntrys.add(new PieEntry(sharesAmounts.get(i).floatValue(), i));
        }

        List<LegendEntry> entries = new ArrayList<>();
        List<String> sharesNames = new ArrayList<String>(mapSoldShares.keySet());
        for (int i = 0; i < mapSoldShares.size(); i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colors.get(i);
            entry.label = sharesNames.get(i);
            entry.formSize = 14;

            entries.add(entry);
        }
        addDataSet(yEntrys, entries, "Zysk ze sprzedaży");
    }

    @OnClick(R.id.ownedSharesChartBtn)
    public void fillOwnedSharesChart(){
        generateOwnedShares();
        fillChart("Posiadane akcje");
        generateOwnedSharesEntries();
    }

    @OnClick(R.id.soldSharesChartBtn)
    public void fillSoldSharesChart(){
        generateSoldShares();
        fillChart("Zysk ze sprzedaży");
        generateSoldSharesEntries();
    }


    public void generateOwnedShares(){
        map = new TreeMap<>();
        for(OwnedShares ownedShares : ownedSharesList){
            if(map.containsKey(ownedShares.getCompanyName())){
                map.put(ownedShares.getCompanyName(), map.get(ownedShares.getCompanyName()) + Integer.parseInt(ownedShares.getAmount()));
            }else{
                map.put(ownedShares.getCompanyName(), Integer.parseInt(ownedShares.getAmount()));
            }
        }
    }
    public void generateSoldShares(){
        mapSoldShares = new TreeMap<>();
        for(SoldShares soldShares : soldSharesList){
            if(mapSoldShares.containsKey(soldShares.getCompanyName())){
                mapSoldShares.put(soldShares.getCompanyName(), mapSoldShares.get(soldShares.getCompanyName()) + Double.parseDouble(soldShares.getProfit()));
            }else{
                mapSoldShares.put(soldShares.getCompanyName(), Double.parseDouble(soldShares.getProfit()));
            }
        }
    }

}
