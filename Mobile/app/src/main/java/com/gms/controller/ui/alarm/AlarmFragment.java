package com.gms.controller.ui.alarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class AlarmFragment extends Fragment implements OnEventListener<String> {

    private AlarmViewModel alarmViewModel;
    private Socket socket;

    TextView textViewFirstLine;
    TextView textViewSecondLine;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_alarm, container, false);

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//
//        alarmViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        textViewFirstLine = root.findViewById(R.id.textViewFirstLine);
        textViewSecondLine = root.findViewById(R.id.textViewSecondLine);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Square-Dot-Matrix.ttf");
        textViewFirstLine.setTypeface(typeface);
        textViewSecondLine.setTypeface(typeface);

        // 1st time
        new ServerAsyncRequester(AlarmFragment.this).execute(SITE, "S", "");  // Status

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

                        String[] columns = {"type", "firstLine", "secondLine"};
                        JSONDataExtractor lCDDataExtractor = new JSONDataExtractor();

                        Log.d("[Socket] ", args[0].toString());

                        String returnStr = lCDDataExtractor.extract(getActivity(), args[0].toString(), columns);
                        String[] object = returnStr.split("-");

                        if (object != null) {
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

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();//.child("LastImage/lastImage.jpg");





//        // Create a storage reference from our app
//        StorageReference storageRef = storage.getReference();
//
//        // Or Create a reference to a file from a Google Cloud Storage URI
//        StorageReference gsReference =
//                storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");


        /*In this case we'll use this kind of reference*/
        //Download file in Memory
        StorageReference imgRef = storageReference.child("LastImage/lastImage.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            ImageView imageViewCapture = root.findViewById(R.id.imageViewCapture);
            @Override
            public void onSuccess(byte[] bytes) {
              // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                DisplayMetrics dm = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(dm);

//                imageViewCapture.setMinimumHeight(dm.heightPixels);
//                imageViewCapture.setMinimumWidth(dm.widthPixels);
                imageViewCapture.setImageBitmap(bm);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        // for button event
        Button buttonDismissAlarm = root.findViewById(R.id.buttonDismissAlarm);

        buttonDismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText editTextPasscode = root.findViewById(R.id.editTextPasscode);
                String fields[] = {"category", "type", "passcode", "param1", "retry"};

                String params = JSONHandler.putParamTogether(fields, "Alarm", "Dismiss Alarm",
                                            editTextPasscode.getText().toString(), editTextPasscode.getText().toString(), "no");

                new ServerAsyncRequester(AlarmFragment.this).execute(SITE, "A", params);  // Motor control
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
        if (result.equals(null) || result.equals("[]\n")) {
            Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String type = result.substring(0, 1);
            String[] columns = {"type", "firstLine", "secondLine"};
            result = result.substring(1);

//            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();

            if (type.equalsIgnoreCase("W"))
                columns = new String[]{"type", "liters"};

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
        }
    }

    @Override
    public void onFailure(Exception e) {

    }
}