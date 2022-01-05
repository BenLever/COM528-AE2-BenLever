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
   

    <H1>Available Items</H1>
    <table class="table">

        <tr>
            <th>Item Name</th>
            <th>Price</th>
            <th>Stock</th>
        </tr>

        <c:forEach var="item" items="${availableItems}">

            <tr>
                <td>${item.name}</td>
                <td>${item.price}</td>
                <td>${item.stock}</td>
                <td>
                    <!-- post avoids url encoded parameters -->
                    <form action="./home" method="post">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="addItemToCart">
                        <button type="submit" >Add to Cart</button>
                    </form> 
                </td>
            </tr>

        </c:forEach>
    </table>
        
    <H1>Cart</H1>
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
                        <button type="submit" >Remove ${item.name}</button>
                    </form> 

                </td>
            </tr>
        </c:forEach>
            <tr>
                <td>Cart Total</td>
                <td>${shoppingcartTotal}</td>
            </tr>
    </table>
    
    <c:if test="${sessionUser.userRole =='ANONYMOUS'}">
        <div class="row">
            <form action="./cart" method="GET"> 
                <div class="col-xs-6 col-md-4">
                    <p style="color: red;"><strong>Sign in to proceed to checkout</strong></p>
                </div>
            </form>
        </div>
    </c:if>

</main>
<jsp:include page="footer.jsp" />
