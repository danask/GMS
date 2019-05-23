package com.gms.dao;

import java.util.List;

import com.gms.model.Sensor;

public interface SensorDAO {

	public void addSensor(Sensor sensor);

	public List<Sensor> getAllSensors();

	public void deleteSensor(Integer sensorId);

	public Sensor updateSensor(Sensor sensor);

	public Sensor getSensor(int sensorid);
	
	public List<Sensor> getSensor(String sensorKey, String sensorValue);
	
	public Sensor getSensorWithParams(String sensorKey, String sensorValue, int timestamp);
	
	public Sensor getSensorLatestValue();
}
