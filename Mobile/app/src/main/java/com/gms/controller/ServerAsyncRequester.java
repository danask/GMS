package com.gms.controller;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


// parameters: doInBackground, onProgressUpdate, onPostExecute
public class ServerAsyncRequester extends AsyncTask<String, Integer, String> {

    // for this example, I want the onPostExecute to inform theCallBack and pass the result as String
    private OnEventListener<String> theCallBack;


    public ServerAsyncRequester(OnEventListener theCallBack) {
        this.theCallBack = theCallBack; // the implementation is controlled by the calling method
    }

    protected void onPreExecute(){
    }

    // return type must match the parameter data type of onPostExecute
    protected String doInBackground(String...data) {

        String site ="";

        // data[0] can represent the url
        if(data[1].equalsIgnoreCase("S"))
            site = data[0] + "/getStatus";
        else if(data[1].equalsIgnoreCase("W"))
            site = data[0] + "/getWater";
        else if(data[1].equalsIgnoreCase("M"))
            site = data[0] + "/updateMotor";
        else if(data[1].equalsIgnoreCase("U"))
            site = data[0] + "/sendMessage";
        else if(data[1].equalsIgnoreCase("A"))
            site = data[0] + "/dismissAlarm";
        else if(data[1].equalsIgnoreCase("H"))
            site = data[0] + "/getHistory";
        else if(data[1].equalsIgnoreCase("L"))
            site = data[0] + "/getUser";
        else if(data[1].equalsIgnoreCase("F")) // Forecast
            site = data[0] + "/getWeather";
        else if(data[1].equalsIgnoreCase("R")) // alaRm
            site = data[0] + "/getAlarmStatus";
        else if(data[1].equalsIgnoreCase("O")) // mOtor
            site = data[0] + "/getMotorStatus";

        // data[1] can represent the params
        String params = data[2];
        String result = getRemoteData(data[1], site, params);

        System.out.println("conn: "+ site + ", " + result);
        return result; // this would call onPostExecute
    }

    protected void onProgressUpdate(Integer... progress) {

    }

    protected void onPostExecute(String result) {
        Log.d("onPostExecute",result);

        // this would call the onSuccess method implemented in the calling class (e.g. SearchActivity)
        theCallBack.onSuccess(result);

    }

    // ==========================================================================

    private String getRemoteData(String type, String site, String params) {

        HttpURLConnection c = null;
        String dataFromServer = "";
        try {
            URL u = new URL(site);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("POST");
            c.setDoInput(true);            // receiving data from the web
            c.setDoOutput(true);            // application sending data to the web

            //passing the data to the server
            OutputStreamWriter writer = new OutputStreamWriter(c.getOutputStream()); // similar to writing to a text file
            writer.write(params);
            writer.flush();
            writer.close();

            Log.d(this.getClass().getName(),"Connecting");
            c.connect();
            int status = c.getResponseCode();
            Log.d("code",status + "");
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    dataFromServer = sb.toString();
                    break;
                case 404: return "Cant find";
            }

        } catch (Exception ex) {
            return ex.toString();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    //disconnect error
                }
            }
        }
        return type + dataFromServer;

    }
}
