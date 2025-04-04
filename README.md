# EmployWise Employee Management System

A Spring Boot application for employee management with CouchDB as the database.

## Table of Contents
- [Requirements](#requirements)
- [Setup](#setup)
- [Database Setup](#database-setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Deployment](#deployment)

## Requirements

- Java 17 or later
- Maven 3.8.x or later
- CouchDB 3.x or later
- Gmail account (for Advanced Level features)

## Setup

1. Clone the repository:
```bash
git clone <repository-url>
cd employee-management
```

2. Update application.properties with your CouchDB credentials:
```properties
couchdb.url=http://localhost:5984
couchdb.username=your_username
couchdb.password=your_password
couchdb.database=employwise_db
```

3. If implementing Advanced Level, update the email configuration:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_new_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Database Setup

1. Install and start CouchDB:
   - For Windows/Mac: Download from [CouchDB website](http://couchdb.apache.org/)
   - For Ubuntu: `sudo apt-get install couchdb`

2. Create a new database named 'employwise_db' in CouchDB:
   - Open CouchDB dashboard (usually at http://localhost:5984/_utils)
   - Go to Databases -> Create Database -> Name: employwise_db

## Running the Application

1. Build the application:
```bash
mvn clean install
```

2. Run the application:
```bash
mvn spring-boot:run
```

The application will start on port 8080 by default.

## API Documentation

### Entry Level APIs

#### 1. Add Employee
- **URL**: `/api/employees`
- **Method**: `POST`
- **Request Body**:
```json
{
  "employeeName": "John Doe",
  "phoneNumber": "1234567890",
  "email": "john.doe@example.com",
  "reportsTo": "manager-uuid",
  "profileImage": "https://example.com/profile.jpg"
}
```
- **Response**:
```json
{
  "id": "generated-uuid",
  "message": "Employee added successfully"
}
```

#### 2. Get All Employees
- **URL**: `/api/employees`
- **Method**: `GET`
- **Response**:
```json
[
  {
    "id": "employee-uuid",
    "employeeName": "John Doe",
    "phoneNumber": "1234567890",
    "email": "john.doe@example.com",
    "reportsTo": "manager-uuid",
    "profileImage": "https://example.com/profile.jpg"
  },
  ...
]
```

#### 3. Delete Employee by ID
- **URL**: `/api/employees/{id}`
- **Method**: `DELETE`
- **Response**:
```json
{
  "message": "Employee deleted successfully"
}
```

#### 4. Update Employee by ID
- **URL**: `/api/employees/{id}`
- **Method**: `PUT`
- **Request Body**:
```json
{
  "employeeName": "John Doe Updated",
  "phoneNumber": "9876543210",
  "email": "john.updated@example.com",
  "reportsTo": "new-manager-uuid",
  "profileImage": "https://example.com/new-profile.jpg"
}
```
- **Response**:
```json
{
  "id": "employee-uuid",
  "employeeName": "John Doe Updated",
  "phoneNumber": "9876543210",
  "email": "john.updated@example.com",
  "reportsTo": "new-manager-uuid",
  "profileImage": "https://example.com/new-profile.jpg"
}
```

### Intermediate Level APIs

#### 1. Get nth Level Manager
- **URL**: `/api/employees/manager`
- **Method**: `POST`
- **Request Body**:
```json
{
  "employeeId": "employee-uuid",
  "level": 2
}
```
- **Response**:
```json
{
  "id": "manager-uuid",
  "employeeName": "Manager Name",
  "phoneNumber": "1234567890",
  "email": "manager@example.com",
  "reportsTo": "higher-manager-uuid",
  "profileImage": "https://example.com/manager.jpg"
}
```

#### 2. Get Employees with Pagination and Sorting
- **URL**: `/api/employees/paginated`
- **Method**: `GET`
- **Query Parameters**:
  - `page` (default: 0): Page number
  - `size` (default: 10): Number of employees per page
  - `sortBy` (default: "employeeName"): Field to sort by
  - `sortDirection` (default: "asc"): Sort direction (asc or desc)
- **Response**:
```json
{
  "content": [
    {
      "id": "employee-uuid",
      "employeeName": "John Doe",
      "phoneNumber": "1234567890",
      "email": "john.doe@example.com",
      "reportsTo": "manager-uuid",
      "profileImage": "https://example.com/profile.jpg"
    },
    ...
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 5,
  "totalElements": 42,
  "last": false,
  "first": true,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "empty": false
}
```

## Deployment

The application can be deployed to free platforms like Heroku:

1. Create a Heroku account and install the Heroku CLI
2. Create a new Heroku app:
```bash
heroku create employwise-app
```

3. Set up environment variables:
```bash
heroku config:set COUCHDB_URL=your_couchdb_url
heroku config:set COUCHDB_USERNAME=your_username
heroku config:set COUCHDB_PASSWORD=your_password
heroku config:set COUCHDB_DATABASE=employwise_db
heroku config:set SPRING_MAIL_USERNAME=your_email@gmail.com
heroku config:set SPRING_MAIL_PASSWORD=your_app_password
```

4. Deploy the application:
```bash
git push heroku main
```

The application will be available at: https://employ
