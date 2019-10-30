<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<% String role = (String)request.getAttribute("sessionInfo"); %>

<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
		<title>User Management</title>
	    <style>
	    	#list {
			    margin-left: 2em;
			    margin-right: 2em;
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
		
		<div id="list">
			<div class="containter">
				
				<h3 id="form_header" align="center">User List</h3>
		        <div>&nbsp;</div>		
		        <!-- h2>
		            <a href="userManagement">User List</a>
		        </h2-->
		        
		        <c:if test="${!empty listUser }">
			        <table class="table table-striped">
			 
			 			<!-- <th>ID</th> -->
			            <th>Name</th>
			            <th>Email</th>
			            <th>Phone Number</th>
			            <th>Role</th>
			            <th></th>
			             
			            <c:forEach var="user" items="${listUser}">
			                <tr>
			                    <%-- <td>${user.id}</td> --%>
			                    <form:hidden path="id"/>
			                    <td>${user.name}</td>
			                    <td>${user.email}</td>
			                    <td>${user.phone}</td>
			                    <td>${user.role}</td>
		 						<td><a href="editUser?id=${user.id}">Edit</a>&nbsp;&nbsp;
			                      <a href="deleteUser?id=${user.id}">Delete</a></td>
			                </tr>
			            </c:forEach>
			        </table>
			    </c:if>
		    </div>
		</div>

	</body>
</html>