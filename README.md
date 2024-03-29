# Follow the money
> There is an application for controlling your money. 
> Live demo [_server_](https://ftm-server-prod.up.railway.app).
> Live demo [_client_](https://ichal6.github.io/Follow-the-money-Client).

## Table of Contents
* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Screenshots](#screenshots)
* [Setup](#setup)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Contact](#contact)
* [Source for Readme file](#Source-for-Readme-file)
<!-- * [License](#license) -->


## General Information
- This project use for controlling spend money. 
- In this application you can add categories, payments, accounts, payees.

## Technologies Used
Server side:
- Java - version 17
- Spring Boot
- Hibernate
- PostgreSQL
- Liquibase
- Testcontainers

Client side:
- Angular


## Features
List the ready features:
- Dashboard view with summary of accounts
- Accounts CRUD
- Categories CRUD
- Subcategories CRUD
- Payees CRUD
- Payments CRUD

List of improve:
- Add subcategories in payments
- Add Financial analysis


## Screenshots
### Dashboard view:
![Dashboard](./img/dashboard.png)

### Mobile view:
![Mobile](./img/mobile.png)


## Setup
1. Install Java 17
2. Install [PostgreSQL](https://www.postgresql.org/download/) 
3. Set up a new system environment: [tutorial for Windows](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html)
   1. FTM_DATASOURCE_URL -> the url address for database (default for localhost: **jdbc:postgresql://localhost:5432/postgres**)

      URL Scheme ```protocol//[hosts][/database]```:
      1. *protocol* -> (**jdbc:postgresql:**) protocol for connection. It always **jdbc:** and name of database provider (example **postgresql:** or **mysql:** ).  
      2. *hosts* -> (**localhost:5432**) domain name to identify a server to connection. Use localhost or website (example: **container.railway.app**) and number of port after colon (**:6622**).
      3. *database* -> (**postgres**) the database to open on server.
      
      Documentation for MySQL [link](https://docs.oracle.com/cd/E17952_01/connector-j-8.0-en/connector-j-reference-jdbc-url-format.html)
   2. FTM_DATASOURCE_USERNAME -> the username, you should create it in database (default: **postgres**)
   3. FTM_DATASOURCE_PASSWORD -> password for user (set up when user was created)
   4. PORT -> select port for server (ex. 8080)
   5. FTM_SECRET -> secret for JWT (example from: [generator online](https://www.allkeysgenerator.com/random/security-encryption-key-generator.aspx)). 512-bit should be ok (now is year 2023)
   6. You should restart your system
   7. If you run server from jar file active profile:
      1. dev -> Drop all database and insert dump data, enable debug mode 
      2. prod -> enable security connection
      3. [none] -> run app on default port and only connect with db
4. Now program should work

## Project Status
Project is: _in progress_ .


## Room for Improvement

- Repair bug in view
- Include subcategories in payments
- Improve dashboard with loan subsection
- Add User settings
- Add notification


## Acknowledgements
- Many thanks to my mentors from Codecool
- And Karolina Budzik for cooperation and design


## Contact
Michał Lechowicz <br />
[LinkedIn](https://www.linkedin.com/in/micha%C5%82-lechowicz/)

### Source for Readme file
Created by [@flynerdpl](https://www.flynerd.pl/)


<!-- Optional -->
<!-- ## License -->
<!-- This project is open source and available under the [... License](). -->

<!-- You don't have to include all sections - just the one's relevant to your project -->
