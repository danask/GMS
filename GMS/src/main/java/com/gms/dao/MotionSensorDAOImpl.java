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

import com.gms.model.MotionSensor;
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
public class MotionSensorDAOImpl implements MotionSensorDAO {

	@Autowired
	private SessionFactory sessionFactory;

	public void addMotionSensor(MotionSensor sensor) 
	{
		sessionFactory.getCurrentSession().saveOrUpdate(sensor);
	}

	@SuppressWarnings("unchecked")
	public List<MotionSensor> getAllMotionSensors() 
	{
//		return sessionFactory.getCurrentSession().createQuery("from MotionSensor")
//				.list();
		List<MotionSensor> sensors = new ArrayList<MotionSensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from MotionSensor "
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
	public void deleteMotionSensor(Integer sensorId) 
	{
		MotionSensor sensor = (MotionSensor) sessionFactory.getCurrentSession().
											load(MotionSensor.class, sensorId);
		if (sensor != null) {
			this.sessionFactory.getCurrentSession().delete(sensor);
		}
	}

	public MotionSensor getMotionSensor(int sensorid) {
		return (MotionSensor) sessionFactory.getCurrentSession().get(
				MotionSensor.class, sensorid);
	}

	@Override
	public MotionSensor updateMotionSensor(MotionSensor sensor) {
		sessionFactory.getCurrentSession().update(sensor);
		return sensor;
	}

	public List<MotionSensor> getMotionSensor(String sensorKey, String sensorValue) 
	{
		List<MotionSensor> sensors = new ArrayList<MotionSensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from MotionSensor "
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

	public MotionSensor getMotionSensorWithParams(String sensorKey, String sensorValue, int timestamp)
	{
		List<MotionSensor> sensors = new ArrayList<MotionSensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from MotionSensor where sensorKey=? or sensorValue=? or timestamp=?")
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
	
	public MotionSensor getMotionSensorLatestValue()
	{
		List<MotionSensor> sensors = new ArrayList<MotionSensor>();
		
		sensors = sessionFactory.getCurrentSession()
			.createQuery("from MotionSensor order by timestamp desc")
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