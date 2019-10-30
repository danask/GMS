package com.gms.service;

import java.util.List;

import com.gms.model.MotionSensor;

public interface MotionSensorService {
	
	public void addMotionSensor(MotionSensor sensor);

	public List<MotionSensor> getAllMotionSensors();

	public void deleteMotionSensor(Integer sensorId);

	public MotionSensor getMotionSensor(int sensorid);
	
	public MotionSensor updateMotionSensor(MotionSensor sensor);
	
	public List<MotionSensor> getMotionSensor(String sensorTitle, String sensorArtist);
	
	public MotionSensor getMotionSensorWithParams(String sensorTitle, String sensorArtist, int year);
	
	public MotionSensor getMotionSensorLatestValue();
}
