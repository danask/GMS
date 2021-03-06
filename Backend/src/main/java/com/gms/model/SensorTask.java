package com.gms.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import com.gms.service.SensorService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SensorTask 
{
	@Autowired
	private SensorService sensorService;
	
	public void getSensorInfoFromFirebase()
	{
		System.out.println("===========sensor info==========");
		// service start
		File file = null;
		
		try {
			file = ResourceUtils.getFile("classpath:gms-rasp.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		System.out.println(file.getPath());
		
		FileInputStream serviceAccount = null;
		
		try {
			serviceAccount = new FileInputStream(file.getPath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FirebaseOptions options = null;
		
		try {
			options = new FirebaseOptions.Builder()
			  .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			  .setDatabaseUrl("https://gms-rasp.firebaseio.com")
			  .build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(FirebaseApp.getApps().isEmpty()) {
		    FirebaseApp.initializeApp(options);
		}
		
		
		final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
		final Sensor sensors = null;
		DatabaseReference ref = firebaseDatabase.getReference("timestamp");
		
        // app_title change listener
		ref.addValueEventListener(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
							
//				System.out.println(snapshot.getKey() + ", " + snapshot.getValue());
				String timestamp = snapshot.getValue(String.class);
//				System.out.println("1: " + timestamp);
				
//				Sensor sensor = new Sensor(0, 0, 111, timestamp);
//				sensorService.addSensor(sensor);
				
//				for (DataSnapshot temp: snapshot.getChildren()) {
//		              System.out.println(temp);
//		              
//		        }
			}
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub
				 System.out.println("The read failed: ");
			}

		 });
		
        // app_title change listener
        firebaseDatabase.getReference("DHT").
                addValueEventListener(new ValueEventListener() {
              @SuppressWarnings("null")
			@Override
              public void onDataChange(DataSnapshot dataSnapshot) {
            	
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	HashMap<String, String> m = new HashMap<>();
            	Sensor sensor = new Sensor("0", "0", 1, "test");
            	
  				for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
  					String key = snapshot.getKey();
  					String val = snapshot.getValue().toString();
  					System.out.println("2nd: " + key + ", " + val);
//  					m.put(key, val);
  					
  					if(key.equalsIgnoreCase("Temperature"))
  						sensor.setSensorTemp(val);
  					
  					if(key.equalsIgnoreCase("Humidity"))
  						sensor.setSensorHumid(val);
		        }
  				
//  			    for (String i : m.keySet()) 
//  			    {
//  			    	if(i.equals("Temperature"))
//  			    		sensor.setTemperature(Float.parseFloat(m.get(i)));
//  			    	
//  			    	if(i.equals("Humidity"))
//  			    		sensor.setHumidity(Float.parseFloat(m.get(i)));
//  			    	
//  			    	System.out.println("key: " + i + " value: " + m.get(i));
//  			    }
  			    
  				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
  				String dateTime = sdf.format(timestamp);
		    	sensor.setTimestamp(timestamp.getTime());
		    	sensor.setDateTime(dateTime);
		    	sensor.setDescription("demo");
		    	
		    	
		    	// to store only one time; to avoid the conflict with the previous one
		    	if(!sensorService.getSensorLatestValue().getSensorTemp()
	    			.equals(sensor.getSensorTemp()) ||
	    			!sensorService.getSensorLatestValue().getSensorHumid()
	    			.equals(sensor.getSensorHumid()))
		    	{
			    	System.out.println(sensor);
			    	sensorService.addSensor(sensor);
		    	}
              }

              @Override
              public void onCancelled(DatabaseError error) {
                  // Failed to read value
              }
          });
	}
	
}
