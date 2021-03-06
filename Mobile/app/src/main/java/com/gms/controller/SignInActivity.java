package com.gms.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class SignInActivity extends AppCompatActivity  implements OnEventListener<String>{

    DatabaseHelper databaseHelper;
    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
// to quit command
        if(getIntent().getBooleanExtra("Exit", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

        Button buttonSignIn = (Button)findViewById(R.id.buttonSignIn);

        final EditText editTextEmailSignIn = (EditText)findViewById(R.id.editTextEmailSignIn);
        final EditText editTextPwdSignIn = (EditText)findViewById(R.id.editTextPwdSignIn);

        formattedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        databaseHelper = new DatabaseHelper(this);

        editTextEmailSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextEmailSignIn.getText()))
                {
                    editTextEmailSignIn.setError(getString(R.string.error_field_required));
                }
            }
        });

        editTextPwdSignIn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (TextUtils.isEmpty(editTextPwdSignIn.getText()))
                {
                    editTextPwdSignIn.setError(getString(R.string.error_field_required));
                }
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editTextEmailSignIn = findViewById(R.id.editTextEmailSignIn);
                final EditText editTextPwdSignIn = findViewById(R.id.editTextPwdSignIn);
                String fields[] = {"email", "password"};

                String params = JSONHandler.putParamTogether(fields,
                        editTextEmailSignIn.getText().toString(), editTextPwdSignIn.getText().toString());

                new ServerAsyncRequester(SignInActivity.this).execute(SITE, "L", params);  // Login
            }
        });

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
            String[] columns = {"name", "email"};
            result = result.substring(1);

            JSONDataExtractor jsonDataExtractor = new JSONDataExtractor();
            String returnStr = jsonDataExtractor.extract(this, result, columns);

            String[] object = returnStr.split("-");

            try {
                if (object != null && type.equalsIgnoreCase("L")) {

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("currentUser", object[1]);
                    editor.putString("currentUserName", object[0]);
                    editor.commit();

                    View parentLayout = findViewById(android.R.id.content);
                    Snackbar.make(parentLayout, object[0] + " signed in successfully", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.info))
                            .show();

                    databaseHelper.setCount(0);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
            catch (Exception e)
            {
                View parentLayout = findViewById(android.R.id.content);
                Snackbar.make(parentLayout, "Failed to login. Try again!", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.alert))
                        .show();
            }
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

    private class CustomProgressWheel extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressBarHolder.setAnimation(outAnimation);
            progressBarHolder.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
