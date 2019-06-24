package com.gms.controller;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gms.model.*;
import com.gms.service.*;

@RestController
@RequestMapping("/Sensor")
@SessionAttributes("User")
public class SensorController {

	private static final Logger logger = Logger.getLogger(SensorController.class);

	public SensorController() {
		System.out.println("SensorController");
	}

	@Autowired
	private SensorService sensorService;

	
	@RequestMapping(value = "/searchSensor", method = RequestMethod.GET)
	public ModelAndView searchSensor(ModelAndView model, @ModelAttribute("User") User userSession) {
		Sensor sensor = new Sensor();
		
		ModelAndView mv = new ModelAndView("sensorSearch", "sessionInfo", userSession.getRole());
		mv.addObject("sensor", sensor);
		
		return mv;
	}
	
	
	@RequestMapping(value = "/searchSensorResult", method = RequestMethod.POST)
	public ModelAndView searchSensorResult(@RequestParam("sensorKey")String sensorKey,
									@RequestParam("sensorValue")String sensorValue,
//									@RequestParam("temperature")int temperature,
									@ModelAttribute("User") User userSession) throws IOException 
	{
		List<Sensor> listSensor = sensorService.getSensor(sensorKey, sensorValue);
        ModelAndView model = new ModelAndView("sensorSearchResult", "sessionInfo", userSession.getRole());
        model.addObject("listSensor", listSensor);
		
		return model;
	}	
	
	@CrossOrigin
	@RequestMapping(value = "/sensorAll", method = RequestMethod.GET)
	public List<Sensor> sensorAll() throws IOException  
	{
		List<Sensor> listSensor = sensorService.getAllSensors();
		
		return listSensor;
	}
	
//	@CrossOrigin
//	@RequestMapping(value = "/weatherAll", method = RequestMethod.GET)
//	public List<Weather> weatherAll() throws IOException  
//	{
////		Weather weather = new Weather();
////		
////		
////		
////		return listSensor;
//	}
	
	
	
	@RequestMapping(value = "/listSensor")
	public ModelAndView listSensor(ModelAndView model, @ModelAttribute("User") User userSession) throws IOException 
	{
		List<Sensor> listSensor = sensorService.getAllSensors();
		ModelAndView mv = new ModelAndView("sensorManagement","sessionInfo", userSession.getRole());
		mv.addObject("listSensor", listSensor);
		
		return mv;
	}
	
	@RequestMapping(value = "/addSensor", method = RequestMethod.GET)
	public ModelAndView addSensor(ModelAndView model, @ModelAttribute("User") User userSession) {
		Sensor sensor = new Sensor();
		
		ModelAndView mv = new ModelAndView("sensorRegistration","sessionInfo", userSession.getRole());
		mv.addObject("sensor", sensor);
		
		return mv;
	}


	@RequestMapping(value = "/saveSensor", method = RequestMethod.POST)
	public ModelAndView saveSensor(@RequestParam("id")String id,
								@RequestParam("sensorKey")String sensorKey,
								@RequestParam("sensorValue")String sensorValue,
								@RequestParam("temperature")int temperature,
								@RequestParam("description")String description,
								@ModelAttribute("User") User userSession
								) 
	{
		String msg = "Registered successfully";
		
		if(id.equals("0"))
		{
			Sensor sensor = new Sensor(sensorKey, sensorValue, temperature, description);
			sensorService.addSensor(sensor);
			
			return new ModelAndView("result", "sessionInfo", userSession.getRole());
		}
		else 
		{ 
			Sensor sensor = new Sensor(Integer.parseInt(id), sensorKey, sensorValue, temperature, description);
			sensorService.updateSensor(sensor);  
			return new ModelAndView("redirect:/listSensor", "sessionInfo", userSession.getRole());
		}
	}
	
    @RequestMapping(value = "/deleteSensor", method = RequestMethod.GET)
    public ModelAndView deleteSensor(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int id = Integer.parseInt(request.getParameter("id"));
        sensorService.deleteSensor(id);
        return new ModelAndView("redirect:/listSensor", "sessionInfo", userSession.getRole());
    }
    
    @RequestMapping(value = "/editSensor", method = RequestMethod.GET)
    public ModelAndView editSensor(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int sensorId = Integer.parseInt(request.getParameter("id"));
        Sensor sensor = sensorService.getSensor(sensorId);
        ModelAndView model = new ModelAndView("sensorRegistration", "sessionInfo", userSession.getRole());
        model.addObject("sensor", sensor);
 
        return model;
    }
}