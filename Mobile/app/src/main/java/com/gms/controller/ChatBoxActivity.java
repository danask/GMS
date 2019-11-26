package com.gms.controller;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ChatBoxActivity extends AppCompatActivity implements OnEventListener<String>{
    public RecyclerView myRecylerView ;
    public List<ChatMessage> MessageList ;
    public ChatBoxAdapter chatBoxAdapter;
    public EditText messagetxt ;
    public Button send ;
    private Socket socket;

    public String partner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(Html.fromHtml("<font color='#ffc107'>"+"Help Desk"+"</font>"));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        setHasOptionsMenu(true);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String currentUserFromSP = sharedPref.getString("currentUserName", "Dan");
        final String currentUser = currentUserFromSP + " (Mobile)";
        final String targetUser = ADMIN; // always

        messagetxt = (EditText) findViewById(R.id.message);  // from input box
        send = (Button)findViewById(R.id.send);


        // get the nickame of the user

        if(getIntent().getExtras() != null)
            partner= (String)getIntent().getExtras().getString("NICKNAME");

        //connect you socket client to the server
        try {
            socket = IO.socket(SITE);
            socket.connect();
            socket.emit("init", currentUser); //partner);
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
       //setting up recyler
        MessageList = new ArrayList<>();
        myRecylerView = (RecyclerView) findViewById(R.id.messagelist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        myRecylerView.setLayoutManager(mLayoutManager);
        myRecylerView.setItemAnimator(new DefaultItemAnimator());

        final DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(this);



        Cursor c = databaseHelper.getChatLog();

        if(c.getCount() > 0)
        {
            while (c.moveToNext())
            {
                String aId = "";
                String bId = "";
                String aMsg = "";
                String bMsg = "";

                aId = c.getString(c.getColumnIndex("A_Id"));
                bId = c.getString(c.getColumnIndex("B_Id"));
                aMsg = c.getString(c.getColumnIndex("A_Message"));
                bMsg = c.getString(c.getColumnIndex("B_Message"));

                if(aId != null && !aId.equals(""))
                {
                    processMessage("receive", aId, aMsg);
                }

                if(bId != null && !bId.equals(""))
                {
                    processMessage("send", bId, bMsg);
                }
            }

        }


        // message send action
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!messagetxt.getText().toString().isEmpty())
                {
                    String msgToSend = messagetxt.getText().toString();
                    socket.emit("rmsg", currentUser, msgToSend, targetUser); // fixed

                    databaseHelper.addChatLog("", currentUser, "", msgToSend, "HelpDesk", "","");

                    processMessage("send", currentUser, msgToSend);
                    messagetxt.setText(" ");

                    hideKeyboard(ChatBoxActivity.this);
                }
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
                            databaseHelper.addChatLog(partener, "", message, "", "HelpDesk", "","");

                            if(!partener.equalsIgnoreCase(currentUser))
                            {
                                processMessage("receive", partener, message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void processMessage(String type, String user, String message)
    {
        ChatMessage m;

        if(type.equals("send"))
            m = new ChatMessage(user, "S" + message);
        else
            m = new ChatMessage(user, message);

        MessageList.add(m);
        chatBoxAdapter = new ChatBoxAdapter(MessageList);
        chatBoxAdapter.notifyDataSetChanged();
        myRecylerView.setAdapter(chatBoxAdapter);

        // scrollToPosition
        myRecylerView.smoothScrollToPosition(chatBoxAdapter.getItemCount()-1);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_socket, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
  }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onFailure(Exception e) {

    }

}
