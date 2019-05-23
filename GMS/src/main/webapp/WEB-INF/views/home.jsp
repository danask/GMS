<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String role = (String)request.getAttribute("sessionInfo"); %>
 
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Login</title>
	</head>
<body>

	<head>	    
	    <title>Home</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
	    <style>
	    
			  .jumbotron {
			    background-color: #f4511e;
			    color: #fff;
			    padding: 100px 25px;
			  }
			  .container-fluid {
			    padding: 60px 50px;
			  }
			  .bg-grey {
			    background-color: #f6f6f6;
			  }
			  .logo {
			    font-size: 200px;
			  }
			  @media screen and (max-width: 768px) {
			    .col-sm-4 {
			      text-align: center;
			      margin: 25px 0;
			    }
			  }
	    
	    	li:nth-of-type(4){margin-left: 25em;}
		</style>
	</head>
	
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
<!--    				  <li class="nav-item">
			        <a class="nav-link" href="#">Search</a>
			      </li>	 -->		   
   			   <%if(role.equals("Movie Database Administrator")	|| role.equals("Administrator")){%>
    			   
				  <li class="nav-item">
			        <a class="nav-link" href="#">List</a>
			      </li>   			   
			      <li class="nav-item">
			        <a class="nav-link" href="#">Database</a>
			      </li>  
		       <%}%>
 		       			         
		      <li class="nav-item">
		        <a class="nav-link" href="addUser">User Registration</a>
		      </li>	
		       		
			   <%if(role.equals("Administrator")){%>
		      	  <li class="nav-item">
			        <a class="nav-link" href="listRole">Admin</a>
			      </li>
		       <%}%>
			   
			   <%if(role.equals("User Manager")	|| role.equals("Administrator")){%>			   
			      <li class="nav-item">
			        <a class="nav-link" href="listUser">Users</a>
			      </li>	 
			   <%}%>
			   
		      <li class="nav-item">
		        <a class="nav-link" href="/GMS/">Log out</a>
		      </li>
		    </ul>
		  </div>
		</nav>
	
		<br/><br/>
	    <div class="container">
	        <h3 id="form_header" align="center">Gardening Management System</h3>
	        <div>&nbsp;</div>
	
			<h4>Current Temperature: ${sensors.sensorTemp}.0 degree</h4>
			<h4>Current Humidity: ${sensors.sensorHumid}.0 %</h4>
	 
			

   		</div>

	</body>
</html>