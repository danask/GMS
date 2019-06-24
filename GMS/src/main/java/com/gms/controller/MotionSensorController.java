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
@RequestMapping("/MotionSensor")
@SessionAttributes("User")
public class MotionSensorController {

	private static final Logger logger = Logger.getLogger(MotionSensorController.class);

	public MotionSensorController() {
		System.out.println("MotionSensorController");
	}

	@Autowired
	private MotionSensorService motionSensorService;

	
	@RequestMapping(value = "/searchMotionSensor", method = RequestMethod.GET)
	public ModelAndView searchMotionSensor(ModelAndView model, @ModelAttribute("User") User userSession) {
		MotionSensor sensor = new MotionSensor();
		
		ModelAndView mv = new ModelAndView("sensorSearch", "sessionInfo", userSession.getRole());
		mv.addObject("sensor", sensor);
		
		return mv;
	}
	
	
	@RequestMapping(value = "/searchMotionSensorResult", method = RequestMethod.POST)
	public ModelAndView searchMotionSensorResult(@RequestParam("sensorKey")String sensorKey,
									@RequestParam("sensorValue")String sensorValue,
//									@RequestParam("temperature")int temperature,
									@ModelAttribute("User") User userSession) throws IOException 
	{
		List<MotionSensor> listMotionSensor = motionSensorService.getMotionSensor(sensorKey, sensorValue);
        ModelAndView model = new ModelAndView("sensorSearchResult", "sessionInfo", userSession.getRole());
        model.addObject("listMotionSensor", listMotionSensor);
		
		return model;
	}	
	
	@CrossOrigin
	@RequestMapping(value = "/motionSensorAll", method = RequestMethod.GET)
	public List<MotionSensor> sensorAll() throws IOException  
	{
		List<MotionSensor> listMotionSensor = motionSensorService.getAllMotionSensors();
		
		return listMotionSensor;
	}
	
//	@CrossOrigin
//	@RequestMapping(value = "/weatherAll", method = RequestMethod.GET)
//	public List<Weather> weatherAll() throws IOException  
//	{
////		Weather weather = new Weather();
////		
////		
////		
////		return listMotionSensor;
//	}
	
	
	
	@RequestMapping(value = "/listMotionSensor")
	public ModelAndView listMotionSensor(ModelAndView model, @ModelAttribute("User") User userSession) throws IOException 
	{
		List<MotionSensor> listMotionSensor = motionSensorService.getAllMotionSensors();
		ModelAndView mv = new ModelAndView("sensorManagement","sessionInfo", userSession.getRole());
		mv.addObject("listMotionSensor", listMotionSensor);
		
		return mv;
	}
	
	@RequestMapping(value = "/addMotionSensor", method = RequestMethod.GET)
	public ModelAndView addMotionSensor(ModelAndView model, @ModelAttribute("User") User userSession) {
		MotionSensor sensor = new MotionSensor();
		
		ModelAndView mv = new ModelAndView("sensorRegistration","sessionInfo", userSession.getRole());
		mv.addObject("sensor", sensor);
		
		return mv;
	}


	
    @RequestMapping(value = "/deleteMotionSensor", method = RequestMethod.GET)
    public ModelAndView deleteMotionSensor(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int id = Integer.parseInt(request.getParameter("id"));
        motionSensorService.deleteMotionSensor(id);
        return new ModelAndView("redirect:/listMotionSensor", "sessionInfo", userSession.getRole());
    }
    
    @RequestMapping(value = "/editMotionSensor", method = RequestMethod.GET)
    public ModelAndView editMotionSensor(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int sensorId = Integer.parseInt(request.getParameter("id"));
        MotionSensor sensor = motionSensorService.getMotionSensor(sensorId);
        ModelAndView model = new ModelAndView("sensorRegistration", "sessionInfo", userSession.getRole());
        model.addObject("sensor", sensor);
 
        return model;
    }
}