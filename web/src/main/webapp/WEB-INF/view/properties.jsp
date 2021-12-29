<%-- 
    Document   : properties
    Created on : 27 Dec 2021, 13:04:52
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "selectedPage" value = "admin" scope="request"/>
<%
// request set in controller
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Properties</H1>
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>

    <form action="./properties" method="POST">

        <p>URL<input type="text" name="url" value="${url}" required></p>
        <p>Username<input type="text" name="username" value="${username}" required></p>
        <p>Password<input type="text" name="password" value="${password}" required></p>
        <p>Shop Keeper Card <input type="text" name="shopKeeperCard" value="${shopcard}" required></p>
        <input type="hidden" name="action" value="updateProperties">

        <button class="btn" type="submit" >Update Properties</button>
    </form> 


   


</main>
<jsp:include page="footer.jsp" />
