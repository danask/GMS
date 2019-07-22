package com.gms.controller;

import java.io.IOException;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
