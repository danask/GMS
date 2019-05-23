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

	private static int TEST_COUNT= 1;
	private String id;
	private String password;
	private int originalCount = 0;
	User currentUser;
	User[] testUser = new User[TEST_COUNT];
	
	@Before
	public void setUp() throws Exception 
	{
	}

	@Test
	public void userTest()
	{	
		// test users
		for(int i = 0; i < TEST_COUNT; i++)
		{
			testUser[i] = new User();
			
			testUser[i].setEmail("tester" + i + "@dc.com");
			testUser[i].setName("tester" + i);
			testUser[i].setPassword("test1010"+ i);
			testUser[i].setPhone("111-111-111"+ i);
			
			assertEquals(testUser[i].getEmail(), "tester" + i + "@dc.com");
			assertEquals(testUser[i].getName(), "tester" + i);
			assertEquals(testUser[i].getPassword(), "test1010"+ i);
			assertEquals(testUser[i].getPhone(), "111-111-111"+ i);
		}
	}
	
	@Test
	public void userRoleTest()
	{	
		// test users
		for(int i = 0; i < TEST_COUNT; i++)
		{
			testUser[i] = new User();
			testUser[i].setRole("Administrator");
			assertEquals(testUser[i].getRole(), "Administrator");
		}
	}
}
