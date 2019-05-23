package com.gms.test.integration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gms.controller.SensorController;
import com.gms.model.Sensor;
import com.gms.service.SensorService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestBeanConfig.class})
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
@ContextConfiguration (locations = {"file:src/main/webapp/WEB-INF/spring-servlet.xml"})

public class SensorTest {
	
	@Autowired
	private SensorController sensorController;
	
	@Autowired
	private SensorService sensorService;

	private static int TEST_COUNT= 3;
	private String id;
	private String password;
	private int originalCount = 0;
	private boolean isAdded = false;
	
	Sensor currentSensor;
	Sensor[] testSensor = new Sensor[TEST_COUNT];
	
	
	@Before
	public void setUp() throws Exception 
	{
		// sensor for login
		List<Sensor> resultSensorList = sensorService.getAllSensors();

		
		// check current status
		for(Sensor u : resultSensorList)
		{
			System.out.println("Sensor: " + u.toString());
			originalCount++;
		}

		// test sensors
		for(int i = 0; i < TEST_COUNT; i++)
		{
			testSensor[i] = new Sensor();
			
			testSensor[i].setSensorTemp("actionSensor" + i);
			testSensor[i].setDescription("tester" + i);
			testSensor[i].setTemperature(25 + i);
			testSensor[i].setDescription("Good sensor"+ i);
		}	
		
		testMultipleFunctions();
	}

	
	@Test
	public void testMultipleFunctions()
	{
		addSensors();
		updateSensor();
		deleteSensor();
	}
	
	public void addSensors()
	{
		for(int i = 0; i < TEST_COUNT; i++)
		{

			assertThat(testSensor[i], instanceOf(Sensor.class));
			System.out.println("Added Sensor: " + testSensor[i].toString());
			
			try {
				sensorService.addSensor(testSensor[i]);
				
				// check the result
				List<Sensor> sensor = (List<Sensor>) sensorService.getSensor(testSensor[i].getSensorTemp(), testSensor[i].getSensorHumid());
				assertThat(sensor.get(0).getSensorTemp(), equalTo(testSensor[i].getSensorTemp()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}	
	}
	
	
	public void updateSensor()
	{
		for(int i = 0; i < TEST_COUNT; i++)
		{
			try {
				List<Sensor> sensor = (List<Sensor>) sensorService.getSensor(testSensor[i].getSensorTemp(), testSensor[i].getSensorHumid());
				assertThat(sensor.get(0).getSensorTemp(), equalTo(testSensor[i].getSensorTemp()));
				sensor.get(0).setDescription("updated");
				
				sensorService.updateSensor(sensor.get(0));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	public void deleteSensor()
	{		
		for(int i = 0; i < TEST_COUNT; i++)
		{
			try {
				List<Sensor> sensor = (List<Sensor>)sensorService.getSensor(testSensor[i].getSensorTemp(), testSensor[i].getSensorHumid());
				assertThat(sensor.get(0).getSensorTemp(), equalTo(testSensor[i].getSensorTemp()));

				// check id to delete
				assertThat(sensor.get(0).getId(), notNullValue());
				sensorService.deleteSensor(sensor.get(0).getId());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}		
		}
	}
	
	
	@Test
	public void getSensorList()
	{	
		int count = 0;
		
		List<Sensor> resultSensorList = sensorService.getAllSensors();

		for(Sensor u : resultSensorList)
		{
			System.out.println("Sensor: " + u.toString());
			count++;
		}
		
		// Check total count between AS-IS and TO-BE 
		assertThat(count, is(originalCount)); 
	}
}
