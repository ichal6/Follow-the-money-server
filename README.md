# Follow the money
> There is an application for controlling your money. 
> Live demo [_server_](https://ftm-server.herokuapp.com).
> Live demo [_client_](https://ftm-client.herokuapp.com/).

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
- Java - version 11
- Spring Boot
- Hibernate
- PostgreSQL

Client side:
- Angular


## Features
List the ready features:
- Dashboard view with summary of accounts
- Accounts CRUD
- Categories CRUD
- Subcategories CRUD
- Payees CRUD
- Payments Create, Read, Delete

List of improve:
- Edit payments
- Add transfers
- Add subcategories in payments


## Screenshots
### Dashboard view:
![Dashboard](./img/dashboard.png)

### Mobile view:
![Mobile](./img/mobile.png)


## Setup
1. Install Java 11
2. Install [PostgreSQL](https://www.postgresql.org/download/) 
3. Set up a new system environment: [tutorial](https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html)
   1. SPRING_DATASOURCE_URL -> the url address for database (default for localhost: **jdbc:postgresql://localhost:5432/postgres**)
   2. SPRING_DATASOURCE_USERNAME -> the username, you should create it in database (default: **postgres**)
   3. SPRING_DATASOURCE_PASSWORD -> password for user (set up when user was created)
   4. You should restart your system
4. Now program should work

## Project Status
Project is: _in progress_ .


## Room for Improvement

- Repair bug in view
- Add payments edit
- Add transfer
- Include subcategories in payments


## Acknowledgements
Give credit here.
- Many thanks to my mentors from Codecool
- And Karolina Budzik for cooperation and design


## Contact
Michał Lechowicz


### Source for Readme file
Created by [@flynerdpl](https://www.flynerd.pl/)


Test

<!-- Optional -->
<!-- ## License -->
<!-- This project is open source and available under the [... License](). -->

<!-- You don't have to include all sections - just the one's relevant to your project -->
