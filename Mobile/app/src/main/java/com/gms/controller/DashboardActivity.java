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

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle("Control");
//        getSupportActionBar().setSubtitle("Control Devices");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(Html.fromHtml("<font color='#ffc107'>"+"Control Devices"+"</font>"));

        //Listen for changes in the back stack
//        getSupportFragmentManager().addOnBackStackChangedListener(this);
        //Handle when activity is recreated like on orientation Change
//        shouldDisplayHomeUp();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_sprinkler, R.id.navigation_alarm, R.id.navigation_message)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);




//        TextView textViewFirstLine = (TextView) findViewById(R.id.textViewFirstLine);
//        TextView textViewSecondLine = (TextView) findViewById(R.id.textViewSecondLine);
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Square-Dot-Matrix.ttf");
//        textViewFirstLine.setTypeface(typeface);
//        textViewSecondLine.setTypeface(typeface);

//        Button buttonStartWatering = (Button) findViewById(R.id.buttonStartWatering);
//
//        buttonStartWatering.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final EditText editTextCycle = (EditText) findViewById(R.id.editTextCycle);
//                final Spinner spinnerUnit = (Spinner) findViewById(R.id.spinnerUnit);
//
//                String site = SITE;
//                String fields[] = {"userId", "password"};
//
//                // to make data[1]: userId='a'&password='abc'
//                String params = putParamTogether(fields, editTextCycle.getText().toString(),
//                                                        spinnerUnit.getSelectedItem().toString());
//
//                // connect to server for auth. : param1 (site), param2 (array)
//                new ServerAsyncRequester(DashboardActivity.this).execute(site, params);
//            }
//        });
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
