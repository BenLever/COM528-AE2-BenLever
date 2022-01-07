# COM528 AE2
[Design Doc](https://github.com/BenLever/COM528-AE2-BenLever/blob/main/DESIGN.md) - Holds all diagrams, use cases, test plans and features

This e-commerce web application was built with Java using [Spring MVC](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mvc.html) and [Spring Boot](https://spring.io/projects/spring-boot). To handle the data used in the app, I am using [Hibernate](https://www.baeldung.com/the-persistence-layer-with-spring-and-jpa).



### To Use The App

At the base of the project run the following command
```
mvn clean install
```
Then move to the web project within Netbeans and right click the web project and click "Build With Dependencies"
After doing so, right click again on the web project and click "Run"

the project will be served at `http://localhost:8080/shoppingCartApplication/home`

## Requirements
This application has been tested with the following. Other variations may work but have have not been tested and hence are not supported:
 - Java 11
 - Tomcat 8.5
 - Browser - Google Chrome

## Defaults
By default when the app runs it will create 2 accounts (username = password):
 - `globaladmin`
 - `user1234`

## Setup
Signing in as Admin allows you to change the properties of the application. To do so navigate to the admin section in the header and click "Manage Properties". From here you can configure all the properties for the application including bank url and shopkeeper details.
