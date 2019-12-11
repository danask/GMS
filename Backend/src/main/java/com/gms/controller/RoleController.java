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

import io.swagger.annotations.Api;

@Controller
@Api(value="swag-rest-controller")
@RequestMapping("/Role")
@SessionAttributes("User")
public class RoleController {

	private static final Logger logger = Logger.getLogger(RoleController.class);

	public RoleController() {
		System.out.println("RoleController");
	}

	@Autowired
	private UserService userService;


//	@RequestMapping(method = RequestMethod.GET)
//	public String setUserSession(HttpServletRequest request){
//	    request.getSession().setAttribute("role", "Administrator");
//	    return "setSession";
//	}
//	
//	@RequestMapping(value = "/info")
//   public String userInfo(@ModelAttribute("User") User user) {
//
//      System.out.println("Email: " + user.getEmail());
//      System.out.println("Role: " + user.getRole());
//
//      return "user";
//   }
	
	
	@RequestMapping(value = "/listRole")
	public ModelAndView listRole(ModelAndView model, @ModelAttribute("User") User user) throws IOException 
	{
		List<User> listUser = userService.getAllUsers();
		model.addObject("listUser", listUser);
		model.setViewName("roleManagement");
	
	      System.out.println("Email: " + user.getEmail());
	      System.out.println("Role: " + user.getRole());
		
		return model;
	}
	
    
    @RequestMapping(value = "/editRole", method = RequestMethod.GET)
    public ModelAndView editRole(HttpServletRequest request) 
    {
        int userId = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUser(userId);
        ModelAndView model = new ModelAndView("roleRegistration");
        model.addObject("user", user);
 
        return model;
    }
	
	@RequestMapping(value = "/saveRole", method = RequestMethod.POST)
	public ModelAndView saveRole(@RequestParam("id")String id,
								@RequestParam("email")String email,
								@RequestParam("password")String password,
								@RequestParam("name")String name,
								@RequestParam("phone")String phone,
								@RequestParam("role")String role
								) 
	{
		String msg = "Role was changed successfully";
		System.out.println("----------- Role --------------" + role);
		
		if(id.equals("0"))
		{
			User user = new User(name, email, password, phone, role);
			userService.addUser(user);
			return new ModelAndView("result", "output", msg);
		}
		else 
		{ 
			User user = new User(Integer.parseInt(id), name, email, password, phone, role);
			userService.updateUser(user);  
			return new ModelAndView("redirect:/listRole");
		}
	}


 
}