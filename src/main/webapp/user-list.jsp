<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>User Management Application</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
<link rel="stylesheet" href="css/style_all.css">
</head>
<body>

	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: black">
			<ul class="navbar-nav" >
				<li><a href="UserServlet?action=list" class="nav-link">列表頁</a></li>
			</ul>
		</nav>
	</header>
	<br>
	<div class="row">
		<!-- <div class="alert alert-success" *ngIf='message'>{{message}}</div> -->

		<div class="container">
			<h3 class="text-center">資料庫內容</h3>
			<hr>
			<div class="container text-left">
				<a href="UserServlet?action=new" class="btn btn-success">Add New User</a>
			</div>

			<br>
			<div class="card">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>ID</th>
						<th>Time</th>
						<th>images</th>
					</tr>
				</thead>
				<tbody>
					<!--   for (Todo todo: todos) {  -->
					<c:forEach var="user" items="${displayedUsers}">
						<tr>
							<!-- c:out 只是一個輸出頁面的標籤 -->
							<td><c:out value="${user.id}" /></td> 
							<td><c:out value="${user.time}" /></td>				
							<td style="text-align:center;"><img src="data:image/jpeg;base64,${user.images}" alt="Image"></td>
							<!-- img>標籤中的 src 屬性用於指定圖像的源。在這裡，
							我們使用了一種特殊的數據URI 方案來表示圖像數據。
							數據URI 是一種將數據內聯嵌入到URL中的方案，這裡使用的是data:image/jpeg;base64。 -->
							<!-- 通過數據URI 方案直接嵌入到HTML 中。這樣可以避免使用外部圖像文件，方便在頁面上展示和處理圖像數據。 -->
							<td><a href="UserServlet?action=edit&id=<c:out value='${user.id}'/>">Edit</a>
								&nbsp;&nbsp;&nbsp;&nbsp; 
								<a href="UserServlet?action=delete&id=<c:out value='${user.id}'/>">Delete</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
				<!-- 資料庫顯示分頁} -->
				<div class="pagination">
				    <c:if test="${currentPage > 1}">
				        <a href="UserServlet?action=list&page=${currentPage - 1}">上一頁</a>
				    </c:if>
				    <c:forEach var="page" begin="1" end="${totalPage}"> 
				        <c:choose>
				            <c:when test="${page == currentPage}">
				                <span class="current-page">${page}</span>
				            </c:when>
				            <c:otherwise>
				                <a href="UserServlet?action=list&page=${page}" >${page}</a><!-- 由於資料結構是list，這裡必須告訴她結構是list才能跳轉 -->
				            </c:otherwise>
				        </c:choose>
				    </c:forEach>	
				    <c:if test="${currentPage < totalPage}">
				        <a href="UserServlet?action=list&page=${currentPage + 1}">下一頁</a>
				    </c:if>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
