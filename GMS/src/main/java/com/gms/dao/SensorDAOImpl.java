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
public class SensorDAOImpl implements SensorDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void addSensor(Sensor sensor) 
	{
		sessionFactory.getCurrentSession().saveOrUpdate(sensor);
	}

	@SuppressWarnings("unchecked")
	public List<Sensor> getAllSensors() 
	{
//		return sessionFactory.getCurrentSession().createQuery("from Sensor")
//				.list();
		List<Sensor> sensors = new ArrayList<Sensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from Sensor "
					+ "order by timestamp desc")
			.list();

		if (sensors.size() > 0) 
		{
			return sensors;
		} 
		else 
		{
			return null;
		}		
	}

	@Override
	public void deleteSensor(Integer sensorId) 
	{
		Sensor sensor = (Sensor) sessionFactory.getCurrentSession().
											load(Sensor.class, sensorId);
		if (sensor != null) {
			this.sessionFactory.getCurrentSession().delete(sensor);
		}
	}

	public Sensor getSensor(int sensorid) {
		return (Sensor) sessionFactory.getCurrentSession().get(
				Sensor.class, sensorid);
	}

	@Override
	public Sensor updateSensor(Sensor sensor) {
		sessionFactory.getCurrentSession().update(sensor);
		return sensor;
	}

	public List<Sensor> getSensor(String sensorKey, String sensorValue) 
	{
		List<Sensor> sensors = new ArrayList<Sensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from Sensor "
					+ "where sensorKey=? or sensorValue=?"
					+ "order by timestamp desc")
			.setParameter(0, sensorKey)
			.setParameter(1, sensorValue)
			.list();

		if (sensors.size() > 0) 
		{
			return sensors;
		} 
		else 
		{
			return null;
		}
	}

	public Sensor getSensorWithParams(String sensorKey, String sensorValue, int timestamp)
	{
		List<Sensor> sensors = new ArrayList<Sensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from Sensor where sensorKey=? or sensorValue=? or timestamp=?")
			.setParameter(0, sensorKey)
			.setParameter(1, sensorValue)
			.setParameter(2, timestamp)
			.list();

		if (sensors.size() > 0) 
		{
			return sensors.get(0);
		} 
		else 
		{
			return null;
		}
	}	
	
	public Sensor getSensorLatestValue()
	{
		List<Sensor> sensors = new ArrayList<Sensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from Sensor order by timestamp desc")
//			.setFirstResult(0)
//			.setMaxResults(1)
			.list();
		
		if (sensors.size() > 0) 
		{
			return sensors.get(0);
		} 
		else 
		{
			return null;
		}
	}		
	
}