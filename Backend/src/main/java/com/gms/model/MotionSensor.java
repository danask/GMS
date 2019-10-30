package com.gms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MotionSensor")
public class MotionSensor 
{
	private static final long serialVersionUID = -3465813074586302847L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private long timestamp;
	@Column
	private String dateTime;	
	@Column
	private String description;
	@Column
	private String alert;
	@Column
	private String pirId;
	@Column
	private String detectTime;


	
	public MotionSensor() {};
	
	public MotionSensor(int timestamp, String description) {
		this.timestamp = timestamp;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long l) {
		this.timestamp = l;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	// PIR	
	public String isAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getPirId() {
		return pirId;
	}

	public void setPirId(String pirId) {
		this.pirId = pirId;
	}

	public String getDetectTime() {
		return detectTime;
	}

	public void setDetectTime(String detectTime) {
		this.detectTime = detectTime;
	}

	@Override
	public String toString() {
		return "MotionSensor [id=" + id + ", timestamp=" + timestamp + ", dateTime=" + dateTime + ", description="
				+ description + ", alert=" + alert + ", pirId=" + pirId + ", detectTime=" + detectTime + "]";
	}

}
