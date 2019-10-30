package com.gms.model;

public class Weather 
{
	private String weatherType;
	private String temp;
	private String pressure;
	private String humidity;
	
	public Weather(String weatherType, String temp, String pressure, String humidity)
	{
		this.weatherType = weatherType;
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
	}
	
	

	public String getWeatherType() {
		return weatherType;
	}
	public void setWeatherType(String weatherType) {
		this.weatherType = weatherType;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getPressure() {
		return pressure;
	}
	public void setPressure(String pressure) {
		this.pressure = pressure;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	
	
}
