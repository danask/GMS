package com.gms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gms.dao.SensorDAO;
import com.gms.model.Sensor;

@Service("SensorService")
@Transactional
public class SensorServiceImpl implements SensorService {

	@Autowired
	private SensorDAO sensorDAO;

	@Override
	@Transactional
	public void addSensor(Sensor sensor) {
		sensorDAO.addSensor(sensor);
	}

	@Override
	@Transactional
	public List<Sensor> getAllSensors() {
		return sensorDAO.getAllSensors();
	}

	@Override
	@Transactional
	public void deleteSensor(Integer sensorId) {
		sensorDAO.deleteSensor(sensorId);
	}

	public Sensor getSensor(int id) {
		return sensorDAO.getSensor(id);
	}
	
	public Sensor updateSensor(Sensor sensor) {
		// TODO Auto-generated method stub
		return sensorDAO.updateSensor(sensor);
	}

	public void setSensorDAO(SensorDAO sensorDAO) {
		this.sensorDAO = sensorDAO;
	}

	public List<Sensor> getSensor(String sensorKey, String sensorValue) {
		return sensorDAO.getSensor(sensorKey, sensorValue);
	}
	
	public Sensor getSensorWithParams(String sensorKey, String sensorValue, int timestamp) {
		return sensorDAO.getSensorWithParams(sensorKey, sensorValue, timestamp);
	}

	public Sensor getSensorLatestValue()
	{
		return sensorDAO.getSensorLatestValue();
	}
}
