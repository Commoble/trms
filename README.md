# Tuition Reimbursement Management System

## Project Overview

## Features
### Implemented Features
* Users can log in to the web app
* Users can submit new reimbursement requests
* Users can view their previously submitted reimbursement requests and the status thereof
* Authorized users can approve or deny pending reimbursement requests approvable by that user
* Authorized users can award reimbursements when the awarding conditions are met

### Not Yet Implemented
* Encryption
* Uploading file attachments with requests
* Deleting reimbursement requests

## Technologies Used
* HTML/CSS/Javascript was used to create the frontend presentation
* Postman was used to test the ReST API
* Java and Maven were used to create the API server and backend
* Hibernate was used to interact with the database server
* PostgreSQL was used to define the database schema
* JUnit4, Selenium, and Cucumber were used to test the project

## Usage Instructions
1. Fork and clone the git project.
2. Set up a PostgreSQL database. A sql script to set up the relevant tables and some sample users is included. These tables should be created in a "trms" schema.
3. Change the database url, username, and password properties in src/main/resources/hibernate.cfg.xml to match your database's credentials.
4. Run App.main() to start the API server (built jars are not yet available)
5. Open frontend/trms.html in a browser to open the web app