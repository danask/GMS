package com.gms.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionListActivity extends AppCompatActivity implements OnEventListener<String> {

    private ListCustomAdapter adapter;
    private ListView listView;

    ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<String> typeList = new ArrayList<>();
    ArrayList<String> paramList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(Html.fromHtml("<font color='#ffc107'>"+"Action List"+"</font>"));

        // 1st time
        new ServerAsyncRequester(ActionListActivity.this).execute(SITE, "H", "");  // Status

    }

    @Override
    public void onSuccess(String result) {
        if(result.equals(null) || result.equals("[]\n"))
        {
            Toast.makeText(getApplicationContext(),"Fail to get data", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String type = result.substring(0, 1);
            String[] columns = {"category", "type", "param1", "param2", "param3", "date"};
            result = result.substring(1);

            JSONDataExtractor jsonDataExtractor = new JSONDataExtractor();
            String returnStr = jsonDataExtractor.extract(this, result, columns);

            String[] returnObjects = returnStr.split(",");


            if (returnObjects != null && type.equalsIgnoreCase("H")) {

                for(int i = 0; i < returnObjects.length; i++)
                {
                    String[] object = returnObjects[i].split("-");
                    categoryList.add(object[0]);
                    typeList.add(object[1]);

                    if(object[2].equals("") || object[2].equals("null")) object[2] = "no value";
                    if(object[3].equals("") || object[3].equals("null")) object[3] = "no value";
                    if(object[4].equals("") || object[4].equals("null")) object[4] = "no value";

                    paramList.add(object[2] + " | " + object[3] + " | "+ object[4]);
                    timeList.add(object[5] + "-" + object[6] + "-" + object[7]);
                }

                adapter = new ListCustomAdapter(typeList, categoryList, paramList, timeList);
                listView = (ListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Object listItem = listView.getItemAtPosition(position);

                        retryAction(listItem, position);
                    }
                });



            }
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();

        return true;
    }


    private void retryAction(Object listItem, int position)
    {
        if(listItem.toString().equals("Start Watering"))
        {
            String fields[] = {"category", "type", "cycle", "status", "param1", "param2", "retry"};

            String[] currentParams = paramList.get(position).split(" | ");
            String unit ="cycle";
            double number = Math.ceil(Double.parseDouble(currentParams[2])); // not 1

            if(currentParams[0].equalsIgnoreCase("liter")) {
                number = number * 60 * 6;
                unit = "liter";
            }
            else if(currentParams[1].equalsIgnoreCase("time")) {
                number = number * 6;
                unit = "time";
            }

            String cycle = String.valueOf((int)Math.ceil(number));
            String params = JSONHandler.putParamTogether(fields, "Sprinkler", "Start Watering", cycle, "on", unit, cycle, "yes");

            new ServerAsyncRequester(ActionListActivity.this).execute(SITE, "M", params);  // Motor control
//            Toast.makeText(getApplicationContext(), params, Toast.LENGTH_SHORT).show();
        }

        if(listItem.toString().equals("Stop Watering"))
        {
            String fields[] = {"category", "type", "cycle", "status", "param1", "param2", "retry"};

            String unit ="cycle";
            String params = JSONHandler.putParamTogether(fields, "Sprinkler", "Start Watering", "0", "on", unit, "0", "yes");

            new ServerAsyncRequester(ActionListActivity.this).execute(SITE, "M", params);  // Motor control
//            Toast.makeText(getApplicationContext(), params, Toast.LENGTH_SHORT).show();
        }

        if(listItem.toString().equals("Dismiss Alarm"))
        {
            String fields[] = {"category", "type", "passcode", "param1", "retry"};
            String[] currentParams = paramList.get(position).split(" | ");
            String params = JSONHandler.putParamTogether(fields, "Alarm", "Dismiss Alarm",
                    currentParams[0], currentParams[0], "yes");

            new ServerAsyncRequester(ActionListActivity.this).execute(SITE, "A", params);
//            Toast.makeText(getApplicationContext(), params, Toast.LENGTH_SHORT).show();
        }

        if(listItem.toString().equals("Send Message"))
        {
            String fields[] = {"category", "type", "firstLine", "secondLine", "duration", "param1", "param2", "param3", "retry"};
            String[] currentParams = paramList.get(position).split(" | ");

            if(currentParams[0].equals("no value")) currentParams[0] = "";
            if(currentParams[2].equals("no value")) currentParams[2] = "";

            String params = JSONHandler.putParamTogether(fields, "Message", "Send Message",
                    currentParams[0], currentParams[2], currentParams[4],
                    currentParams[0], currentParams[2], currentParams[4], "yes");

            new ServerAsyncRequester(ActionListActivity.this).execute(SITE, "U", params);
        }
        displaySnackBar();
    }

    private void displaySnackBar()
    {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, "Your action applied successfully", Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.info))
                .show();
//            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Send Message has been retried", Snackbar.LENGTH_LONG);
//            snackbar.show();
    }

}
