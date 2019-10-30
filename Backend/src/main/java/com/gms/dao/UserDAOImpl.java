package com.gms.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import com.gms.model.Sensor;
import com.gms.model.User;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void addUser(User user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);

	}

	@SuppressWarnings("unchecked")
	public List<User> getAllUsers() {

		return sessionFactory.getCurrentSession().createQuery("from User")
				.list();
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = (User) sessionFactory.getCurrentSession().load(
				User.class, userId);
		if (null != user) {
			this.sessionFactory.getCurrentSession().delete(user);
		}

	}

	public User getUser(int userid) {
		return (User) sessionFactory.getCurrentSession().get(
				User.class, userid);
	}

	public User getUserByEmailPwd(String email, String pwd) {
		
		List<User> users = new ArrayList<User>();
		
		users = sessionFactory.getCurrentSession()
			.createQuery("from User where email=? and password=?")
			.setParameter(0, email)
			.setParameter(1, pwd)
			.list();

		if (users.size() > 0) 
		{
			return users.get(0);
		} 
		else 
		{
			return null;
		}
	}
	
	@Override
	public User updateUser(User user) {
		sessionFactory.getCurrentSession().update(user);
		return user;
	}

	public void fetchSensorDataFromFirebase()
	{
		File file = null;
		
		try {
			file = ResourceUtils.getFile("classpath:gms-rasp.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(file.getPath());
		
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
							
				System.out.println(snapshot.getKey() + ", " + snapshot.getValue());
				String timestamp = snapshot.getValue(String.class);
				System.out.println(timestamp);
				
				for (DataSnapshot temp: snapshot.getChildren()) {
		              System.out.println(temp);
		              
		        }
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
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
  				for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
  					System.out.println(snapshot.getKey() + ", " + snapshot.getValue());
  					
		        }
              }

              @Override
              public void onCancelled(DatabaseError error) {
                  // Failed to read value
              }
          });
	}
}