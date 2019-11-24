package com.gms.controller.ui.urgentMessage;

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
import com.gms.controller.JSONHandler;
import com.gms.controller.ServerAsyncRequester;
import com.gms.controller.JSONDataExtractor;
import com.gms.controller.OnEventListener;
import com.gms.controller.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class UrgentMessageFragment extends Fragment implements OnEventListener<String> {

    private UrgentMessageViewModel urgentMessageViewModel;
    private Socket socket;

    TextView textViewFirstLine;
    TextView textViewSecondLine;
    EditText editTextFirstInput;
    EditText editTextSecondInput;
    Spinner spinnerDuration;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        urgentMessageViewModel =
                ViewModelProviders.of(this).get(UrgentMessageViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_urgent_message, container, false);
//        final TextView textView = root.findViewById(R.id.text_notifications);
//        urgentMessageViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        textViewFirstLine = root.findViewById(R.id.textViewFirstLine);
        textViewSecondLine = root.findViewById(R.id.textViewSecondLine);
        editTextFirstInput = root.findViewById(R.id.editTextFirstInput);
        editTextSecondInput = root.findViewById(R.id.editTextSecondInput);
        spinnerDuration = root.findViewById(R.id.spinnerDuration);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Square-Dot-Matrix.ttf");
        textViewFirstLine.setTypeface(typeface);
        textViewSecondLine.setTypeface(typeface);

        // 1st time
        new ServerAsyncRequester(UrgentMessageFragment.this).execute(SITE, "S", "");  // Status

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
        Button buttonSendMessage = root.findViewById(R.id.buttonSendMessage);

        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editTextFirstInput = root.findViewById(R.id.editTextFirstInput);
                final EditText editTextSecondInput = root.findViewById(R.id.editTextSecondInput);

                Spinner spinnerUnit= root.findViewById(R.id.spinnerDuration);

                String unit = spinnerUnit.getSelectedItem().toString().substring(0,1);

                String fields[] = {"category", "type", "firstLine", "secondLine", "duration", "param1", "param2", "param3", "retry"};

                String params = JSONHandler.putParamTogether(fields, "Message", "Send Message",
                                                    editTextFirstInput.getText().toString(), editTextSecondInput.getText().toString(), unit,
                                                    editTextFirstInput.getText().toString(), editTextSecondInput.getText().toString(), unit, "no");

                new ServerAsyncRequester(UrgentMessageFragment.this).execute(SITE, "U", params);  // Motor control
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
        else {
            String type = result.substring(0, 1);
            String[] columns = {"type", "firstLine", "secondLine"};
            result = result.substring(1);

//            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

            JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();
            String returnStr = lCDDataExtractor.extract(getActivity(), result, columns);
            String[] object = returnStr.split("-");

            if (object != null && type.equalsIgnoreCase("S")) {
                String firstLine = "";
                String secondLine = "";

                if(object[1] != null)
                    firstLine = object[1];

                if(object.length > 2 && object[2] != null)
                    secondLine = object[2];

                textViewFirstLine.setText(firstLine);
                textViewSecondLine.setText(secondLine);
            }

            if(type.equalsIgnoreCase("U"))
            {
                editTextFirstInput.setText("");
                editTextSecondInput.setText("");
            }
        }
    }

    @Override
    public void onFailure(Exception e) {

    }

}