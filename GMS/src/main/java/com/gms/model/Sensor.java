package com.gms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Sensor")
public class Sensor 
{
	private static final long serialVersionUID = -3465813074586302847L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private String sensorTemp;
	@Column
	private String sensorHumid;
	@Column
	private int timestamp;
	@Column
	private String description;
	@Column
	private float temperature;
	@Column
	private float humidity;

	
	public Sensor() {};
	
	public Sensor(String sensorTemp, String sensorHumid, int timestamp, String description) {
		this.sensorTemp = sensorTemp;
		this.sensorHumid = sensorHumid;
		this.timestamp = timestamp;
		this.description = description;
	}
	
	public Sensor(float temperature, float humidity, int timestamp, String description) {
		this.temperature = temperature;
		this.humidity = humidity;
		this.timestamp = timestamp;
		this.description = description;
	}
	
	public Sensor(int id, String sensorTemp, String sensorHumid, int timestamp, String description) {
		this.id = id;
		this.sensorTemp = sensorTemp;
		this.sensorHumid = sensorHumid;
		this.timestamp = timestamp;
		this.description = description;
	}
	
	public Sensor(String sensorTemp) {
		super();
		this.sensorTemp = sensorTemp;
		System.out.println("Sensor " + sensorTemp + "has been created.");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSensorTemp() {
		return sensorTemp;
	}
	public void setSensorTemp(String sensorTemp) {
		this.sensorTemp = sensorTemp;
	}
	public String getSensorHumid() {
		return sensorHumid;
	}
	public void setSensorHumid(String sensorHumid) {
		this.sensorHumid = sensorHumid;
	}
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getTemperature() {
		return temperature;
	}
	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}
	public float getHumidity() {
		return humidity;
	}
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	
	@Override
	public String toString() {
		return "Sensor [sensorId=" + id + ", sensorTemp=" + sensorTemp + ", sensorHumid=" + sensorHumid + ", timestamp="
				+ timestamp + ", description=" + description + "]";
	}

}
