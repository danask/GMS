package com.gms.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gms.dao.SensorDAOImpl;
import com.gms.model.Sensor;
import com.gms.model.Sensor;
import com.gms.model.User;
import com.gms.service.SensorService;
import com.gms.service.UserService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@Controller
@SessionAttributes("User")
public class SessionController {


	private static final Logger logger = Logger.getLogger(SessionController.class);

	public SessionController() {
		System.out.println("SessionController");
	}

	@Autowired
	private UserService userService;
	
	@Autowired
	private SensorService sensorService;
	
   @ModelAttribute("User")
   public User setUpUserForm() {
      return new User();
   }

	@RequestMapping(value = "/")
	public ModelAndView index(ModelAndView model, @ModelAttribute("User") User userSession) throws IOException 
	{
		userSession.setName("");
		userSession.setRole("");
		System.out.println("Welcome " + userSession.getEmail() + " / " + userSession.getRole()  + "!");	
		
		return new ModelAndView("index","sessionInfo", userSession.getRole());
	}
   
	@RequestMapping(value = "/home")
	public ModelAndView home(ModelAndView model, 
							@ModelAttribute("User") User userSession) 
									throws IOException 
	{
		Sensor sensors = new Sensor();
		sensors = sensorService.getSensorLatestValue();
		
		System.out.println("------"+sensors+"--------");
		
		ModelAndView mv = new ModelAndView("home","sessionInfo", userSession.getRole());
		mv.addObject("sensors", sensors);
        
		return mv;
	}
   
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView loginUser(@RequestParam("email")String email,
								@RequestParam("password")String password,
								@ModelAttribute("User") User userSession) 
										throws IOException, URISyntaxException 
	{	
		String msg = "";
		
		User user = userService.getUserByEmailPwd(email, password);
		
		// service start
		//sensorService.fetchSensorDataFromFirebase();
		
		if(user == null)
		{
			msg = "Invalid credentials";
			return new ModelAndView("fail", "sessionInfo", userSession.getRole());
		}
		else //if(!user.getEmail().equalsIgnoreCase(null))
		{
			Sensor sensors = new Sensor();
			sensors = sensorService.getSensorLatestValue();
			
			System.out.println("------"+sensors+"--------");
			
			userSession.setName(user.getEmail());
			userSession.setRole(user.getRole());
			System.out.println("Welcome " + userSession.getEmail() + " / " + userSession.getRole()  + "!");	
			
			ModelAndView mv = new ModelAndView("home","sessionInfo", userSession.getRole());
			mv.addObject("sensors", sensors);
			
			return mv;
		}
	}	
}