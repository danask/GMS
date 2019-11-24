package com.gms.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static com.gms.controller.OnEventListener.SITE;
import static java.util.Arrays.asList;

public class NotificationService extends Service {

    private NotificationManager notifier = null;
    private static final int CHAT_NOTIFY = 0x1001;
    private static final String NOTIFICATION_CHANNEL_ID = "gms_notification_channel";
    private static final String DEBUG_TAG = "NotificationService";
    private Socket socket;

    public NotificationService() {


    }

    // init
    @Override
    public void onCreate()
    {
        super.onCreate();

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUser = sharedPref.getString("currentUser", "-");

        //        location = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);

        // to ask this to OS (system)
        notifier = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Log.w("NOTI", "Noti-------------------------------------------");

//        Toast.makeText(getApplicationContext(), "NOTI start with "+ currentUser, Toast.LENGTH_LONG).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  // oreo
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "GMS Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notifier.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }


    // UnbindService, newer: onStartCommand, cf. onStart: old
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.w(DEBUG_TAG, "onStartCommand called.");

        if(flags !=0)
        {
            Log.w(DEBUG_TAG, "Redelivered or retrying service start: " + flags);
        }

        final Intent tempIntent = intent;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final String currentUser = sharedPref.getString("currentUser", "-");

        //connect you socket client to the server
//        try {
//            socket = IO.socket(SITE);
//            socket.connect();
//            socket.emit("init", currentUser); //partner);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//
//        }

//        socket.on("rmsg", new Emitter.Listener() {
//            @Override
//            public void call(final Object... args) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject data = (JSONObject) args[0];
//                        try {
//                            //extract data from fired event
//
//                            String partener = data.getString("tag");  //"senderNickname");
//                            String message = data.getString("m");
//
//
//                            Log.d("[=====CHAT=====]", "REC: "+ partener + ", "+ message);
//                            doServiceStart(tempIntent, 1, "Test");
//
////                                            if(!partener.equalsIgnoreCase(currentUser))
////                                            {
////                                                ChatMessage m = new ChatMessage(partener, message);
////                                                MessageList.add(m);
////                                                chatBoxAdapter = new ChatBoxAdapter(MessageList);
////                                                chatBoxAdapter.notifyDataSetChanged();
////                                                myRecylerView.setAdapter(chatBoxAdapter);
////                                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                });
//            }
//        });

        new Thread(new Runnable() {
            @Override
            public void run() {

//                while (true)
                {
                    try {
                        Thread.sleep(10000);

                        MongoClientURI uri = new MongoClientURI(
                                "mongodb+srv://Dan:admin1010@cluster0-8af06.mongodb.net/test?retryWrites=true&w=majority");

                        MongoClient mongoClient = new MongoClient(uri);
                        MongoDatabase db = mongoClient.getDatabase("gms_data");
//                        db.getCollection("status").watch();
                        MongoCollection<Document> collection = db.getCollection("sensors");

                        Block<ChangeStreamDocument<Document>> printBlock = new Block<ChangeStreamDocument<Document>>() {
                            public void apply(final ChangeStreamDocument<Document> changeStreamDocument) {

                                System.out.println(" MyService:::"+changeStreamDocument.getFullDocument());
                            }
                        };

                        // collection.watch - Establishes a Change Stream on a collection.This will identify any changes happening to the  collection.
                        collection.watch(asList(Aggregates.match(Filters.in("operationType", asList("insert", "update", "replace", "delete")))))
                                .fullDocument(FullDocument.UPDATE_LOOKUP).forEach(printBlock);

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

//                    if(isRunning){
//                        Log.i(TAG, "Service running");
//                    }
                }

                //Stop service once it finishes its task (in 5sec)
//                stopSelf(); // by itself
            }
        }).start();

//        return Service.START_STICKY;
        return Service.START_REDELIVER_INTENT;
    }


    // notification -> intent
    public void doServiceStart(Intent intent, int id, String name)
    {


        Intent toLaunch = new Intent(getApplicationContext(), MainActivity.class);

        // for foreign app with your permission
        PendingIntent intentBack = PendingIntent.getActivity(
                getApplicationContext(), 0, toLaunch, 0);

        // Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),NOTIFICATION_CHANNEL_ID);

        // currently support for local user
        builder.setTicker("Chat Alarm");
        builder.setSmallIcon(android.R.drawable.stat_notify_more);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Chat Alarm");
        builder.setContentText(name + " want to talk with you");

        builder.setContentIntent(intentBack);
        builder.setAutoCancel(true);

        Notification notify = builder.build();
        notifier.notify(CHAT_NOTIFY, notify);

    }

}
