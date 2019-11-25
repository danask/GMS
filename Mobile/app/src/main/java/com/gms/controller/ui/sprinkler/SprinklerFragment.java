package com.gms.controller.ui.sprinkler;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.gms.controller.JSONDataExtractor;
import com.gms.controller.JSONHandler;
import com.gms.controller.ServerAsyncRequester;
import com.gms.controller.OnEventListener;
import com.gms.controller.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class SprinklerFragment extends Fragment implements OnEventListener<String> {

    private SprinklerViewModel sprinklerViewModel;
    private Socket socket;

    TextView textViewFirstLine;
    TextView textViewSecondLine;
    TextView textViewLiters;
    TextView textViewTime;
    TextView textViewCycles;
    EditText editTextCycle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sprinklerViewModel =
                ViewModelProviders.of(this).get(SprinklerViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_sprinkler, container, false);

//        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActivity().getActionBar().setHomeButtonEnabled(true);

//        final TextView textView = root.findViewById(R.id.textSprinklerDesc);
//
//        sprinklerViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        textViewFirstLine = root.findViewById(R.id.textViewFirstLine);
        textViewSecondLine = root.findViewById(R.id.textViewSecondLine);
        textViewLiters = root.findViewById(R.id.textViewLiters);
        textViewTime = root.findViewById(R.id.textViewTime);
        textViewCycles = root.findViewById(R.id.textViewCycles);
        editTextCycle = root.findViewById(R.id.editTextCycle);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Square-Dot-Matrix.ttf");
        textViewFirstLine.setTypeface(typeface);
        textViewSecondLine.setTypeface(typeface);

        // 1st time
        new ServerAsyncRequester(SprinklerFragment.this).execute(SITE, "S", "");  // Status
        new ServerAsyncRequester(SprinklerFragment.this).execute(SITE, "W", "");  // Status

        // 2nd time for emitting
        try {
            socket = IO.socket(SITE);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.on("lcdMessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if(getActivity() != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String[] columns = {"type","firstLine", "secondLine"};
                            JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();

                            Log.d("[Socket] ", args[0].toString());

                            String returnStr = lCDDataExtractor.extract(getActivity(), args[0].toString(), columns);
                            String[] object = returnStr.split("-");

                            if(object != null)
                            {
                                textViewFirstLine.setText(object[1]);
                                textViewSecondLine.setText(object[2]);

    //                            double liter = Double.parseDouble(object[3]);
    //                            textViewLiters.setText("Amount of water (liters): " + object[3]);
    //                            textViewTime.setText("Watering time (min): " + String.valueOf(((int)liter)*6));
    //                            textViewCycles.setText("Splinker cycles:" + String.valueOf(((int)liter)*6*60));
                            }
                        }
                    });
            }
        });


        // for button event
        Button buttonStartWatering = root.findViewById(R.id.buttonSendMessage);
        Button buttonStopWatering = root.findViewById(R.id.buttonStopWatering);

        buttonStartWatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editTextCycle = root.findViewById(R.id.editTextCycle);
                final Spinner spinnerUnit= root.findViewById(R.id.spinnerDuration);

                String fields[] = {"category", "type", "cycle", "status", "param1", "param2", "retry"};

                String unit = "cycle";

                if(spinnerUnit != null)
                    spinnerUnit.getSelectedItem().toString();

                double number = Math.ceil(Double.parseDouble(editTextCycle.getText().toString()));

                if(unit.equalsIgnoreCase("Liters (liters)")) {
                    number = number * 60 * 6;
                    unit = "liter";
                }
                else if(unit.equalsIgnoreCase("Time (minutes)")) {
                    number = number * 6;
                    unit = "time";
                }

                String cycle = String.valueOf((int)Math.ceil(number));
                String params = JSONHandler.putParamTogether(fields, "Sprinkler", "Start Watering", cycle, "on", unit, cycle, "no");

                new ServerAsyncRequester(SprinklerFragment.this).execute(SITE, "M", params);  // Motor control
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Your action applied successfully", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.info ))
                        .show();
            }
        });

        buttonStopWatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fields[] = {"category", "type", "cycle", "status", "param1", "param2", "retry"};
                String params = JSONHandler.putParamTogether(fields, "Sprinkler", "Stop Watering", "0", "off", "cycle", "0", "no");

                new ServerAsyncRequester(SprinklerFragment.this).execute(SITE, "M", params);  // Motor control
//                Toast.makeText(getContext(), "Test", Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Your action applied successfully", Snackbar.LENGTH_LONG)
                        .setAction("CLOSE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.info ))
                        .show();
            }
        });

        return root;
    }

    @Override
    public void onSuccess(String result) {

        if(result.equals(null) || result.equals("[]\n"))
        {
            Toast.makeText(getContext(),"Fail to get data", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            String type = result.substring(0, 1);
            String[] columns = {"type","firstLine", "secondLine"};
            result = result.substring(1);

//            Toast.makeText(getContext(),result, Toast.LENGTH_SHORT).show();

            if(type.equalsIgnoreCase("W"))
                columns = new String[]{"type", "liters"};

            JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();
            String returnStr = lCDDataExtractor.extract(getActivity(), result, columns);
            String[] object = returnStr.split("-");

            if(object != null && type.equalsIgnoreCase("S"))
            {
                String firstLine = "";
                String secondLine = "";

                if(object[1] != null)
                    firstLine = object[1];

                if(object.length > 2 && object[2] != null)
                    secondLine = object[2];

                textViewFirstLine.setText(firstLine);
                textViewSecondLine.setText(secondLine);
            }

            if(object != null && type.equalsIgnoreCase("W")) {
                double liter = Double.parseDouble(object[1]);
                textViewLiters.setText("Amount of water (liters): " + object[1].substring(0,4));
                textViewTime.setText("Watering time (min): " + String.valueOf((liter) * 60).substring(0,6));
                textViewCycles.setText("Sprinkler cycles: " + String.valueOf((liter) * 6 * 60).substring(0,6));
            }

            if(type.equalsIgnoreCase("M"))
            {
                editTextCycle.setText("");
            }
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

}