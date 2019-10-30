package com.gms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gms.dao.MotionSensorDAO;
import com.gms.model.MotionSensor;

@Service("MotionSensorService")
@Transactional
public class MotionSensorServiceImpl implements MotionSensorService {

	@Autowired
	private MotionSensorDAO motionSensorDAO;

	@Override
	@Transactional
	public void addMotionSensor(MotionSensor sensor) {
		motionSensorDAO.addMotionSensor(sensor);
	}

	@Override
	@Transactional
	public List<MotionSensor> getAllMotionSensors() {
		return motionSensorDAO.getAllMotionSensors();
	}

	@Override
	@Transactional
	public void deleteMotionSensor(Integer sensorId) {
		motionSensorDAO.deleteMotionSensor(sensorId);
	}

	public MotionSensor getMotionSensor(int id) {
		return motionSensorDAO.getMotionSensor(id);
	}
	
	public MotionSensor updateMotionSensor(MotionSensor sensor) {
		// TODO Auto-generated method stub
		return motionSensorDAO.updateMotionSensor(sensor);
	}

	public void setMotionSensorDAO(MotionSensorDAO motionSensorDAO) {
		this.motionSensorDAO = motionSensorDAO;
	}

	public List<MotionSensor> getMotionSensor(String sensorKey, String sensorValue) {
		return motionSensorDAO.getMotionSensor(sensorKey, sensorValue);
	}
	
	public MotionSensor getMotionSensorWithParams(String sensorKey, String sensorValue, int timestamp) {
		return motionSensorDAO.getMotionSensorWithParams(sensorKey, sensorValue, timestamp);
	}

	public MotionSensor getMotionSensorLatestValue()
	{
		return motionSensorDAO.getMotionSensorLatestValue();
	}
}
