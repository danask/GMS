package com.gms.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements OnEventListener<String> {

    Integer[] Selections = {
            R.drawable.img_console2_active,
            R.drawable.img_checklist,
            R.drawable.img_chat_active,
            R.drawable.img_project_active
    };

    ImageView pic;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle(Html.fromHtml("<font color='#ffc107'>"+"GMS Mobile Controller"+"</font>"));

        // part1
        final DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(this);

        new ServerAsyncRequester(MainActivity.this).execute(SITE, "F", "");
        new ServerAsyncRequester(MainActivity.this).execute(SITE, "R", "");  // Status
        new ServerAsyncRequester(MainActivity.this).execute(SITE, "O", "");  // Status

        try {
            socket = IO.socket(SITE);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on("alarmStatus", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("[alarmStatus] ", args[0].toString());

                        String[] columns = {"status"};
                        JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();

                        String returnStr = lCDDataExtractor.extract(MainActivity.this, args[0].toString(), columns);
                        String[] object = returnStr.split("-");

                        TextView textViewAlarmStatus = (TextView)findViewById(R.id.textViewAlarmStatus);
                        TextView textViewAlarm = (TextView)findViewById(R.id.textViewAlarm);
                        textViewAlarmStatus.setText(object[0]);

//                        View parentLayout = findViewById(android.R.id.content);
//                        Snackbar.make(parentLayout, "[Alarm] " + object[0], Snackbar.LENGTH_LONG)
//                                .setAction("CLOSE", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                    }
//                                })
//                                .setActionTextColor(getResources().getColor(R.color.info))
//                                .show();
                        if(object[0].equals("on"))
                        {
                            textViewAlarm.setTextColor(Color.parseColor("#D81B60"));
                            textViewAlarmStatus.setTextColor(Color.parseColor("#D81B60"));
                        }
                        else{
                            textViewAlarm.setTextColor(Color.BLACK);
                            textViewAlarmStatus.setTextColor(Color.BLACK);
                        }

                    }
                });
            }
        });

        socket.on("motorStatus", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("[motorStatus] ", args[0].toString());

                        String[] columns = {"status"};
                        JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();

                        String returnStr = lCDDataExtractor.extract(MainActivity.this, args[0].toString(), columns);
                        String[] object = returnStr.split("-");

                        TextView textViewWateringStatus = (TextView)findViewById(R.id.textViewWateringStatus);
                        TextView textViewWatering = (TextView)findViewById(R.id.textViewWatering);
                        textViewWateringStatus.setText(object[0]);

                        if(object[0].equals("on"))
                        {
                            textViewWatering.setTextColor(Color.parseColor("#337AB7"));
                            textViewWateringStatus.setTextColor(Color.parseColor("#337AB7"));
                        }
                        else{
                            textViewWatering.setTextColor(Color.BLACK);
                            textViewWateringStatus.setTextColor(Color.BLACK);
                        }

//                        View parentLayout = findViewById(android.R.id.content);
//                        Snackbar.make(parentLayout, "[Watering] " +args[0].toString(), Snackbar.LENGTH_LONG)
//                                .setAction("CLOSE", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//
//                                    }
//                                })
//                                .setActionTextColor(getResources().getColor(R.color.info))
//                                .show();
                    }
                });
            }
        });

        socket.on("rmsg", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            String partener = data.getString("tag");  //"senderNickname");
                            String message = data.getString("m");

                            Log.d("[=====CHAT=====]", "REC: "+ partener + ", "+ message);
//                            databaseHelper.addChatLog(partener, "", message, "", "HelpDesk", "","");

                            TextView textViewChatStatus = (TextView)findViewById(R.id.textViewChatStatus);
                            TextView textViewChat = (TextView)findViewById(R.id.textViewChat);
                            textViewChatStatus.setText("chatting");

                            textViewChat.setTextColor(Color.parseColor("#D81B60"));
                            textViewChatStatus.setTextColor(Color.parseColor("#D81B60"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        // part2
        GridView gridView = (GridView)findViewById(R.id.gridViewMenu);

        gridView.setAdapter(new ImageAdapter(this)); // link between gridView and ctx

        // just like listView, support this
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) displaySelectedScreen(0);
                if(position == 1) displaySelectedScreen(1);
                if(position == 2) displaySelectedScreen(2);
                if(position == 3) displaySelectedScreen(3);
            }
        });
    }

    private void displaySelectedScreen(int id)
    {
        switch (id)
        {
            case 0:
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                break;
            case 1:
                startActivity(new Intent(getApplicationContext(), ActionListActivity.class));
                break;
            case 2:
                startActivity(new Intent(getApplicationContext(), ChatBoxActivity.class));
                break;
            case 3:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(String result) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUserName = sharedPref.getString("currentUserName", "Dan");
        String currentUserNameEmail = sharedPref.getString("currentUser", "dan@dc.ca");

        TextView textViewCurrentUser = (TextView)findViewById(R.id.textViewCurrentUser);
        TextView textViewCurrentUserEmail = (TextView)findViewById(R.id.textViewCurrentUserEmail);
        TextView textViewHumidity = (TextView)findViewById(R.id.textViewHumidity);
        TextView textViewTemperature = (TextView)findViewById(R.id.textViewTemperature);
//        TextView textViewWeather = (TextView)findViewById(R.id.textViewWeather);
        ImageView imageViewWeather = (ImageView)findViewById(R.id.imageViewWeather);
        TextView textViewAlarmStatus = (TextView)findViewById(R.id.textViewAlarmStatus);
        TextView textViewWateringStatus = (TextView)findViewById(R.id.textViewWateringStatus);
        TextView textViewAlarm = (TextView)findViewById(R.id.textViewAlarm);
        TextView textViewWatering = (TextView)findViewById(R.id.textViewWatering);

        textViewCurrentUser.setText(currentUserName);
        textViewCurrentUserEmail.setText(currentUserNameEmail);

        if(result.equals(null) || result.equals("[]\n"))
        {
            Toast.makeText(getApplicationContext(),"Fail to get data", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String type = result.substring(0, 1);

            if (type.equals("F")) {
                String[] columns = {"weather", "main"};
                result = result.substring(1);

                JSONDataExtractor jsonDataExtractor = new JSONDataExtractor();
                String[] returnStr = jsonDataExtractor.extract(this, result, columns).split("-");

//                textViewWeather.setText(returnStr[0]);
                if(returnStr[0].equals("Clear"))
                    imageViewWeather.setImageResource(R.drawable.sunny);
                else if(returnStr[0].equals("Rain"))
                    imageViewWeather.setImageResource(R.drawable.umbrellas);
                else if(returnStr[0].equals("Clouds"))
                    imageViewWeather.setImageResource(R.drawable.cloud);
                else if(returnStr[0].equals("Snow"))
                    imageViewWeather.setImageResource(R.drawable.snowman);

                textViewTemperature.setText(returnStr[1] + "C");
                textViewHumidity.setText(returnStr[2] + "%");
            }

            if (type.equals("R")) {
                String[] columns = {"status"};
                result = result.substring(1);

                JSONDataExtractor jsonDataExtractor = new JSONDataExtractor();
                String[] returnStr = jsonDataExtractor.extract(this, result, columns).split("-");

                textViewAlarmStatus.setText(returnStr[0]);

                if(returnStr[0].equals("on"))
                {
                    textViewAlarm.setTextColor(Color.parseColor("#D81B60"));
                    textViewAlarmStatus.setTextColor(Color.parseColor("#D81B60"));
                }
            }

            // watering
            if (type.equals("O")) {
                String[] columns = {"status"};
                result = result.substring(1);

                JSONDataExtractor jsonDataExtractor = new JSONDataExtractor();
                String[] returnStr = jsonDataExtractor.extract(this, result, columns).split("-");

                textViewWateringStatus.setText(returnStr[0]);

                if(returnStr[0].equals("on"))
                {
                    textViewWatering.setTextColor(Color.parseColor("#337AB7"));
                    textViewWateringStatus.setTextColor(Color.parseColor("#337AB7"));
                }
            }
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    public class ImageAdapter extends BaseAdapter {

        private Context ctx; // each image

        // constructor
        public ImageAdapter(Context c) {
            ctx = c;
        }

        @Override
        public int getCount(){
            return Selections.length;
        }

        @Override
        public Object getItem(int pos){
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            pic = new ImageView(ctx);
            pic.setImageResource(Selections[position]);
            pic.setScaleType(ImageView.ScaleType.FIT_XY);
            pic.setLayoutParams(new GridView.LayoutParams(250,250));
            pic.setPadding(10,20,10,20);

            return pic;
        }
    }
}


