package com.gms.dao;

import java.util.List;

import com.gms.model.User;

public interface UserDAO {

	public void addUser(User user);

	public List<User> getAllUsers();

	public void deleteUser(Integer userId);

	public User updateUser(User user);

	public User getUser(int userid);
	
	public User getUserByEmailPwd(String email, String pwd);	
}
