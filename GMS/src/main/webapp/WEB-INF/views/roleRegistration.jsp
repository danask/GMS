<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>	    
	    <title>Role</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	    <style>
	    	li:nth-of-type(4){margin-left: 25em;}
		</style>
	
	<body>
		
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
		  <a class="navbar-brand" href="home"><strong>GMS</strong></a>
		  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
		    <span class="navbar-toggler-icon"></span>
		  </button>
		  <div class="collapse navbar-collapse" id="navbarNav">
		    <ul class="navbar-nav">
<!-- 		      <li class="nav-item active">
		        <a class="nav-link" href="home">Home <span class="sr-only">(current)</span></a>
		      </li> -->
<!-- 			<li class="nav-item">
			  <a class="nav-link" href="#">Search</a>
			</li> 	 -->	      
			  <li class="nav-item">
		        <a class="nav-link" href="#">List</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="#">Database</a>
		      </li>     
		      <li class="nav-item">
		        <a class="nav-link" href="addUser">User Registration</a>
		      </li>	
		       		        
		      <li class="nav-item">
		        <a class="nav-link" href="listRole">Admin</a>
		      </li>
		      <li class="nav-item">
		        <a class="nav-link" href="listUser">Users</a>
		      </li>	 
		      <li class="nav-item">
		        <a class="nav-link" href="/">Log out</a>
		      </li>
		    </ul>
		  </div>
		</nav>


		<br/><br/>
	    <div class="container">
	        <h3 id="form_header" align="center">Role Registration</h3>
	        <div>&nbsp;</div>
	
        	<c:url var="saveRoleUrl" value="/saveRole" />
 	        <form id="user_form" action="${saveRoleUrl}" method="POST">
	     <!--    <form:form action="saveUser" method="post" modelAttribute="user">
	         <div class="form-group"><label for="name">User Name:</label>
	        <form:input path="name" /> 
	        </div>-->

			   <input type="hidden" name="id" value='${user.id}'/>
			   <input type="hidden" name="name" value='${user.name}'/>
			   <input type="hidden" name="email" value='${user.email}'/>			   
			   <input type="hidden" name="password" value='${user.password}'/>
			   <input type="hidden" name="phone" value='${user.phone}'/>
               <div class="form-group">
	                <label for="name">User Name:</label>
	                <input type="text" class="form-control" id="name" placeholder="Enter username" name="name" 
	                value='${user.name}' disabled/>
	            </div>
	            <div class="form-group">
	                <label for="name">Email Address (ID):</label>
	                <input type="text" class="form-control" id="email" placeholder="Enter email address" name="email" 
	                value='${user.email}'disabled>
	            </div>
	            <div class="form-group">
	                <label for="name">Role:</label>
					<select name="role" class="form-control" id="role"  size="3" required>
						<option value="Guest" ${user.role == "Guest" ? "selected" : ""}>Guest</option>
						<option value="User Manager" ${user.role == "UserManager" ? "selected" : ""}>User Manager</option>
<%-- 						<option value="Database Administrator" ${user.role == "MovieManager" ? "selected" : ""}>Movie Database Administrator</option>
 --%>						<option value="Administrator" ${user.role == "Admin" ? "selected" : ""}>Administrator</option> 						
					</select>               
	            </div>  
        		<!-- <input type="hidden" name="role" value="Admin"/> -->
        		
	            <div align="center">       	            
	            	<button id="confirm_user" type="submit" class="btn btn-dark">Apply</button>
	            </div>
            </form>  
	    </div>
	</body>
</html>