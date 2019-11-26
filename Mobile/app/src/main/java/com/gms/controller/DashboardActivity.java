package com.gms.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DashboardActivity extends AppCompatActivity
                        implements OnEventListener <String>{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(Html.fromHtml("<font color='#ffc107'>"+"Control Devices"+"</font>"));

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_sprinkler, R.id.navigation_alarm, R.id.navigation_message)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        return true;
    }


    private String putParamTogether(String fields[], String... values) {
        StringBuilder sb = new StringBuilder();


        // the key must have the same spelling and case when extracted by the web app
        for (int i = 0; i < fields.length; i++ ) {
            try {
                // https://developer.android.com/reference/java/net/URLEncoder
                values[i] = URLEncoder.encode(values[i], "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            if (sb.length() > 0)
                sb.append("&"); // to separate each entry

            // add condition (where)
            sb.append(fields[i]).append("=").append(""+values[i]+"");
        }

        return sb.toString();
    }
}
