package com.mogo.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.mogo.R;
import com.mogo.view.adapters.NavigationDrawerAdapter;
import com.mogo.view.customcontrols.TextViewRegular;
import com.mogo.view.fragments.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static HomeActivity instance;
    private GoogleMap map;
    private static final int TAKE_PICTURE = 1;
    private static final int GALLERY_PICK = 2;
    @BindView(R.id.recyclerViewNavigation)
    RecyclerView recyclerView;

    FragmentManager fm;
    FragmentTransaction ft;

    @BindView(R.id.titleIV)
    TextViewRegular titleTV;

    public static ImageView mapIV;

    public static ImageView searchIV;

    public static TextViewRegular saveTV;
    public static ImageView filterSearchIV;
    public static ImageView filterSearchListIV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new NavigationDrawerAdapter(this, getResources().getStringArray(R.array.labels)));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setFragment(new HomeFragment());
        titleTV.setText(getString(R.string.home));
        mapIV = (ImageView) findViewById(R.id.mapIV);
        saveTV = (TextViewRegular) findViewById(R.id.saveTV);
        searchIV = (ImageView) findViewById(R.id.searchIV);
        filterSearchIV = (ImageView) findViewById(R.id.filterSearchIV);
        filterSearchListIV = (ImageView) findViewById(R.id.filterSearchListIV);
    }

    public static HomeActivity getinstance() {
        return instance;
    }


    @OnClick(R.id.mapIV)
    public void onMapImageClick() {

        Intent intent = new Intent(this, EventsListActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            // Fragment frag = getCurrentFragmentInstance();
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void setFragment(Fragment fragment) {
        closeDrawer();

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment).commit();
        Log.e("stackcount", fm.getBackStackEntryCount() + "");
    }


    public void setTitle(String title) {
        titleTV.setText(title);
    }

    @OnClick(R.id.menu_iconIV)
    public void openDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
