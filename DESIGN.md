This document contains all the planning towards the project

# Features
### Home View
- List of all items
- Ability to add items to cart 
- Ability to remove items from cart

### Orders View
None as of yet

### Catalog View
- Admin - View all items in the catalog 
- Admin - Add new item to catalog 
- Admin - See total available items

### Cart View
- View all items & their quantity in the cart 
- View total cost of shopping cart 
- Enter and submit cart details for transaction
- Card details are checked and ReST client sends money
- When purchased stock levels are decreased

### Users 
- Create a new user 
- Login to an account 
- Edit account details 
- Logout of account 
- Admin - Edit an account details 

# Use Cases
Below are the use cases for the application from the point of view of 4 different users:  
`Anonymous` - A user who has not logged in  
`Customer` - A user who has logged in  
`Deactivated` - A user whose account is closed  
`Admin` - A user with elevated privileges

| ID 	| Role  	 | Action | Outcome |
| ----------- 	| ---------- | ----------- | ----------- |
| UC1			| Anonymous/Customer  | User loads into site | App displays home page with available items as a list |
| UC2			| Anonymous/Customer  | User adds item to cart | The app adds the item to the cart or increases the quantity of an existing item by 1 |
| UC3			| Anonymous/Customer  | User removes item | The app removes items from cart |
| UC4			| Anonymous  | User signs in  | The app stores the user in session storage |
| UC5			| Anonymous  | User signs up  | The app allows user to enter their details (name, address) and creates a new account in the database |
| UC6.1			| Customer  | User purchases the items in the shopping cart  | The app sends a request to the Bank REST API and sends the required total amount from the customer's card to the shop's card |
| UC6.2			| Customer  | User purchases the items in the shopping cart  | The items stock decreases by amount purchased |
| UC6.3 		| Customer  | User enters incorrect card details  | The user is displayed with an error message |
| UC7   		| Admin  | Admin adds a new item to the catalog  | The item is added to the database and becomes visible to customers |
| UC8   		| Admin  | Admin views all users  | The app shows a list of all users in the system |
| UC9 			| Admin  | Admin modifies a user | The app updates the users account to match inputted data|
| UC10 			| Customer/Admin  | User logs out  | The app removes the current user from session and turns role back to anonymous |
| UC11			| De-activated  | User attempts to login  | The app won't let user log in |

# Test Plan

### Anonymous and customer Tests
| Test ID  | Action  	 | Result |
| ----------- 	| ---------- | ---------- | 
| TC1			    | New user loads site  | Every product available is displayed on the home page |
| TC2.0    		| New user adds an item to their cart  | The cart page shows the product(s) in the cart |
| TC2.1    		|  New user adds the same item to their cart | The cart page shows the product quantity has increased by 1 |
| TC2.2    		|  New user keeps adding the same item to the cart  | A message is displayed to show the max quanitity has been reached |
| TC3   		  |  New user removes item from cart  | Item quantity in cart is decreased by 1 |
| TC4.0   		|  New user clicks "login or create a new account" and then clicks "create a new account"  | Account creation page loads |
| TC4.1   		|  User enters account details and submits  | Account is created and user is logged in |
| TC4.2 		|   User clicks on cart  | Items are stored in cart |
| TC5   		|  User clicks logout  | Account is logged out and user is returned to anonymous |
| TC6.0   		|  User clicks login  | Login page loads with all required inputs for log in  | 
| TC6.1   		|  User enters account details  | User is logged in to their account |

### Admin Tests
| Test ID 	| Action  	 | Result | 
| ----------- 	| ---------- | ---------- | 
| TC7			|  Admin clicks add item on the manage catalog page  | Page loads for admin to enter details for a new product |
| TC7.1  Admin enters product info and clicks save  | The product is displayed on the home and catalog page |
| TC8   	|  Admin opens the orders page | A page loads for orders  | 
| TC9  	| Admin clicks on the users page | A page loads containing all the users in the system  | 
| TC9.1  	|  Admin clicks modify user on the users page | A page loads containing all information about the user  | 
| TC9.2  	|  Admin edits user's details | The details are changed on the user's my profile page  | 
| TC9.3 	| Admin edits user's state to de-activated | The user is unable to log in to the system  | 
| TC9.4   	| Admin edits user's state to de-activated | The user is unable to log in to the system  | 
| TC10   	| Admin clicks on manage properties page| Page loads for admin to change properties
| TC10.1 	| Admin enters properties information in to the system and clicks save | Properties information changes and is saved in file |

# UML Diagram
## Class Diagram

## Web Class Diagram
![Web Class Diagram](Diagrams/Web%20Class.png "Class Diagram")
## Model Class Diagram
![Model Class Diagram](Diagrams/Model%20Class.png "Class Diagram")
## Service Class Diagram
![Service Class Diagram](Diagrams/Service%20Class.png "Class Diagram")
## DAO Class Diagram
![DAO Class Diagram](Diagrams/DAO%20Class.png "Class Diagram")

## Use Case Diagram
Below you can see the Use Case Diagram for the application. From the diagram you can see the 2 main actors, the User and the Admin, as well as a 3rd Party Actor - the bank. The diagram shows the possible actions of each actor and how each role extends actions from the other role.

![Use Cases](Diagrams/Use%20Case%20Diagram.drawio.png "Use Case Diagram")


## Robustness Diagram
Here is the robustness diagram for the whole system. Here we can see how each page is setup and how different objects interact with each other.

![Robustness Diagram](Diagrams/Robustness%20Diagram.drawio.png "Robustness Diagram")

## Sequence Diagram
Here is the sequence diagram for the home page of the application. Here you can see the different request to the page including GET requests and how these requests interact with other layers of the application including the session, service layer and data access layer.

![Sequence Diagram](Diagrams/SequenceDiagram.drawio.png "Sequence Diagram")

# Development

## Coding Standards
For this application I will use JavaDoc in order to comment all classes & methods in order to provide detailed explanation in what they do. I will also use JUnit to unit test all classes as well in order to maintain great test coverage.

## Git & SDLC Strategy
For this project I will keep track of my backlog through the use of Github Projects. I will be implementing Agile practices into my development lifecycle for this project and will hence use the Github Projects board as my backlog of tasks.  

I will then also setup a Continuous Integration pipeline within my Github Project in order to automatically build by application and run through all the unit tests. By doing this I will be able to identify issues in my code before the code is pushed to the master branch.

In order to keep track of releases I will also be using Github Releases which natively use Git tags.

# System Overview
This application is built with Java technologies and uses Maven as its build system. Using a build system like maven means that project dependencies are automatically added to the project simply by reading the pom files. Maven also allows multi-module projects meaning I can compile my Business Logic Projects to Jar files while compiling my web project to a single WAR file to be run in Tomcat.

This system will be built using the MVC architecture which is based on 3 main elements - Model, View & Controller. The use of MVC enables better organization of the project which in turn leads to cleaner code and easier expansion of additional features. The MVC framework I will be using for this application is Spring MVC.

## Model Layer 
For the model layer of the application I will use basic DTO (data transfer objects) which will be persisted in a database through the use of Spring Data JPA. These data repositories provide a simple data access layer to the application that allows data queries to be created either manually or through convention.

The benefits of using Spring Data is that they provide a layer of abstraction above direct database connections & queries - meaning I can focus my development time more on features for the application rather than writing database connection code and database queries.

Due to the fact this application will have the ability for users to sign in - I need a way to securely store user passwords. This is due to the fact that passwords should never be stored in plaintext anywhere in your application (including your database). In order to overcome this I will implement BCrypt to hash passwords which will then be stored in the database. This allows my application to simply compare hashes of passwords rather than ever having to work with passwords in plain text.
The benefit of using hashing is that a hash function is a one way function meaning that once the password is hashed, there is no way to get the original password back (without brute forcing it). 

## Controller Layer
For this application our business logic will be done at the controller layer. Each page in our application will have a controller which will fetch data from the database (through our model layer) and send it to our view layer. When the user changes data through the view it will then be passed back to our controller in order for the model/data access layer to update/save the changes. Simply put, our controller connects our persistence layer to our view.

By using MVC over pure JSPs I get the benefit of abstracting my business logic away from the page itself. Making the code for the views much cleaner and also helping to reduce duplicate code.

By using Spring for MVC I gain the benefits of Dependency Injection which allow me to annotate certain classes as Services or Components and simply 'Autowire' these into my controllers. This means that I can eliminate my use of Factories and simply provide singletons through "scope" annotations on the classes themselves.

For the services and repositories in my application, I am using the Facade pattern which hides the complexity of the class through the use of interfaces. For the services, I will create the interfaces first and then write the business logic behind them. However for the repositories, Spring Data allows me to simply write the interface and it'll auto-generate the code for the repository for me.


## View Layer
For the views for this application I will still use JSPs however I will also use JSTL (JavaServer Pages Tag Library). The tag library will allow me to add logic such as if statements or foreach loops to my views in order to enumerate through the data provided by the controller and conditionally show parts of the page. By using MVC these views will focus mainly on the UI of the application and will leave the business logic code to be implemented by the controllers and services
