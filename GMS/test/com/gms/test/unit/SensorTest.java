package com.gms.test.unit;

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
	private Sensor sensorService;

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
	}

	
	@Test
	public void sensorTest()
	{
		// test sensors
		for(int i = 0; i < TEST_COUNT; i++)
		{
			testSensor[i] = new Sensor();
			
			testSensor[i].setSensorTemp("actionSensor" + i);
			testSensor[i].setSensorHumid("tester" + i);
			testSensor[i].setTemperature(20 + i);
			testSensor[i].setDescription("Good sensor"+ i);
			
			assertEquals(testSensor[i].getSensorTemp(), "actionSensor" + i);
			assertEquals(testSensor[i].getSensorHumid(), "tester" + i);
			assertEquals(testSensor[i].getTemperature(), 20 + i);
			assertEquals(testSensor[i].getDescription(), "Good sensor"+ i);
		}	
	}
}
