package com.gms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gms.model.*;
import com.gms.service.*;

@Controller
@SessionAttributes("User")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class);

	public UserController() {
		System.out.println("UserController");
	}

	@Autowired
	private UserService userService;


	
	@RequestMapping(value = "/listUser")
	public ModelAndView listUser(ModelAndView model, @ModelAttribute("User") User userSession) throws IOException 
	{
		List<User> listUser = userService.getAllUsers();
		ModelAndView mv = new ModelAndView("userManagement","sessionInfo", userSession.getRole());
		mv.addObject("listUser", listUser);
	
		return mv;
	}


		
	@RequestMapping(value = "/addUser", method = RequestMethod.GET)
	public ModelAndView addUser(ModelAndView model, @ModelAttribute("User") User userSession) 
	{
		User user = new User();
		
		ModelAndView mv = new ModelAndView("userRegistration","sessionInfo", userSession.getRole());
		mv.addObject("user", user);
		
		return mv;
	}

	@RequestMapping(value = "/saveUser", method = RequestMethod.POST)
	public ModelAndView saveUser(@RequestParam("id")String id,
								@RequestParam("email")String email,
								@RequestParam("password")String password,
								@RequestParam("name")String name,
								@RequestParam("phone")String phone,
								@ModelAttribute("User") User userSession
								) 
	{
		String role = "Guest";  // default
		 
		if(id.equals("0"))
		{
			User user = new User(name, email, password, phone, role);
			userService.addUser(user);
			return new ModelAndView("result", "sessionInfo", userSession.getRole());
		}
		else 
		{ 
			User user = new User(Integer.parseInt(id), name, email, password, phone, role);
			userService.updateUser(user);  
			return new ModelAndView("redirect:/listUser", "sessionInfo", userSession.getRole());
		}
	}

    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public ModelAndView deleteUser(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int id = Integer.parseInt(request.getParameter("id"));
        userService.deleteUser(id);
        return new ModelAndView("redirect:/listUser", "sessionInfo", userSession.getRole());
    }
	
    
    @RequestMapping(value = "/editUser", method = RequestMethod.GET)
    public ModelAndView editUser(HttpServletRequest request, @ModelAttribute("User") User userSession) 
    {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUser(userId);
        ModelAndView model = new ModelAndView("userRegistration","sessionInfo", userSession.getRole());
        model.addObject("user", user);

        return model;
    }
 
}