package com.gms.dao;

import java.util.List;

import com.gms.model.MotionSensor;

public interface MotionSensorDAO {

	public void addMotionSensor(MotionSensor sensor);

	public List<MotionSensor> getAllMotionSensors();

	public void deleteMotionSensor(Integer sensorId);

	public MotionSensor updateMotionSensor(MotionSensor sensor);

	public MotionSensor getMotionSensor(int sensorid);
	
	public List<MotionSensor> getMotionSensor(String sensorKey, String sensorValue);
	
	public MotionSensor getMotionSensorWithParams(String sensorKey, String sensorValue, int timestamp);
	
	public MotionSensor getMotionSensorLatestValue();
}
