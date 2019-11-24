package com.gms.controller;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class JSONDataExtractor {

    public String extract (Activity context, String data, String... columns) {

        StringBuilder returnValues = new StringBuilder();
        String returnValue = "";

        Log.d(this.getClass().getName(),data);
        try {

            if(data.substring(0, 2).equals("{\"")){

                JSONObject jsonObject = new JSONObject(data);

                // getting the columns
                for (int y = 0; y < columns.length; y++) {

                    if (columns[y].equals("weather")) {
                        JSONArray tempArray = jsonObject.getJSONArray((columns[y]));
                        JSONObject tempObject = tempArray.getJSONObject(0);
                        String weather = tempObject.getString("main");

                        returnValues.append(weather);
                        returnValues.append("-");
                    } else if (columns[y].equals("main")) {
                        JSONObject tempObject = jsonObject.getJSONObject((columns[y]));
                        String temperature = tempObject.getString("temp");
                        String humidity = tempObject.getString("humidity");
                        returnValues.append(temperature);
                        returnValues.append("-");
                        returnValues.append(humidity);
                        returnValues.append("-");
                    } else {
                        returnValues.append(jsonObject.getString(columns[y]));
                        returnValues.append("-");
                    }
                }

            }
            else{
                JSONArray jsonArray = new JSONArray(data);
                JSONObject jsonObject;

                int jsonArraySize = jsonArray.length();

                for (int x=0; x < jsonArraySize; x++)
                {
                    jsonObject = jsonArray.getJSONObject(x);

                    // getting the columns
                    for (int y=0; y < columns.length; y++) {

                        if(columns[y].equals("weather"))
                        {
                            JSONArray tempArray = jsonObject.getJSONArray((columns[y]));
                            JSONObject tempObject = tempArray.getJSONObject(0);
                            String weather = tempObject.getString("main");

                            returnValues.append(weather);
                            returnValues.append("-");
                        }
                        else if(columns[y].equals("main"))
                        {
                            JSONObject tempObject = jsonObject.getJSONObject((columns[y]));
                            String temperature = tempObject.getString("temp");
                            String humidity = tempObject.getString("humidity");
                            returnValues.append(temperature);
                            returnValues.append("-");
                            returnValues.append(humidity);
                            returnValues.append("-");
                        }
                        else {
                            returnValues.append(jsonObject.getString(columns[y]));
                            returnValues.append("-");
                        }
                    }

                    if(jsonArraySize > 1)
                        returnValues.append(",");
                }
            }
        }
        catch (Exception e) {
            Log.d(this.getClass().getName(), e.getMessage());
        }

        if(returnValues.toString() != "")
            returnValue = returnValues.toString().substring(0, returnValues.length()-1);

        return returnValue;
    }
}
