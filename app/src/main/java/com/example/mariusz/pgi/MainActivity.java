package com.example.mariusz.pgi;

import android.app.SearchManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mariusz.pgi.newpurchase.NewPurchaseFragment;
import com.example.mariusz.pgi.ownedshares.OwnedSharesFragment;
import com.example.mariusz.pgi.saleshistory.SalesHistoryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseReference database;
    private DatabaseReference mUserRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        }
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            System.out.println("on create if");
            sendToStart();
        }else{
            System.out.println("on create else");
        }
        setSupportActionBar(toolbar);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar , R.string.open, R.string.close );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()){
                    case R.id.sales_history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SalesHistoryFragment()).commit();
                        toolbar.setTitle("Historia sprzedaży");
                        break;
                    case R.id.owned_shares:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OwnedSharesFragment()).commit();
                        toolbar.setTitle("Posiadane akcje");
                        break;
                    case R.id.new_purchase:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewPurchaseFragment()).commit();
                        toolbar.setTitle("Dodaj nowy zakup");
                        break;
                    case R.id.new_sale:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewSaleFragment()).commit();
                        toolbar.setTitle("Dodaj nową sprzedaż");
                        break;
                    case R.id.exchange_rates:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ExchangeRatesFragment()).commit();
                        toolbar.setTitle("Kursy walut");
                        break;
                    case R.id.statistics:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StatisticsFragment()).commit();
                        toolbar.setTitle("Statystyki");
                        break;
                    case R.id.author:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AuthorFragment()).commit();
                        toolbar.setTitle("O autorze");
                        break;
                    case R.id.rates:
                    Intent intent = new Intent(getApplicationContext(), RatesActivity.class);
                    startActivity(intent);
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        sendToStart();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SalesHistoryFragment()).commit();
            navigationView.setCheckedItem(R.id.sales_history);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (count == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            System.out.println("Uruchamiam Main activity -  nieudło się wracam");
            sendToStart();
        }else{
            System.out.println("Uruchamiam Main activity -  udało się wracam");
        }

    }

    private void sendToStart(){
        Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginActivity);
        finish();
    }
    public Toolbar getToolbar(){
        return toolbar;
    }

}
