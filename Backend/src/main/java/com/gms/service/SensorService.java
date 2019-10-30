package com.gms.service;

import java.util.List;

import com.gms.model.Sensor;

public interface SensorService {
	
	public void addSensor(Sensor sensor);

	public List<Sensor> getAllSensors();

	public void deleteSensor(Integer sensorId);

	public Sensor getSensor(int sensorid);
	
	public Sensor updateSensor(Sensor sensor);
	
	public List<Sensor> getSensor(String sensorTitle, String sensorArtist);
	
	public Sensor getSensorWithParams(String sensorTitle, String sensorArtist, int year);
	
	public Sensor getSensorLatestValue();
}
