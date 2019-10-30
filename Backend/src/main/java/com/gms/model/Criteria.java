package com.gms.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Criteria")
public class Criteria 
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column
	private String spaceSize;	
	@Column
	private String plantType;	
	
	@Column
	private String emailNotification;		
	@Column
	private String emailAccount;		
	@Column
	private String emailPassword;	
	@Column
	private String notificationInterval;	
	
	@Column
	private String criteriaWater;
	@Column
	private String criteriaSunshine;
	@Column
	private String criteriaTemperature;
	@Column
	private String criteriaHumidity;
	@Column
	private String criteriaVentilation;

	@Column
	private String gapTemperature;
	@Column
	private String gapHumidity;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getSpaceSize() {
		return spaceSize;
	}
	public void setSpaceSize(String spaceSize) {
		this.spaceSize = spaceSize;
	}
	public String getPlantType() {
		return plantType;
	}
	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}
	public String getEmailNotification() {
		return emailNotification;
	}
	public void setEmailNotification(String emailNotification) {
		this.emailNotification = emailNotification;
	}
	
	public String getEmailAccount() {
		return emailAccount;
	}
	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	public String getNotificationInterval() {
		return notificationInterval;
	}
	public void setNotificationInterval(String notificationInterval) {
		this.notificationInterval = notificationInterval;
	}
	
	public String getCriteriaWater() {
		return criteriaWater;
	}
	public void setCriteriaWater(String criteriaWater) {
		this.criteriaWater = criteriaWater;
	}
	public String getCriteriaSunshine() {
		return criteriaSunshine;
	}
	public void setCriteriaSunshine(String criteriaSunshine) {
		this.criteriaSunshine = criteriaSunshine;
	}
	public String getCriteriaTemperature() {
		return criteriaTemperature;
	}
	public void setCriteriaTemperature(String criteriaTemperature) {
		this.criteriaTemperature = criteriaTemperature;
	}
	public String getCriteriaHumidity() {
		return criteriaHumidity;
	}
	public void setCriteriaHumidity(String criteriaHumidity) {
		this.criteriaHumidity = criteriaHumidity;
	}
	public String getCriteriaVentilation() {
		return criteriaVentilation;
	}
	public void setCriteriaVentilation(String criteriaVentilation) {
		this.criteriaVentilation = criteriaVentilation;
	}
	public String getGapTemperature() {
		return gapTemperature;
	}
	public void setGapTemperature(String gapTemperature) {
		this.gapTemperature = gapTemperature;
	}
	public String getGapHumidity() {
		return gapHumidity;
	}
	public void setGapHumidity(String gapHumidity) {
		this.gapHumidity = gapHumidity;
	}
	
	
	
	
	
}
