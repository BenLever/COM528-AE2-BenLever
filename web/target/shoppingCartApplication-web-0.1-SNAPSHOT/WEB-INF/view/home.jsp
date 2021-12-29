<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//request.setAttribute("selectedPage", "home");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Home</H1>
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>
    
    <div class="row">
        <form action="./home" method="GET"> 
            <div class="col-xs-6 col-md-4">
                <button class="btn btn-primary" type="submit">
                  Clear
                </button>
            </div>
        </form>
        <form action="./home" method="GET"> 
            <div class="col-xs-6 col-md-4">
              <div class="input-group">
                  <input type="text" class="form-control" placeholder="Search" name="searchQuery" id="searchQuery"/>            
                  <input type="hidden" name="action" value="search">
                <div class="input-group-btn">
                  <button class="btn btn-primary" type="submit">
                    <svg xmlns="http://www.w3.org/2000/svg" style="height: 15px; width: 15px;" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                  </button>
                </div>
              </div>
            </div>
        </form>
        <div class="col-xs-6 col-md-4" style="text-align: end; list-style-position: inside;">
            <h1>Item Categories</h1>
            <ul>
                <c:forEach var="category" items="${categories}">
                    <li><a href="./home?category=${category}">${category}</a></li>      
                </c:forEach>

            </ul>
        </div>

    <H1>Available Items</H1>
    <table class="table">

        <tr>
            <th>Item Name</th>
            <th>Price</th>
            <th></th>
        </tr>

        <c:forEach var="item" items="${availableItems}">

            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td></td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home" method="get">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="addItemToCart">
                        <button type="submit" >Add Item</button>
                    </form> 
                </td>
            </tr>

        </c:forEach>
    </table>

    <H1>Shopping cart</H1>
    <table class="table">

        <tr>
            <th>Item Name</th>
            <th>Price</th>
            <th>Quantity</th>
        </tr>

        <c:forEach var="item" items="${shoppingCartItems}">

            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td>${item.quantity}</td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home" method="post">
                        <input type="hidden" name="itemUUID" value="${item.uuid}">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="removeItemFromCart">
                        <button type="submit" >Remove Item</button>
                    </form> 
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td>TOTAL</td>
            <td>${shoppingcartTotal}</td>
        </tr>
    </table>



</main>
<jsp:include page="footer.jsp" />
