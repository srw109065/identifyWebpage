<%@page import="com.customer.UserServlet.*"%>
<%@page import="org.apache.jasper.tagplugins.jstl.core.Import"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>User Management Application</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"	crossorigin="anonymous">
	<link rel="stylesheet" href="css/style_all.css">
<script>
    function showImage(event) {
    //讀圖片
      var input = event.target;
      var reader = new FileReader();
      
      //顯示圖片//
      reader.onload = function(){
        var img = document.getElementById('image');
        img.src = reader.result;
      };
      
      reader.readAsDataURL(input.files[0]);
    }
</script>
</head>
<body>
	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: black">
			<ul class="navbar-nav">
				<li><a href="UserServlet?action=list" class="nav-link">列表頁</a></li>
			</ul>
		</nav>
	</header>
	<br>
	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
				<c:if test="${user != null}">
					<form action="UserServlet?action=update" method="post" enctype="multipart/form-data">
				</c:if>
				<c:if test="${user == null}">
					<form action="UserServlet?action=insert" method="post" enctype="multipart/form-data">
				</c:if>
				<caption>
					<h2>
						<c:if test="${user != null}">Edit User</c:if>
						<c:if test="${user == null}">Add New User</c:if>
					</h2>
				</caption>
				<c:if test="${user != null}">
					<input type="hidden" name="id" value="<c:out value='${user.id}'/>" />
				</c:if>
				<fieldset class="form-group">
					<label>Insert Time</label>
					<input type="text" value="<c:out value='${user.time}' />" class="form-control" name="time" required="required">
				</fieldset>
				<fieldset class="form-group">
					<label>Insert Image</label>
					<br>
					<input type="file" name="image" accept="image/*" onchange="showImage(event)" />
					<br>
					<img id="image" src="" alt="Selected Image" />
				</fieldset>
				<button type="submit" class="btn btn-success">Save</button>
			</div>
		</div>
	</div>
</body>
</html>
