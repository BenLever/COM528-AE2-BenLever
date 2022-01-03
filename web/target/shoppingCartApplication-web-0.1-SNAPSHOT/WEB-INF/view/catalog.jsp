<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//    request.setAttribute("selectedPage","about");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <H1>Manage Catalog</H1>
    
     <H1>Available Items</H1>
     <p>${availableItemsSize} </p>
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
                    <form action="./catalog" method="get">
                        <input type="hidden" name="itemName" value="${item.name}">
                        <input type="hidden" name="action" value="addItemToCart">
                        <button type="submit" >Add Item</button>
                    </form> 
                </td>
            </tr>
        </c:forEach>
            
        
        <c:if test="${user.userRole =='ADMINISTRATOR'}">
            <p> Add New Item </p>
            <form action="./catalog" method="post">
                <input type="hidden" name="action" value="addNewItem">
                <p>Item Name <input type="text" name="name" ></input></p>
                <p>Price <input type="double" name="price" ></input></p>
                <p>Stock Level <input type="integer" name="stock" ></input></p>
                <p><button type="submit" >Add New Item</button></p>
            </form>
        </c:if>
            
    </table>

    


 
</main>




<jsp:include page="footer.jsp" />
