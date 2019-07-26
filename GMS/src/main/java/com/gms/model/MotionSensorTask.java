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

import com.gms.service.CriteriaService;
import com.gms.service.MotionSensorService;
import com.gms.service.SensorService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MotionSensorTask 
{
	@Autowired
	private MotionSensorService motionSensorService;
	
	@Autowired
	private CriteriaService criteriaService;
	
	public final int EMAIL_INTERVAL = 120; // mins: 30 times -> 8min
	
	public void getSensorInfoFromFirebase()
	{
		System.out.println("===========motion sensor info==========");
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
        firebaseDatabase.getReference("PIR").
                addValueEventListener(new ValueEventListener() {
              @SuppressWarnings("null")
			@Override
              public void onDataChange(DataSnapshot dataSnapshot) {
            	
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	HashMap<String, String> m = new HashMap<>();
            	MotionSensor motionSensor = new MotionSensor(1, "test");
            	
  				for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
  					String key = snapshot.getKey();
  					String val = snapshot.getValue().toString();
  					System.out.println("2nd: " + key + ", " + val);
//  					m.put(key, val);
  					
  					if(key.equalsIgnoreCase("id"))
  						motionSensor.setPirId(val);
  					
  					if(key.equalsIgnoreCase("alert"))
  						motionSensor.setAlert(val);
  					
  					if(key.equalsIgnoreCase("detectTime"))
  						motionSensor.setDetectTime(val);
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
  				motionSensor.setTimestamp(timestamp.getTime());
  				motionSensor.setDateTime(dateTime);
  				motionSensor.setDescription("demo");
		    	
		    	// to store only one time; to avoid the conflict with the previous one
		    	if(!motionSensorService.getMotionSensorLatestValue().getDetectTime().
		    			equals(motionSensor.getDetectTime()))
		    	{
			    	System.out.println(motionSensor);
		    		motionSensorService.addMotionSensor(motionSensor);
		    	}
		    	
		    	
		    	// to send email 
		    	Criteria currentCriteria = criteriaService.getCriteria();
		    	
		    	if(!criteriaService.getEmailNotification().equals("off"))
		    	{
		    		if(criteriaService.getEmailNotification().equals("on"))
		    		{
		    			NotificationTask.sendEmail(); 
		    			currentCriteria.setEmailNotification("1");
		    		}
		    		else  // numbers
		    		{
		    			int tempNumber = Integer.parseInt(currentCriteria.getEmailNotification());
		    			
		    			// till boundary
		    			if(tempNumber == EMAIL_INTERVAL)  
		    				currentCriteria.setEmailNotification("on");
		    			else // increase number
		    				currentCriteria.setEmailNotification(String.valueOf((++tempNumber)));
		    		}
		    		
	    			criteriaService.updateCriteria(currentCriteria);
		    	}
		    	
              }

              @Override
              public void onCancelled(DatabaseError error) {
                  // Failed to read value
              }
          });
	}
	
}
