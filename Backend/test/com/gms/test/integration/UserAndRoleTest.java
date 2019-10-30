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

import com.gms.controller.UserController;
import com.gms.model.User;
import com.gms.service.UserService;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {TestBeanConfig.class})
//@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
@ContextConfiguration (locations = {"file:src/main/webapp/WEB-INF/spring-servlet.xml"})

public class UserAndRoleTest {
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UserService userService;

	private static int TEST_COUNT= 3;
	private String id;
	private String password;
	private int originalCount = 0;
	User currentUser;
	User[] testUser = new User[TEST_COUNT];
	
	@Before
	public void setUp() throws Exception 
	{
		// user for login
		currentUser = userService.getUserByEmailPwd("dan@dc.com", "admin1010");
		
		List<User> resultUserList = userService.getAllUsers();

		
		// check current status
		for(User u : resultUserList)
		{
			System.out.println("User: " + u.toString());
			originalCount++;
		}

		// test users
		for(int i = 0; i < TEST_COUNT; i++)
		{
			testUser[i] = new User();
			
			testUser[i].setEmail("tester" + i + "@dc.com");
			testUser[i].setName("tester" + i);
			testUser[i].setPassword("test1010"+ i);
			testUser[i].setPhone("111-111-111"+ i);
			testUser[i].setRole("Guest");
		}	
		
		testMultipleFunctions();
	}

	@Test
	public void userValidation()
	{
		if(currentUser == null)
			fail("Login failed");
		
		assertThat(currentUser, notNullValue());
		assertThat(currentUser, instanceOf(User.class));
	}
	
	
	@Test
	public void testMultipleFunctions()
	{
		addUsers();
		updateUser();
		deleteUser();
	}
	
	public void addUsers()
	{
		User user = new User();
		
		for(int i = 0; i < TEST_COUNT; i++)
		{

			assertThat(testUser[i], instanceOf(User.class));
			System.out.println("Added User: " + testUser[i].toString());
			
			try {
				userService.addUser(testUser[i]);
				
				// check the result
				user = userService.getUserByEmailPwd(testUser[i].getEmail(), testUser[i].getPassword());
				assertThat(user.getName(), equalTo(testUser[i].getName()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	

	public void updateUser()
	{
		for(int i = 0; i < TEST_COUNT; i++)
		{
			try {
				User user = userService.getUserByEmailPwd(testUser[i].getEmail(), testUser[i].getPassword());
				assertThat(user.getName(), equalTo(testUser[i].getName()));
				user.setRole("Admin");
				
				userService.updateUser(user);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}


	public void deleteUser()
	{		
		try {
			
			for(int i = 0; i < TEST_COUNT; i++)
			{
				User user = userService.getUserByEmailPwd(testUser[i].getEmail(), testUser[i].getPassword());
				assertThat(user.getName(), equalTo(testUser[i].getName()));

				// check id to delete
				assertThat(user.getId(), notNullValue());
				userService.deleteUser(user.getId());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	
	@Test
	public void getUserList()
	{	
		int count = 0;
		
		List<User> resultUserList = userService.getAllUsers();

		for(User u : resultUserList)
		{
			System.out.println("User: " + u.toString());
			count++;
		}
		
		// Check total count between AS-IS and TO-BE 
		assertThat(count, is(originalCount)); 
	}
}
