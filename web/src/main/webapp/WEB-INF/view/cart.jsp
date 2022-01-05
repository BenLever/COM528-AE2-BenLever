<%-- 
    Document   : cart
    Created on : 2 Jan 2022, 16:14:20
    Author     : Ben
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Cart</H1>
    <div style="color:red;">${errorMessage}</div>
    <div style="color:green;">${message}</div>

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
                    <form action="./cart" method="post">
                        <input type="hidden" name="itemId" value="${item.uuid}">
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
        <td>
                <H1>Enter Your Card Details</H1>
                <form action="./cart" method="post">
                    <input type="hidden" name="action" value="purchase">
                    <p>Name: <input type="text" value="${user.firstName}" name="name" ></input></p>
                    <p>Expriy Date: <input type="text" value="${user.custexpirydate}" name="cust_expirydate" ></input></p>
                    <p>Card Number: <input type="text" value="${user.custcardnumber}" name="cust_cardnumber" ></input></p>
                    <p>CVV: <input type="text" name="cust_cvv" ></input></p>
                    <p>Issue Number: <input type="text" value="${user.custissuenumber}" name="cust_issuenumber" ></input></p>
                    <p>Total £${shoppingcartTotal} </p>
                    <p><button type="submit" >Purchase</button></p>
                </form>



        </td>
    </table>
     

    



</main>
<jsp:include page="footer.jsp" />