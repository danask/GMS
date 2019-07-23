package com.gms.controller;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.gms.model.Criteria;
import com.gms.model.Sensor;
import com.gms.model.User;
import com.gms.service.CriteriaService;
import com.gms.service.SensorService;

@RestController
@RequestMapping("/Criteria")
public class CriteriaController {
	private static final Logger logger = Logger.getLogger(CriteriaController.class);

	public CriteriaController() {
		System.out.println("CriteriaController");
	}

	@Autowired
	private CriteriaService criteriaService;

	@Autowired
	private SensorService sensorService;
	
	
	@CrossOrigin
	@RequestMapping(value = "/getCriteria", method = RequestMethod.GET)
	public Criteria getCriteria() throws IOException  
	{
		Sensor sensor = sensorService.getSensorLatestValue();
		Criteria criteria = criteriaService.getCriteria();
		
		int gapTemp = Integer.parseInt(criteria.getCriteriaTemperature()) 
				- Integer.parseInt(sensor.getSensorTemp());
		
		if(gapTemp > 0)
		{
			criteria.setGapTemperature(("Need " + String.valueOf(gapTemp)) + " up");
		}
		else
		{
			criteria.setGapTemperature(("Need " + String.valueOf(-gapTemp)) + " down");
		}
		
		int gapHumid = Integer.parseInt(criteria.getCriteriaHumidity()) 
						- Integer.parseInt(sensor.getSensorHumid());
		if(gapHumid > 0)
		{
			criteria.setGapHumidity(("Need " + String.valueOf(gapHumid)+ " up"));	
		}
		else
		{
			criteria.setGapHumidity("Good");
		}
		
		return criteria;
	}
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/saveCriteria", method = RequestMethod.POST)
	public Criteria saveCriteria(@RequestBody Criteria criteria) 
	{
		System.out.println(criteria.getPlantType() + ", " + criteria.getSpaceSize());
		
		Sensor sensor = sensorService.getSensorLatestValue();
		Criteria orgCriteria = criteriaService.getCriteria();
		
		int gapTemp = Integer.parseInt(orgCriteria.getCriteriaTemperature()) 
				- Integer.parseInt(sensor.getSensorTemp());
		
		if(gapTemp > 0)
		{
			criteria.setGapTemperature(("Need " + String.valueOf(gapTemp)) + " up");
		}
		else
		{
			criteria.setGapTemperature(("Need " + String.valueOf(-gapTemp)) + " down");
		}
		
		int gapHumid = Integer.parseInt(orgCriteria.getCriteriaHumidity()) 
						- Integer.parseInt(sensor.getSensorHumid());
		if(gapHumid > 0)
		{
			criteria.setGapHumidity(("Need " + String.valueOf(gapHumid)+ " up"));	
		}
		else
		{
			criteria.setGapHumidity("Good");
		}
		
		
		criteria.setId(1);  // default
		int criteriaWater = 2000;
				
		switch(criteria.getPlantType())
		{
			case "Dry":
				criteriaWater *= 0.5;
				break;
				
			case "Normal":
				criteriaWater *= 1.0;
				break;
				
			case "Wet":
				criteriaWater *= 2.0;
				break;		

			case "Mixed":
				criteriaWater *= 1.5;
				break;				
		}
		
		
		switch(criteria.getSpaceSize())
		{
			case "Indoor":
				criteriaWater *= 1.0;
				break;
				
			case "Medium":
				criteriaWater *= 2.0;
				break;
				
			case "Large":
				criteriaWater *= 3.0;
				break;		

			case "Outdoor":
				criteriaWater *= 5.0;
				break;				
		}	
		
		
		criteria.setCriteriaWater(String.valueOf(criteriaWater));
		System.out.println(criteria.getPlantType() + ", " + criteria.getSpaceSize() + ", " 
								+ criteriaWater);
		criteriaService.updateCriteria(criteria);  

		return criteria;
	}
}
